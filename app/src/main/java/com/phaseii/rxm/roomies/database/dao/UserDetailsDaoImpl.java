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
import com.phaseii.rxm.roomies.database.model.RoomiesModel;
import com.phaseii.rxm.roomies.database.model.UserDetails;
import com.phaseii.rxm.roomies.database.provider.UserDetailsProvider;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.UserDetails.USER_SENDER_ID;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.UserDetails.USER_USERNAME;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.UserDetails.USER_USER_ALIAS;
import static com.phaseii.rxm.roomies.utils.RoomiesHelper.listToProjection;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class UserDetailsDaoImpl implements RoomiesDao {

    private Context mContext;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private UserDetails user;
    private ContentValues values;
    private Uri userDetailsUri;

    public UserDetailsDaoImpl(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {

        List<QueryParam> projectionList = (List<QueryParam>) queryMap.get(ServiceParam.PROJECTION);
        List<QueryParam> selectionList = (List<QueryParam>) queryMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) queryMap.get(ServiceParam.SELECTION_ARGS);
        String sortOrder = (String) queryMap.get(ServiceParam.SORT_ORDER);

        String[] projection = listToProjection(projectionList);
        selection = null;
        selectionArgs = null;
        if (null != selectionList && selectionList.size() > 0) {

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
        Map<QueryParam, String> queryParams = (Map<QueryParam, String>) queryMap.get
                (ServiceParam.QUERY_ARGS);
        if (null != queryParams && queryParams.size() > 0) {
            for (QueryParam param : queryParams.keySet()) {
                if (param.equals(QueryParam.USER_ID)) {
                    queryArg = "month/" + queryParams.get(param);
                } else if (param.equals(QueryParam.USER_ALIAS)) {
                    queryArg = "room/" + queryParams.get(param);
                } else if (param.equals(QueryParam.SENDER_ID)) {
                    queryArg = "room/" + queryParams.get(param);
                }
            }
        }
        if (null != queryArg) {
            userDetailsUri = Uri.withAppendedPath(UserDetailsProvider.CONTENT_URI,
                    queryArg);
        } else {
            userDetailsUri = UserDetailsProvider.CONTENT_URI;
        }

        Cursor cursor = null;
        List<UserDetails> userDetailsList = new ArrayList<>();
        try {
            cursor = mContext.getContentResolver().query(userDetailsUri, projection, selection,
                    selectionArgs, sortOrder);
            if (null != cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    UserDetails user = new UserDetails();
                    user.setUserId(cursor.getColumnIndex(_ID) >= 0 ? cursor.getInt(cursor
                            .getColumnIndex(_ID)) : -1);
                    user.setUsername(cursor.getColumnIndex(USER_USERNAME) >= 0 ? cursor.getString
                            (cursor.getColumnIndex(USER_USERNAME)) : null);
                    user.setUserAlias(
                            cursor.getColumnIndex(USER_USER_ALIAS) >= 0 ? cursor.getString(cursor
                                    .getColumnIndex(USER_USER_ALIAS)) : null);
                    user.setSenderId(cursor.getColumnIndex(USER_SENDER_ID) >= 0 ?
                            cursor.getString(cursor.getColumnIndex(USER_SENDER_ID)) : null);
                    userDetailsList.add(user);
                    cursor.moveToNext();
                }
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return userDetailsList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {
        user = (UserDetails) detailsMap.get(ServiceParam.MODEL);
        int row = 0;
        values = new ContentValues();
        values.put(RoomiesContract.UserDetails.USER_USERNAME, null != user.getUsername() ? user
                .getUsername() : null);
        values.put(RoomiesContract.UserDetails.USER_USER_ALIAS,
                null != user.getUserAlias() ? user.getUserAlias() : null);
        values.put(RoomiesContract.UserDetails.USER_SENDER_ID, null != user.getSenderId() ? user
                .getSenderId() : null);
        Uri resultUri = mContext.getContentResolver().insert(UserDetailsProvider.CONTENT_URI,
                values);
        row = Integer.parseInt(resultUri.getLastPathSegment());

        return row;
    }

    /**
     *
     * @param detailsMap
     * @return
     */
    @Override public int deleteDetails(Map<ServiceParam, ?> detailsMap) {
        return 0;
    }

    @Override
    public int updateDetails(Map<ServiceParam, ?> detailsMap) {
        prepareStatement(detailsMap);
        int rowsUpdated = mContext.getContentResolver().update(UserDetailsProvider.CONTENT_URI,
                values, selection, selectionArgs);
        return rowsUpdated;
    }

    public void prepareStatement(Map<ServiceParam, ?> detailsMap) {

        List<QueryParam> selectionList = (List<QueryParam>) detailsMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) detailsMap.get(ServiceParam.SELECTION_ARGS);

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

        if (null != detailsMap.get(ServiceParam.MODEL)) {
            values = new ContentValues();
            user = (UserDetails) detailsMap.get(ServiceParam.MODEL);

            if (null != user.getUsername()) {
                values.put(RoomiesContract.UserDetails.USER_USERNAME, user.getUsername());
            }
            if (null != user.getUserAlias()) {
                values.put(RoomiesContract.UserDetails.USER_USER_ALIAS, user.getUserAlias());
            }
            if (null != user.getSenderId()) {
                values.put(RoomiesContract.UserDetails.USER_SENDER_ID, user.getSenderId());
            }
            if (null != user.getPhone()) {
                values.put(RoomiesContract.UserDetails.USER_PHONE, user.getPhone());
            }
            if (null != user.getLocation()) {
                values.put(RoomiesContract.UserDetails.USER_LOCATION, user.getLocation());
            }
            if (null != user.getAboutMe()) {
                values.put(RoomiesContract.UserDetails.USER_ABOUT_ME, user.getAboutMe());
            }
        }
    }
}
