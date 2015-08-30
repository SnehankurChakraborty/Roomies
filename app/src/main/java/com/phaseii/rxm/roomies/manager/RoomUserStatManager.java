package com.phaseii.rxm.roomies.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.phaseii.rxm.roomies.dao.RoomDetailsDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomStatsDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomUserMapDao;
import com.phaseii.rxm.roomies.dao.RoomUserStatDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomDetails;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.model.RoomUserMap;
import com.phaseii.rxm.roomies.model.RoomUserStatData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USER_ID;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserStatManager {

    private boolean isDataLoaded;
    private RoomiesDao roomiesDao;
    private Context mContext;
    private Toast mToast;
    private boolean isDataInserted;
    private SharedPreferences mSharedPref;

    public RoomUserStatManager(Context mContext) {
        this.mContext = mContext;
        roomiesDao = new RoomUserStatDaoImpl(mContext);
        isDataLoaded = false;
        isDataInserted = false;
        mSharedPref = mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE);
    }

    public boolean loadRoomDetails(String username) {
        isDataLoaded = false;
        Map<ServiceParam, Object> paramMap = new HashMap<>();

        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        projectionParams.add(QueryParam.USERALIAS);
        projectionParams.add(QueryParam.ROOMID);
        projectionParams.add(QueryParam.ROOM_ALIAS);
        projectionParams.add(QueryParam.NO_OF_PERSONS);
        projectionParams.add(QueryParam.MONTH_YEAR);
        projectionParams.add(QueryParam.RENT_MARGIN);
        projectionParams.add(QueryParam.MAID_MARGIN);
        projectionParams.add(QueryParam.ELECTRICITY_MARGIN);
        projectionParams.add(QueryParam.MISCELLANEOUS_MARGIN);
        projectionParams.add(QueryParam.RENT_SPENT);
        projectionParams.add(QueryParam.MAID_SPENT);
        projectionParams.add(QueryParam.ELECTRICITY_SPENT);
        projectionParams.add(QueryParam.MISCELLANEOUS_SPENT);
        projectionParams.add(QueryParam.TOTAL);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.USERNAME);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(username);
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        List<RoomUserStatData> roomUserStatDataList = (List<RoomUserStatData>) roomiesDao.getDetails(
                paramMap);

        if (roomUserStatDataList.size() > 0) {
            RoomiesHelper.cacheDBtoPreferences(mContext, roomUserStatDataList.get(0), null, null,
                    null, false);
            isDataLoaded = true;
        }

        return isDataLoaded;
    }

    public boolean storeRoomDetails(RoomDetails roomDetails, RoomStats roomStats) {

        isDataInserted = false;
        Map<ServiceParam, RoomDetails> roomDetailsMap = new HashMap<>();
        Map<ServiceParam, RoomStats> roomStatsMap = new HashMap<>();
        Map<ServiceParam, RoomUserMap> roomUserMap = new HashMap<>();
        SharedPreferences.Editor mEditor = mSharedPref.edit();

        roomDetailsMap.put(ServiceParam.MODEL, roomDetails);
        roomiesDao = new RoomDetailsDaoImpl(mContext);
        int roomId = roomiesDao.insertDetails(roomDetailsMap);

        if (roomId >= 0) {

            roomDetails.setRoomId(roomId);
            roomStats.setRoomId(roomId);

            roomStatsMap.put(ServiceParam.MODEL, roomStats);
            roomiesDao = new RoomStatsDaoImpl(mContext);
            int statId = roomiesDao.insertDetails(roomStatsMap);

            if (statId >= 0) {
                roomStats.setStatsId(statId);

                RoomUserMap roomUser = new RoomUserMap();
                roomUser.setUserId(Integer.valueOf(mSharedPref.getString(PREF_USER_ID, null)));
                roomUser.setRoomId(roomId);
                roomUserMap.put(ServiceParam.MODEL, roomUser);

                roomiesDao = new RoomUserMapDao(mContext);
                if (roomiesDao.insertDetails(roomUserMap) > 0) {
                    isDataInserted = true;
                    RoomiesHelper.createToast(mContext,
                            "Room details saved and expense set", mToast);
                }

            } else {
                RoomiesHelper.createToast(mContext, APP_ERROR, mToast);
                System.exit(0);
            }
        } else {
            RoomiesHelper.createToast(mContext, APP_ERROR, mToast);
            System.exit(0);
        }
        return isDataInserted;
    }
}
