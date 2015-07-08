package com.phaseii.rxm.roomies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomiesModel;
import com.phaseii.rxm.roomies.providers.RoomExpensesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_AMOUNT;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_CATEGORY;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_DATE;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_DESCRIPTION;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_MONTH_YEAR;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_QUANTITY;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_ROOM_ID;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_SUBCATEGORY;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses.EXPENSE_USER_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.dateToStringFormatter;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.getCurrentMonthYear;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.stringToDateParser;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class RoomExpensesDaoImpl implements RoomiesDao {

    private Context mContext;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private ContentValues values;
    private Uri expenseUri;
    private RoomExpenses roomExpenses;

    public RoomExpensesDaoImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap) {

        prepareStatement(queryMap);

        Cursor cursor = mContext.getContentResolver().query(expenseUri, projection, selection,
                selectionArgs, sortOrder);
        List<RoomExpenses> roomExpensesList = null;
        if (null != cursor) {
            cursor.moveToFirst();
            roomExpensesList = new ArrayList<>();
            while (!cursor.isAfterLast()) {

                RoomExpenses roomExpenses = new RoomExpenses();

                roomExpenses.setRoomId(cursor.getColumnIndex(EXPENSE_ROOM_ID) >= 0 ? cursor.getInt
                        (cursor.getColumnIndex(EXPENSE_ROOM_ID)) : -1);
                roomExpenses.setUserId(cursor.getColumnIndex(EXPENSE_USER_ID) >= 0 ? cursor.getInt
                        (cursor.getColumnIndex(EXPENSE_USER_ID)) : -1);
                roomExpenses.setExpenseDate(cursor.getColumnIndex(EXPENSE_DATE) >= 0 ?
                        stringToDateParser(
                                cursor.getString(cursor.getColumnIndex(EXPENSE_DATE))) : null);
                roomExpenses.setExpenseCategory(cursor.getColumnIndex(EXPENSE_CATEGORY) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(EXPENSE_CATEGORY)) : null);
                roomExpenses.setExpenseSubcategory(cursor.getColumnIndex(EXPENSE_SUBCATEGORY) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(EXPENSE_SUBCATEGORY)) : null);
                roomExpenses.setAmount(cursor.getColumnIndex(EXPENSE_AMOUNT) >= 0 ? cursor.getLong
                        (cursor.getColumnIndex(EXPENSE_AMOUNT)) : -1);
                roomExpenses.setQuantity(
                        cursor.getColumnIndex(EXPENSE_QUANTITY) >= 0 ? cursor.getString
                                (cursor.getColumnIndex(EXPENSE_QUANTITY)) : null);
                roomExpenses.setDescription(cursor.getColumnIndex(EXPENSE_DESCRIPTION) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(EXPENSE_DESCRIPTION)) : null);
                roomExpenses.setMonthYear(cursor.getColumnIndex(EXPENSE_MONTH_YEAR) >= 0 ?
                        cursor.getString(cursor.getColumnIndex(EXPENSE_MONTH_YEAR)) : null);
                roomExpensesList.add(roomExpenses);

                cursor.moveToNext();
            }
        }

        return roomExpensesList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {

        int row = -1;
        prepareStatement(detailsMap);

        Uri resultUri = mContext.getContentResolver().insert(RoomExpensesProvider.CONTENT_URI,
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
                        selection = select.toString() + "=?";
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
        String queryArg = null;
        QueryParam queryParam = (QueryParam) detailsMap.get(ServiceParam.QUERYARGS);
        if (null != queryParam) {
            if (queryParam.equals(QueryParam.MONTH_YEAR)) {
                queryArg = "month/" + getCurrentMonthYear();
            } else if (queryParam.equals(QueryParam.ROOMID)) {
                queryArg = "room/" + mContext.getSharedPreferences(PREF_ROOMIES_KEY,
                        Context.MODE_PRIVATE).getString
                        (PREF_ROOM_ID, null);
            }
        }

        if (null != queryArg) {
            expenseUri = Uri.withAppendedPath(RoomExpensesProvider.CONTENT_URI,
                    queryArg);
        } else {
            expenseUri = RoomExpensesProvider.CONTENT_URI;
        }

        if (null != detailsMap.get(ServiceParam.MODEL)) {

            values = new ContentValues();
            roomExpenses = (RoomExpenses) detailsMap.get(ServiceParam.MODEL);

            if (roomExpenses.getRoomId() >= 0) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_ROOM_ID, roomExpenses.getRoomId());
            }
            if (roomExpenses.getUserId() >= 0) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_USER_ID, roomExpenses.getUserId());
            }
            if (null != roomExpenses.getMonthYear()) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_MONTH_YEAR,
                        roomExpenses.getMonthYear());
            }
            if (null != roomExpenses.getExpenseCategory()) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_CATEGORY,
                        roomExpenses.getExpenseCategory());
            }
            if (null != roomExpenses.getExpenseSubcategory()) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_SUBCATEGORY,
                        roomExpenses.getExpenseSubcategory());
            }
            if (null != roomExpenses.getDescription()) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_DESCRIPTION,
                        roomExpenses.getDescription());
            }
            if (roomExpenses.getAmount() >= 0) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_AMOUNT,
                        roomExpenses.getAmount());
            }
            if (null != roomExpenses.getQuantity()) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_QUANTITY,
                        roomExpenses.getQuantity());
            }
            if (null != roomExpenses.getExpenseDate()) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_DATE,
                        dateToStringFormatter(roomExpenses.getExpenseDate()));
            }
        }
    }

}
