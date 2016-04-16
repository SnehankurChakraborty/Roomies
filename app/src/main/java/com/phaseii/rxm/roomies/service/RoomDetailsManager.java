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
