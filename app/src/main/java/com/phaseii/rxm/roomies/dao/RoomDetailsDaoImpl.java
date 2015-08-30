package com.phaseii.rxm.roomies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomDetails;
import com.phaseii.rxm.roomies.model.RoomiesModel;
import com.phaseii.rxm.roomies.providers.RoomDetailsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class RoomDetailsDaoImpl implements RoomiesDao {

    private Context mContext;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private ContentValues values;
    private RoomDetails roomDetails;

    public RoomDetailsDaoImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {

        prepareStatement(queryMap);
        Cursor cursor = mContext.getContentResolver().query(RoomDetailsProvider
                .CONTENT_URI, projection, null, null, sortOrder);

        List<RoomDetails> roomDetailsList = new ArrayList<>();
        if (null != cursor) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                RoomDetails roomDetail = new RoomDetails();

                if (cursor.getColumnIndex(RoomiesContract.RoomDetails.DETAILS_ROOM_ID) >= 0) {
                    roomDetail.setRoomId(cursor.getInt(
                            cursor.getColumnIndex(RoomiesContract.RoomDetails.DETAILS_ROOM_ID)));
                }
                if (cursor.getColumnIndex(RoomiesContract.RoomDetails.DETAILS_ROOM_ALIAS) >= 0) {
                    roomDetail.setRoomAlias(cursor.getString(
                            cursor.getColumnIndex(RoomiesContract.RoomDetails.DETAILS_ROOM_ALIAS)));
                }
                if (cursor.getColumnIndex(RoomiesContract.RoomDetails.DETAILS_NO_OF_PERSONS) >= 0) {
                    roomDetail.setNoOfPersons(cursor.getInt(
                            cursor.getColumnIndex(RoomiesContract.RoomDetails.DETAILS_ROOM_ALIAS)));
                }

                roomDetailsList.add(roomDetail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return roomDetailsList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {

        int row = -1;
        prepareStatement(detailsMap);

        Uri resultUri = mContext.getContentResolver().insert(RoomDetailsProvider.CONTENT_URI,
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
        return 0;
    }

    private void prepareStatement(Map<ServiceParam, ?> detailsMap) {

        if (null != detailsMap.get(ServiceParam.PROJECTION)) {
            List<QueryParam> projectionList = (List<QueryParam>) detailsMap.get(
                    ServiceParam.PROJECTION);

            List<String> projectionStringList = new ArrayList<>();
            if (null != projectionList && projectionList.size() > 0) {

                for (QueryParam query : projectionList) {
                    projectionStringList.add(query.toString());
                }
                projection = new String[projectionStringList.size()];
                projectionStringList.toArray(projection);
            }
        }


        if (null != detailsMap.get(ServiceParam.SELECTION)) {

            List<QueryParam> selectionList = (List<QueryParam>) detailsMap.get(
                    ServiceParam.SELECTION);
            if (null != selectionList && selectionList.size() > 0) {
                selection = null;
                for (QueryParam select : selectionList) {
                    if (null == selection) {
                        selection = select.toString() + " =?";
                    } else {
                        selection = selection + " AND " + select.toString() + "=?";
                    }
                }
            }
        }
        if (null != detailsMap.get(ServiceParam.SELECTIONARGS)) {

            List<String> selectionArgsList = (List<String>) detailsMap.get(
                    ServiceParam.SELECTIONARGS);
            if (null != selectionArgsList && null != selection && selectionArgsList.size() > 0) {
                selectionArgs = new String[selectionArgsList.size()];
                selectionArgsList.toArray(selectionArgs);
            }
        }

        if (null != detailsMap.get(ServiceParam.MODEL)) {

            values = new ContentValues();
            roomDetails = (RoomDetails) detailsMap.get(ServiceParam.MODEL);

            if (null != roomDetails.getRoomAlias()) {
                values.put(RoomiesContract.RoomDetails.DETAILS_ROOM_ALIAS,
                        roomDetails.getRoomAlias());
            }
            if (roomDetails.getNoOfPersons() > 0) {
                values.put(RoomiesContract.RoomDetails.DETAILS_NO_OF_PERSONS,
                        roomDetails.getNoOfPersons());
            }
        }
    }
}
