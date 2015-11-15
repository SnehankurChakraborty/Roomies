package com.phaseii.rxm.roomies.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.phaseii.rxm.roomies.dao.RoomDetailsDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomStatsDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomUserMapDao;
import com.phaseii.rxm.roomies.dao.RoomUserStatDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.dao.UserDetailsDaoImpl;
import com.phaseii.rxm.roomies.model.RoomDetails;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.model.RoomUserMap;
import com.phaseii.rxm.roomies.model.RoomUserStatData;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.APP_ERROR;

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

    /**
     *
     * @param mContext
     */
    public RoomUserStatManager(Context mContext) {
        this.mContext = mContext;
        roomiesDao = new RoomUserStatDaoImpl(mContext);
        isDataLoaded = false;
        isDataInserted = false;
        mSharedPref = mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE);
    }

    /**
     *
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
            paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);
        }

        List<RoomUserStatData> roomUserStatDataList = (List<RoomUserStatData>) roomiesDao
                .getDetails(paramMap);

        return roomUserStatDataList;
    }

    /**
     *
     * @param userDetails
     * @param roomDetails
     * @param roomStats
     * @return
     */
    public boolean storeRoomDetails(UserDetails userDetails, RoomDetails roomDetails, RoomStats
            roomStats) {

        isDataInserted = false;
        Map<ServiceParam, RoomDetails> roomDetailsMap = new HashMap<>();
        Map<ServiceParam, RoomStats> roomStatsMap = new HashMap<>();
        Map<ServiceParam, RoomUserMap> roomUserMap = new HashMap<>();

        Map<ServiceParam, UserDetails> userMap = new HashMap<>();
        userMap.put(ServiceParam.MODEL, userDetails);
        roomiesDao = new UserDetailsDaoImpl(mContext);
        int userId = roomiesDao.insertDetails(userMap);

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
                    roomiesDao = new RoomUserMapDao(mContext);
                    if (roomiesDao.insertDetails(roomUserMap) > 0) {
                        isDataInserted = true;
                        ToastUtils.createToast(mContext,
                                "Room details saved and expense set", mToast);
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
}
