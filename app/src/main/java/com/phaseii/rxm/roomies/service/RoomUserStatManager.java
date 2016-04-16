package com.phaseii.rxm.roomies.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.phaseii.rxm.roomies.database.dao.RoomDetailsDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomStatsDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomUserMapDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomUserStatDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomiesDao;
import com.phaseii.rxm.roomies.database.model.RoomDetails;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.database.model.RoomUserMap;
import com.phaseii.rxm.roomies.database.model.RoomUserStatData;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.ServiceParam;
import com.phaseii.rxm.roomies.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.utils.Constants.APP_ERROR;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ID;

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
    private RoomiesLogger logger = RoomiesLogger.getInstance();

    /**
     * @param mContext
     */
    public RoomUserStatManager(Context mContext) {
        this.mContext = mContext;
        isDataLoaded = false;
        isDataInserted = false;
        mSharedPref = mContext.getSharedPreferences(Constants.PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE);
    }

    /**
     * @param username
     * @return
     */
    public List<RoomUserStatData> loadRoomDetails(String username) {
        isDataLoaded = false;
        Map<ServiceParam, Object> paramMap = new HashMap<>();

        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        projectionParams.add(QueryParam.USER_ALIAS);
        projectionParams.add(QueryParam.ROOM_ID);
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

        if (null != username) {
            List<QueryParam> selectionParams = new ArrayList<QueryParam>();
            selectionParams.add(QueryParam.USERNAME);
            paramMap.put(ServiceParam.SELECTION, selectionParams);

            List<String> selectionArgs = new ArrayList<String>();
            selectionArgs.add(username);
            paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        }
        roomiesDao = new RoomUserStatDaoImpl(mContext);
        List<RoomUserStatData> roomUserStatDataList = (List<RoomUserStatData>) roomiesDao
                .getDetails(paramMap);

        return roomUserStatDataList;
    }

    /**
     * @param username
     * @param roomDetails
     * @param roomStats
     * @return
     */
    public boolean storeRoomDetails(String username, RoomDetails roomDetails, RoomStats
            roomStats) {

        isDataInserted = false;
        Map<ServiceParam, RoomDetails> roomDetailsMap = new HashMap<>();
        Map<ServiceParam, RoomStats> roomStatsMap = new HashMap<>();
        Map<ServiceParam, RoomUserMap> roomUserMap = new HashMap<>();

        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> projections = new ArrayList<>();
        List<QueryParam> selections = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();
        projections.add(QueryParam.ID);
        selections.add(QueryParam.USERNAME);
        selectionArgs.add(username);

        paramMap.put(ServiceParam.PROJECTION, projections);
        paramMap.put(ServiceParam.SELECTION, selections);
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);

        int userId = Integer.valueOf(mSharedPref.getString(PREF_USER_ID, "0"));
        if (userId >= 0) {
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
                    roomUser.setUserId(userId);
                    roomUser.setRoomId(roomId);
                    roomUserMap.put(ServiceParam.MODEL, roomUser);
                    roomiesDao = new RoomUserMapDaoImpl(mContext);
                    if (roomiesDao.insertDetails(roomUserMap) > 0) {
                        isDataInserted = true;
                        /*ToastUtils.createToast(mContext,
                                "Room details saved and expense set", mToast);*/
                    }
                } else {
                    ToastUtils.createToast(mContext, APP_ERROR, mToast);
                    System.exit(0);
                }

            } else {
                ToastUtils.createToast(mContext, APP_ERROR, mToast);
                System.exit(0);
            }
        } else {
            ToastUtils.createToast(mContext, APP_ERROR, mToast);
            System.exit(0);
        }
        return isDataInserted;
    }

    public boolean deleteRoomDetails(int userId) {
        boolean isDataDeleted = false;
        int rowsDeleted = 0;
        Map<ServiceParam, Object> queryMap = new HashMap<>();
        List<QueryParam> projections = new ArrayList<>();
        List<QueryParam> selections = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();

        /*get roomId from {@link RoomiesContract.RoomDetails room_user} table*/
        projections.add(QueryParam.ROOM_ID);
        selections.add(QueryParam.USER_ID);
        selectionArgs.add(String.valueOf(userId));
        roomiesDao = new RoomUserMapDaoImpl(mContext);
        queryMap.put(ServiceParam.PROJECTION, projections);
        queryMap.put(ServiceParam.SELECTION, selections);
        queryMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        List<RoomUserMap> roomUserList = (List<RoomUserMap>) roomiesDao.getDetails(queryMap);
        queryMap.clear();
        projections.clear();
        selections.clear();
        selectionArgs.clear();

        /*delete details for each room_id returned*/
        for (RoomUserMap roomUserMap : roomUserList) {
            logger.info("RoomId:" + roomUserMap.getRoomId() + " retrieved for userId:" + userId);
            selections.add(QueryParam.ROOM_ID);
            selectionArgs.add(String.valueOf(roomUserMap.getRoomId()));
            queryMap.put(ServiceParam.SELECTION, selections);
            queryMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
            /*delete from room_details table*/
            roomiesDao = new RoomDetailsDaoImpl(mContext);
            int detailsDeleted = roomiesDao.deleteDetails(queryMap);
            logger.info(detailsDeleted + " rows from Room details deleted for roomId:"
                    + roomUserMap.getRoomId());

            if (detailsDeleted > 0) {
                /*delete from room_stats table*/
                roomiesDao = new RoomStatsDaoImpl(mContext);
                int statDeleted = roomiesDao.deleteDetails(queryMap);
                logger.info(statDeleted + " rows from Room stats deleted for roomId:"
                        + roomUserMap.getRoomId());

                /*delete from room_expense table*/
                if (statDeleted > 0) {
                    roomiesDao = new RoomExpensesDaoImpl(mContext);
                    int expenseDeleted = roomiesDao.deleteDetails(queryMap);
                    logger.info(
                            expenseDeleted + " rows from Room expense deleted for roomId:"
                                    + roomUserMap.getRoomId());
                    rowsDeleted++;
                }
            }
        }

        if (rowsDeleted == roomUserList.size()) {
            isDataDeleted = true;
        }
        return isDataDeleted;
    }
}
