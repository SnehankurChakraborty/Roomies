/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.phaseii.rxm.roomies.database.dao.RoomStatsDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomUserMapDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomiesDao;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.database.model.RoomUserMap;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOMIES_KEY;

/**
 * Created by Snehankur on 9/6/2015.
 */
public class RoomStatManager {

    private Context mContext;
    private SharedPreferences mSharedPref;
    private RoomiesDao roomiesDao;
    private Map<ServiceParam, RoomStats> roomStatsMap;
    private RoomiesLogger log;

    /**
     *
     * @param mContext
     */
    public RoomStatManager(Context mContext) {
        this.mContext = mContext;
        this.mSharedPref = mContext.getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
        this.log = RoomiesLogger.getInstance();
    }

    /**
     *
     * @return
     */
    public boolean addNewMonthData() {
        int rowsRetrieved = 0;
        int rowsInserted = 0;

        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        projectionParams.add(QueryParam.ROOM_ID);
        projectionParams.add(QueryParam.RENT_MARGIN);
        projectionParams.add(QueryParam.MAID_MARGIN);
        projectionParams.add(QueryParam.ELECTRICITY_MARGIN);
        projectionParams.add(QueryParam.MISCELLANEOUS_MARGIN);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.MONTH_YEAR);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(DateUtils.getCurrentMonthYear());
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);

        List<RoomStats> roomStats = (List<RoomStats>) roomiesDao.getDetails(paramMap);
        rowsRetrieved = roomStats.size();

        roomStatsMap = new HashMap<>();
        this.roomiesDao = new RoomStatsDaoImpl(mContext);
        for (RoomStats roomStat : roomStats) {
            roomStat.setMonthYear(DateUtils.getNextMonthYear());
            roomStatsMap.put(ServiceParam.MODEL, roomStat);
            if (roomiesDao.insertDetails(roomStatsMap) > 0) {
                rowsInserted++;
            }
        }

        return rowsRetrieved == rowsInserted;
    }

    /**
     *
     * @return
     */
    public List<RoomStats> getAllRoomStats(String userId) {

        //prepare for room-user map dao call
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        List<String> selectionArgs = new ArrayList<String>();
        Map<QueryParam, Object> selectonInParams = new HashMap<QueryParam, Object>();

        projectionParams.add(QueryParam.USER_ID);
        projectionParams.add(QueryParam.ROOM_ID);
        selectionParams.add(QueryParam.USER_ID);
        selectionArgs.add(userId);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);
        paramMap.put(ServiceParam.SELECTION, selectionParams);
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        this.roomiesDao = new RoomUserMapDaoImpl(mContext);
        List<RoomUserMap> roomUserMapping = (List<RoomUserMap>) roomiesDao.getDetails(paramMap);

        //clear all the list to prepare for room stats dao call
        paramMap.clear();
        projectionParams.clear();
        selectionParams.clear();
        selectionArgs.clear();

        projectionParams.add(QueryParam.ROOM_ID);
        projectionParams.add(QueryParam.RENT_MARGIN);
        projectionParams.add(QueryParam.MAID_MARGIN);
        projectionParams.add(QueryParam.ELECTRICITY_MARGIN);
        projectionParams.add(QueryParam.MISCELLANEOUS_MARGIN);
        projectionParams.add(QueryParam.RENT_SPENT);
        projectionParams.add(QueryParam.MAID_SPENT);
        projectionParams.add(QueryParam.ELECTRICITY_SPENT);
        projectionParams.add(QueryParam.MISCELLANEOUS_SPENT);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        selectionParams.add(QueryParam.MONTH_YEAR);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        selectionArgs.add(DateUtils.getCurrentMonthYear());
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);

        List<String> roomIds = new ArrayList<>();
        for (RoomUserMap roomUser : roomUserMapping) {
            String user = String.valueOf(roomUser.getUserId());
            if (user.equals(userId)) {
                roomIds.add(String.valueOf(roomUser.getRoomId()));
            }
        }
        selectonInParams.put(QueryParam.ROOM_ID, roomIds);
        paramMap.put(ServiceParam.SELECTION_IN, selectonInParams);

        this.roomiesDao = new RoomStatsDaoImpl(mContext);
        List<RoomStats> roomStatsList = (List<RoomStats>) roomiesDao.getDetails(paramMap);
        return roomStatsList;
    }

    /**
     *
     * @param months
     * @param roomId
     * @return
     */
    public List<RoomStats> getRoomStatsTrend(List<String> months, String roomId) {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        List<RoomStats> roomTrendList = new ArrayList<>();
        Map<QueryParam, String> queryParams = new HashMap<>();

        /*setting projection*/
        projectionParams.add(QueryParam.ROOM_ID);
        projectionParams.add(QueryParam.RENT_MARGIN);
        projectionParams.add(QueryParam.MAID_MARGIN);
        projectionParams.add(QueryParam.ELECTRICITY_MARGIN);
        projectionParams.add(QueryParam.MISCELLANEOUS_MARGIN);
        projectionParams.add(QueryParam.RENT_SPENT);
        projectionParams.add(QueryParam.MAID_SPENT);
        projectionParams.add(QueryParam.ELECTRICITY_SPENT);
        projectionParams.add(QueryParam.MISCELLANEOUS_SPENT);
        projectionParams.add(QueryParam.MONTH_YEAR);

        queryParams.put(QueryParam.ROOM_ID, roomId);
        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.MONTH_YEAR);

        paramMap.put(ServiceParam.SELECTION, selectionParams);
        paramMap.put(ServiceParam.QUERY_ARGS, queryParams);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);
        this.roomiesDao = new RoomStatsDaoImpl(mContext);
        for (String month : months) {
            List<String> selectionArgs = new ArrayList<String>();
            selectionArgs.add(month);
            paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
            List<RoomStats> roomStatsList = (List<RoomStats>) roomiesDao.getDetails(paramMap);
            if (roomStatsList.size() > 0 && null != roomStatsList.get(0)) {
                roomTrendList.add(roomStatsList.get(0));
            }
        }
        return roomTrendList;

    }
}
