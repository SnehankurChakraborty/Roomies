package com.phaseii.rxm.roomies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomiesModel;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.providers.UserDetailsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_PASSWORD;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_SENDER_ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_USERNAME;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_USER_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.listToProjection;

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

    public UserDetailsDaoImpl(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {

        List<QueryParam> projectionList = (List<QueryParam>) queryMap.get(ServiceParam.PROJECTION);
        List<QueryParam> selectionList = (List<QueryParam>) queryMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) queryMap.get(ServiceParam.SELECTIONARGS);
        String sortOrder = (String) queryMap.get(ServiceParam.SORTORDER);

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

        Cursor cursor = mContext.getContentResolver().query(UserDetailsProvider
                .CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        List<UserDetails> userDetailsList = null;
        if (null != cursor) {
            cursor.moveToFirst();
            userDetailsList = new ArrayList<>();
            while (!cursor.isAfterLast()) {

                UserDetails user = new UserDetails();

                user.setUserId(cursor.getColumnIndex(_ID) >= 0 ? cursor.getInt(cursor
                        .getColumnIndex(_ID)) : -1);
                user.setUsername(cursor.getColumnIndex(USER_USERNAME) >= 0 ? cursor.getString(cursor
                        .getColumnIndex(USER_USERNAME)) : null);
                user.setPassword(cursor.getColumnIndex(USER_PASSWORD) >= 0 ? cursor.getString(cursor
                        .getColumnIndex(USER_PASSWORD)) : null);
                user.setUserAlias(
                        cursor.getColumnIndex(USER_USER_ALIAS) >= 0 ? cursor.getString(cursor
                                .getColumnIndex(USER_USER_ALIAS)) : null);
                user.setSenderId(cursor.getColumnIndex(USER_SENDER_ID) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(USER_SENDER_ID)) : null);
                userDetailsList.add(user);

                cursor.moveToNext();
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
        values.put(RoomiesContract.UserDetails.USER_PASSWORD, null != user.getPassword() ? user
                .getPassword() : null);
        values.put(RoomiesContract.UserDetails.USER_USER_ALIAS,
                null != user.getUserAlias() ? user.getUserAlias() : null);
        values.put(RoomiesContract.UserDetails.USER_SENDER_ID, null != user.getSenderId() ? user
                .getSenderId() : null);
        Uri resultUri = mContext.getContentResolver().insert(UserDetailsProvider.CONTENT_URI,
                values);
        row = Integer.parseInt(resultUri.getLastPathSegment());

        return row;
    }

    @Override
    public <T> int deleteDetails(Map<ServiceParam, List<T>> detailsMap) {
        return 0;
    }

    @Override
    public int updateDetails(Map<ServiceParam, ?> detailsMap) {
        prepareStatement(detailsMap);
        int rowsUpdated = mContext.getContentResolver().update(UserDetailsProvider.CONTENT_URI,
                values, selection, selectionArgs);
        return rowsUpdated;
    }

    private void prepareStatement(Map<ServiceParam, ?> detailsMap) {

        List<QueryParam> selectionList = (List<QueryParam>) detailsMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) detailsMap.get(ServiceParam.SELECTIONARGS);

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
            if (null != user.getPassword()) {
                values.put(RoomiesContract.UserDetails.USER_PASSWORD, user.getPassword());
            }
            if (null != user.getUserAlias()) {
                values.put(RoomiesContract.UserDetails.USER_USER_ALIAS, user.getUserAlias());
            }
            if (null != user.getSenderId()) {
                values.put(RoomiesContract.UserDetails.USER_SENDER_ID, user.getSenderId());
            }
        }
    }
}
