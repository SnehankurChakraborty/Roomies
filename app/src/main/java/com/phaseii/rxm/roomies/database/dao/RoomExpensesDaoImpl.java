package com.phaseii.rxm.roomies.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.contract.RoomiesContract;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomiesModel;
import com.phaseii.rxm.roomies.database.provider.RoomExpensesProvider;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_AMOUNT;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_CATEGORY;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_DATE;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_DESCRIPTION;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_MONTH_YEAR;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_QUANTITY;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_ROOM_ID;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_SUBCATEGORY;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses.EXPENSE_USER_ID;

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
        Cursor cursor = null;
        List<RoomExpenses> roomExpensesList = new ArrayList<>();
        try {
            cursor = mContext.getContentResolver().query(expenseUri, projection, selection,
                    selectionArgs, sortOrder);

            if (null != cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    RoomExpenses roomExpenses = new RoomExpenses();

                    roomExpenses.setRoomId(cursor.getColumnIndex(EXPENSE_ROOM_ID) >= 0 ? cursor
                            .getInt(cursor.getColumnIndex(EXPENSE_ROOM_ID)) : -1);
                    roomExpenses.setUserId(cursor.getColumnIndex(EXPENSE_USER_ID) >= 0 ? cursor
                            .getInt(cursor.getColumnIndex(EXPENSE_USER_ID)) : -1);
                    roomExpenses.setExpenseDate(cursor.getColumnIndex(EXPENSE_DATE) >= 0 ?
                            DateUtils.stringToDateParser(
                                    cursor.getString(cursor.getColumnIndex(EXPENSE_DATE))) : null);
                    roomExpenses.setExpenseCategory(cursor.getColumnIndex(EXPENSE_CATEGORY) >= 0 ?
                            cursor.getString(cursor.getColumnIndex(EXPENSE_CATEGORY)) : null);
                    roomExpenses.setExpenseSubcategory(cursor.getColumnIndex(EXPENSE_SUBCATEGORY)
                            >= 0 ?
                            cursor.getString(cursor.getColumnIndex(EXPENSE_SUBCATEGORY)) : null);
                    roomExpenses.setAmount(cursor.getColumnIndex(EXPENSE_AMOUNT) >= 0 ? cursor
                            .getInt(cursor.getColumnIndex(EXPENSE_AMOUNT)) : -1);
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
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return roomExpensesList;
    }

    @Override
    public int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap) {

        int row = -1;
        prepareStatement(detailsMap);

        Uri resultUri = mContext.getContentResolver().insert(RoomExpensesProvider
                        .CONTENT_URI,
                values);
        row = Integer.parseInt(resultUri.getLastPathSegment());
        return row;
    }

    @Override
    public int deleteDetails(Map<ServiceParam, ?> detailsMap) {

        prepareStatement(detailsMap);
        return mContext.getContentResolver().delete(RoomExpensesProvider.CONTENT_URI,
                selection, selectionArgs);
    }

    @Override
    public int updateDetails(Map<ServiceParam, ?> detailsMap) {
        return 0;
    }

    public void prepareStatement(Map<ServiceParam, ?> detailsMap) {

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
        if (null != detailsMap.get(ServiceParam.SELECTION_ARGS)) {

            List<String> selectionArgsList = (List<String>) detailsMap.get(
                    ServiceParam.SELECTION_ARGS);
            if (null != selectionArgsList && null != selection && selectionArgsList.size() >
                    0) {
                selectionArgs = new String[selectionArgsList.size()];
                selectionArgsList.toArray(selectionArgs);
            }
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
            expenseUri = Uri.withAppendedPath(RoomExpensesProvider.CONTENT_URI,
                    queryArg);
        } else {
            expenseUri = RoomExpensesProvider.CONTENT_URI;
        }

        if (null != detailsMap.get(ServiceParam.MODEL)) {

            values = new ContentValues();
            roomExpenses = (RoomExpenses) detailsMap.get(ServiceParam.MODEL);

            if (roomExpenses.getRoomId() >= 0) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_ROOM_ID, roomExpenses
                        .getRoomId());
            }
            if (roomExpenses.getUserId() >= 0) {
                values.put(RoomiesContract.RoomExpenses.EXPENSE_USER_ID, roomExpenses
                        .getUserId());
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
                        DateUtils.dateToStringFormatter(roomExpenses.getExpenseDate()));
            }
        }
    }

}
