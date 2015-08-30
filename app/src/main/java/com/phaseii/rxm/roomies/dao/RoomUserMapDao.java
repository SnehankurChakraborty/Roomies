package com.phaseii.rxm.roomies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomUserMap;
import com.phaseii.rxm.roomies.model.RoomiesModel;
import com.phaseii.rxm.roomies.providers.RoomUserMapProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomUserMap.ROOM_USER_USER_ID;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserMapDao implements RoomiesDao {

    private Context mContext;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private RoomUserMap roomUserMap;
    private ContentValues values;

    public RoomUserMapDao(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {
        prepareStatement(queryMap);

        Cursor cursor = mContext.getContentResolver().query(RoomUserMapProvider
                .CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        List<RoomUserMap> roomUserMapList = null;
        if (null != cursor) {
            cursor.moveToFirst();
            roomUserMapList = new ArrayList<>();
            while (!cursor.isAfterLast()) {

                RoomUserMap roomUserMap = new RoomUserMap();

                roomUserMap.setRoomId(cursor.getColumnIndex(_ID) >= 0 ? cursor.getInt(cursor
                        .getColumnIndex(ROOM_USER_ROOM_ID)) : -1);
                roomUserMap.setUserId(cursor.getColumnIndex(_ID) >= 0 ? cursor.getInt(cursor
                        .getColumnIndex(ROOM_USER_USER_ID)) : -1);
                roomUserMapList.add(roomUserMap);

                cursor.moveToNext();
            }
            cursor.close();
        }


        return roomUserMapList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {
        roomUserMap = (RoomUserMap) detailsMap.get(ServiceParam.MODEL);
        int row = 0;
        prepareStatement(detailsMap);

        Uri resultUri = mContext.getContentResolver().insert(RoomUserMapProvider.CONTENT_URI,
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
        int rowsUpdated = mContext.getContentResolver().update(RoomUserMapProvider.CONTENT_URI,
                values, selection, selectionArgs);
        return rowsUpdated;
    }

    private void prepareStatement(Map<ServiceParam, ?> detailsMap) {

        List<QueryParam> selectionList = (List<QueryParam>) detailsMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) detailsMap.get(ServiceParam.SELECTIONARGS);
        List<QueryParam> projectionList = (List<QueryParam>) detailsMap.get(
                ServiceParam.PROJECTION);
        sortOrder = (String) detailsMap.get(ServiceParam.SORTORDER);

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
