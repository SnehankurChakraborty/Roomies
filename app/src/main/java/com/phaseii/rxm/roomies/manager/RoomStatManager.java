package com.phaseii.rxm.roomies.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.phaseii.rxm.roomies.dao.RoomStatsDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.util.DateUtils;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.RoomiesHelper;
import com.phaseii.rxm.roomies.util.ServiceParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;

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
        this.roomiesDao = new RoomStatsDaoImpl(mContext);
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
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        List<RoomStats> roomStats = (List<RoomStats>) roomiesDao.getDetails(paramMap);
        rowsRetrieved = roomStats.size();

        roomStatsMap = new HashMap<>();
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
    public RoomStats getCurrentRoomStats() {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        List<String> selectionArgs = new ArrayList<String>();

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
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        paramMap.put(ServiceParam.QUERYARGS, QueryParam.ROOM_ID);
        List<RoomStats> roomStatsList = (List<RoomStats>) roomiesDao.getDetails(paramMap);
        RoomStats roomStats = roomStatsList.get(0);
        RoomiesHelper.cacheDBtoPreferences(mContext, null, null, null, roomStats, false);
        return roomStats;
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
        paramMap.put(ServiceParam.QUERYARGS, queryParams);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        for (String month : months) {
            List<String> selectionArgs = new ArrayList<String>();
            selectionArgs.add(month);
            paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);
            List<RoomStats> roomStatsList = (List<RoomStats>) roomiesDao.getDetails(paramMap);
            if (roomStatsList.size() > 0 && null != roomStatsList.get(0)) {
                roomTrendList.add(roomStatsList.get(0));
            }
        }
        return roomTrendList;

    }
}
