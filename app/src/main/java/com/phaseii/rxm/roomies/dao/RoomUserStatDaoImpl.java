package com.phaseii.rxm.roomies.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.phaseii.rxm.roomies.database.RoomiesDbHelper;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomUserStatData;
import com.phaseii.rxm.roomies.model.RoomiesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomDetails.DETAILS_NO_OF_PERSONS;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomDetails.DETAILS_ROOM_ALIAS;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomDetails.DETAILS_ROOM_ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MAID_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MONTH_YEAR;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_RENT_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_TOTAL;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_SENDER_ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_USERNAME;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails.USER_USER_ALIAS;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails._ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.VIEW_NAME;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.listToProjection;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserStatDaoImpl implements RoomiesDao {

    private SQLiteOpenHelper mdbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    public RoomUserStatDaoImpl(Context mContext) {
        this.mContext = mContext;
        this.mdbHelper = new RoomiesDbHelper(this.mContext);
        this.db = mdbHelper.getReadableDatabase();
    }

    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {

        List<QueryParam> projectionList = (List<QueryParam>) queryMap.get(ServiceParam.PROJECTION);
        List<QueryParam> selectionList = (List<QueryParam>) queryMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) queryMap.get(ServiceParam.SELECTIONARGS);
        String sortOrder = (String) queryMap.get(ServiceParam.SORTORDER);
        List<RoomUserStatData> roomUserStatDataList = new ArrayList<>();

        String[] selectionArgs = null;
        String[] projection = listToProjection(projectionList);
        String selection = null;

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

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(VIEW_NAME);
        qb.appendWhere(STATS_MONTH_YEAR + "=" + RoomiesHelper.getCurrentMonthYear());
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder, null);

        if (null != cursor) {
            cursor.moveToFirst();
            while (cursor.isAfterLast()) {
                RoomUserStatData roomUserStatData = new RoomUserStatData();

                /**
                 * User Details
                 */
                roomUserStatData.setUserId(cursor.getColumnIndex(_ID) >= 0 ? cursor.getInt(cursor
                        .getColumnIndex(_ID)) : -1);
                roomUserStatData.setUsername(
                        cursor.getColumnIndex(USER_USERNAME) >= 0 ? cursor.getString(cursor
                                .getColumnIndex(USER_USERNAME)) : null);
                roomUserStatData.setUserAlias(
                        cursor.getColumnIndex(USER_USER_ALIAS) >= 0 ? cursor.getString(cursor
                                .getColumnIndex(USER_USER_ALIAS)) : null);
                roomUserStatData.setSenderId(cursor.getColumnIndex(USER_SENDER_ID) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(USER_SENDER_ID)) : null);

                /**
                 * Room Details
                 */
                roomUserStatData.setRoomId(cursor.getColumnIndex(DETAILS_ROOM_ID) >= 0 ?
                        cursor.getInt(cursor.getColumnIndex(DETAILS_ROOM_ID)) : -1);
                roomUserStatData.setRoomAlias(cursor.getColumnIndex(DETAILS_ROOM_ALIAS) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(DETAILS_ROOM_ALIAS)) : null);
                roomUserStatData.setNoOfMembers(cursor.getColumnIndex(DETAILS_NO_OF_PERSONS) >= 0 ?
                        cursor.getInt(cursor.getColumnIndex(DETAILS_ROOM_ALIAS)) : -1);

                /**
                 * Room Stats
                 */
                roomUserStatData.setMonthYear(
                        cursor.getColumnIndex(STATS_MONTH_YEAR) >= 0 ? cursor.getString
                                (cursor.getColumnIndex(STATS_MONTH_YEAR)) : null);
                roomUserStatData.setRentMargin(cursor.getColumnIndex(STATS_RENT_MARGIN) >= 0 ?
                        cursor.getLong(cursor.getColumnIndex(STATS_RENT_MARGIN)) : -1);
                roomUserStatData.setMaidMargin(cursor.getColumnIndex(STATS_MAID_MARGIN) >= 0 ?
                        cursor.getLong(cursor.getColumnIndex(STATS_MAID_MARGIN)) : -1);
                roomUserStatData.setElectricityMargin(
                        cursor.getColumnIndex(STATS_ELECTRICITY_MARGIN) >= 0 ?
                                cursor.getLong(
                                        cursor.getColumnIndex(STATS_ELECTRICITY_MARGIN)) : -1);
                roomUserStatData.setMiscellaneousMargin(
                        cursor.getColumnIndex(STATS_MISCELLANEOUS_MARGIN) >= 0 ?
                                cursor.getLong(
                                        cursor.getColumnIndex(STATS_MISCELLANEOUS_MARGIN)) : -1);
                roomUserStatData.setTotal(
                        cursor.getColumnIndex(STATS_TOTAL) >= 0 ?
                                cursor.getLong(cursor.getColumnIndex(STATS_TOTAL)) : -1);

                roomUserStatDataList.add(roomUserStatData);
            }
        }

        return roomUserStatDataList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {
        throw new RoomiesStateException("Insertion not allowed");
    }

    @Override
    public <T> int deleteDetails(Map<ServiceParam, List<T>> detailsMap) {
        throw new RoomiesStateException("Deletion not allowed");
    }

    @Override
    public int updateDetails(Map<ServiceParam, ?> detailsMap) {
        throw new RoomiesStateException("Update not allowed");
    }
}
