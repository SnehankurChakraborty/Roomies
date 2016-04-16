package com.phaseii.rxm.roomies.service;

import android.content.Context;

import com.phaseii.rxm.roomies.database.dao.RoomDetailsDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomiesDao;
import com.phaseii.rxm.roomies.database.model.RoomDetails;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 1/23/2016.
 */
public class RoomDetailsManager {

    private Context mContext;
    private RoomiesDao roomiesDao;

    public RoomDetailsManager(Context mContext) {
        this.mContext = mContext;
    }

    public int storeRoomDetails(RoomDetails roomDetail) {
        Map<ServiceParam, RoomDetails> roomDetailsMap = new HashMap<>();
        roomDetailsMap.put(ServiceParam.MODEL, roomDetail);
        roomiesDao = new RoomDetailsDaoImpl(mContext);
        int roomId = roomiesDao.insertDetails(roomDetailsMap);
        return roomId;
    }
}
