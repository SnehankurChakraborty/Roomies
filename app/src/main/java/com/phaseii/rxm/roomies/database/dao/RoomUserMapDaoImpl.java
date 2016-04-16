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

package com.phaseii.rxm.roomies.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.contract.RoomiesContract;
import com.phaseii.rxm.roomies.database.model.RoomUserMap;
import com.phaseii.rxm.roomies.database.model.RoomiesModel;
import com.phaseii.rxm.roomies.database.provider.RoomUserMapProvider;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomUserMap.ROOM_USER_USER_ID;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserMapDaoImpl implements RoomiesDao {

    private Context mContext;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private RoomUserMap roomUserMap;
    private ContentValues values;
    private Uri roomUserMapUri;

    public RoomUserMapDaoImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {
        prepareStatement(queryMap);
        Cursor cursor = null;
        List<RoomUserMap> roomUserMapList = new ArrayList<>();
        try {
            cursor = mContext.getContentResolver().query(roomUserMapUri, projection, selection,
                    selectionArgs, sortOrder);

            if (null != cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    RoomUserMap roomUserMap = new RoomUserMap();
                    roomUserMap.setRoomId(cursor.getColumnIndex(ROOM_USER_ROOM_ID) >= 0 ? cursor
                            .getInt(cursor.getColumnIndex(ROOM_USER_ROOM_ID)) : -1);
                    roomUserMap.setUserId(
                            cursor.getColumnIndex(ROOM_USER_USER_ID) >= 0 ? cursor.getInt(cursor
                                    .getColumnIndex(ROOM_USER_USER_ID)) : -1);
                    roomUserMapList.add(roomUserMap);
                    cursor.moveToNext();
                }
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return roomUserMapList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {
        int row = 0;
        prepareStatement(detailsMap);

        Uri resultUri = mContext.getContentResolver().insert(RoomUserMapProvider.CONTENT_URI,
                values);
        row = Integer.parseInt(resultUri.getLastPathSegment());

        return row;
    }

    @Override
    public int deleteDetails(Map<ServiceParam, ?> detailsMap) {

        prepareStatement(detailsMap);
        return mContext.getContentResolver().delete(RoomUserMapProvider.CONTENT_URI,
                selection, selectionArgs);
    }

    @Override
    public int updateDetails(Map<ServiceParam, ?> detailsMap) {
        prepareStatement(detailsMap);
        int rowsUpdated = mContext.getContentResolver().update(RoomUserMapProvider.CONTENT_URI,
                values, selection, selectionArgs);
        return rowsUpdated;
    }

    public void prepareStatement(Map<ServiceParam, ?> detailsMap) {

        List<QueryParam> selectionList = (List<QueryParam>) detailsMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) detailsMap.get(ServiceParam.SELECTION_ARGS);
        List<QueryParam> projectionList = (List<QueryParam>) detailsMap.get(
                ServiceParam.PROJECTION);
        sortOrder = (String) detailsMap.get(ServiceParam.SORT_ORDER);

        projection = RoomiesHelper.listToProjection(projectionList);
        if (null != selectionList && selectionList.size() > 0) {
            selection = null;
            for (QueryParam select : selectionList) {
                if (null == selection) {
                    selection = select.toString() + "=?";
                } else {
                    selection = selection + " AND " + select.toString() + "=?";
                }
            }
        }

        if (null != selectionArgsList && null != selection && selectionArgsList.size() > 0) {
            selectionArgs = new String[selectionArgsList.size()];
            selectionArgsList.toArray(selectionArgs);
        }

        String queryArg = null;
        Map<QueryParam, String> queryParams = (Map<QueryParam, String>) detailsMap.get
                (ServiceParam.QUERY_ARGS);
        if (null != queryParams && queryParams.size() > 0) {
            for (QueryParam param : queryParams.keySet()) {
                if (param.equals(QueryParam.MONTH_YEAR)) {
                    queryArg = "month/" + queryParams.get(param);
                } else if (param.equals(QueryParam.ROOM_ID)) {
                    queryArg = "room/" + queryParams.get(param);
                }
            }
        }
        if (null != queryArg) {
            roomUserMapUri = Uri.withAppendedPath(RoomUserMapProvider.CONTENT_URI,
                    queryArg);
        } else {
            roomUserMapUri = RoomUserMapProvider.CONTENT_URI;
        }

        if (null != detailsMap.get(ServiceParam.MODEL)) {
            values = new ContentValues();
            roomUserMap = (RoomUserMap) detailsMap.get(ServiceParam.MODEL);

            if (roomUserMap.getRoomId() >= 0) {
                values.put(RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID, roomUserMap.getRoomId());
            }
            if (roomUserMap.getUserId() >= 0) {
                values.put(RoomiesContract.RoomUserMap.ROOM_USER_USER_ID, roomUserMap.getUserId());
            }
        }
    }
}
