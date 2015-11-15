package com.phaseii.rxm.roomies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.model.RoomiesModel;
import com.phaseii.rxm.roomies.providers.RoomStatsProvider;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.ServiceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MAID_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MAID_SPENT;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_MONTH_YEAR;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_RENT_MARGIN;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_RENT_SPENT;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats.STATS_ROOM_ID;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.listToProjection;

/**
 * Created by Snehankur on 7/1/2015.
 */
public class RoomStatsDaoImpl implements RoomiesDao {
    Context mContext;
    private Uri statUri;

    public RoomStatsDaoImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {
        List<QueryParam> projectionList = (List<QueryParam>) queryMap.get(ServiceParam.PROJECTION);
        List<QueryParam> selectionList = (List<QueryParam>) queryMap.get(ServiceParam.SELECTION);
        List<String> selectionArgsList = (List<String>) queryMap.get(ServiceParam.SELECTIONARGS);
        String sortOrder = (String) queryMap.get(ServiceParam.SORTORDER);

        String[] projection = listToProjection(projectionList);
        String selection = null;
        String selectionArgs[] = null;
        if (null != selectionList && selectionList.size() > 0) {

            for (QueryParam select : selectionList) {
                if (null == selection) {
                    selection = select.toString() + " =?";
                } else {
                    selection = selection + " AND " + select.toString() + " =?";
                }
            }
        }
        if (null != selectionArgsList && null != selection && selectionArgsList.size() > 0) {
            selectionArgs = new String[selectionArgsList.size()];
            selectionArgsList.toArray(selectionArgs);
        }

        String queryArg = null;
        Map<QueryParam, String> queryParams = (Map<QueryParam, String>) queryMap.get
                (ServiceParam.QUERYARGS);
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
            statUri = Uri.withAppendedPath(RoomStatsProvider.CONTENT_URI, queryArg);
        } else {
            statUri = RoomStatsProvider.CONTENT_URI;
        }

        List<RoomStats> roomStatsList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(statUri, projection, selection,
                    selectionArgs, sortOrder);

            if (null != cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    RoomStats roomStats = new RoomStats();

                    roomStats.setRoomId(cursor.getColumnIndex(STATS_ROOM_ID) >= 0 ? cursor.getInt
                            (cursor.getColumnIndex(STATS_ROOM_ID)) : -1);
                    roomStats.setMonthYear(
                            cursor.getColumnIndex(STATS_MONTH_YEAR) >= 0 ? cursor.getString
                                    (cursor.getColumnIndex(STATS_MONTH_YEAR)) : null);
                    roomStats.setRentMargin(cursor.getColumnIndex(STATS_RENT_MARGIN) >= 0 ?
                            cursor.getLong(cursor.getColumnIndex(STATS_RENT_MARGIN)) : -1);
                    roomStats.setMaidMargin(cursor.getColumnIndex(STATS_MAID_MARGIN) >= 0 ?
                            cursor.getLong(cursor.getColumnIndex(STATS_MAID_MARGIN)) : -1);
                    roomStats.setElectricityMargin(
                            cursor.getColumnIndex(STATS_ELECTRICITY_MARGIN) >= 0 ?
                                    cursor.getLong(
                                            cursor.getColumnIndex(STATS_ELECTRICITY_MARGIN)) : -1);
                    roomStats.setMiscellaneousMargin(
                            cursor.getColumnIndex(STATS_MISCELLANEOUS_MARGIN) >= 0 ?
                                    cursor.getLong(
                                            cursor.getColumnIndex(STATS_MISCELLANEOUS_MARGIN)) :
                                    -1);
                    roomStats.setRentSpent(cursor.getColumnIndex(STATS_RENT_SPENT) >= 0 ?
                            cursor.getLong(cursor.getColumnIndex(STATS_RENT_SPENT)) : -1);
                    roomStats.setMaidSpent(cursor.getColumnIndex(STATS_MAID_SPENT) >= 0 ?
                            cursor.getLong(cursor.getColumnIndex(STATS_MAID_SPENT)) : -1);
                    roomStats.setElectricitySpent(
                            cursor.getColumnIndex(STATS_ELECTRICITY_SPENT) >= 0 ?
                                    cursor.getLong(
                                            cursor.getColumnIndex(STATS_ELECTRICITY_SPENT)) : -1);
                    roomStats.setMiscellaneousSpent(
                            cursor.getColumnIndex(STATS_MISCELLANEOUS_SPENT) >= 0 ?
                                    cursor.getLong(
                                            cursor.getColumnIndex(STATS_MISCELLANEOUS_SPENT)) : -1);

                    roomStatsList.add(roomStats);

                    cursor.moveToNext();
                }
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return roomStatsList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {

        int row = 0;

        RoomStats roomStat = (RoomStats) detailsMap.get(ServiceParam.MODEL);

        ContentValues values = new ContentValues();
        values.put(RoomiesContract.RoomStats.STATS_ROOM_ID, roomStat.getRoomId());
        values.put(RoomiesContract.RoomStats.STATS_MONTH_YEAR, roomStat.getMonthYear());
        values.put(RoomiesContract.RoomStats.STATS_RENT_MARGIN, roomStat.getRentMargin());
        values.put(RoomiesContract.RoomStats.STATS_MAID_MARGIN, roomStat.getMaidMargin());
        values.put(RoomiesContract.RoomStats.STATS_ELECTRICITY_MARGIN,
                roomStat.getElectricityMargin());
        values.put(RoomiesContract.RoomStats.STATS_MISCELLANEOUS_MARGIN,
                roomStat.getMiscellaneousMargin());
        values.put(RoomiesContract.RoomStats.STATS_RENT_SPENT, roomStat.getRentSpent());
        values.put(RoomiesContract.RoomStats.STATS_MAID_SPENT, roomStat.getMaidSpent());
        values.put(RoomiesContract.RoomStats.STATS_ELECTRICITY_SPENT,
                roomStat.getElectricitySpent());
        values.put(RoomiesContract.RoomStats.STATS_MISCELLANEOUS_SPENT,
                roomStat.getMiscellaneousSpent());

        Uri resultUri = mContext.getContentResolver().insert(RoomStatsProvider.CONTENT_URI,
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


}
