package com.phaseii.rxm.roomies.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.EditText;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.providers.MiscellaneousProvider;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Snehankur on 4/12/2015.
 */
public class MiscServiceImpl implements MiscService {

    Context mContext;

    public MiscServiceImpl(Context mContext) {
        this.mContext = mContext;
    }

    final Uri monthUri = Uri.withAppendedPath(MiscellaneousProvider.CONTENT_URI,
            "month/" + new DateFormatSymbols().getMonths()[Calendar
                    .getInstance().get(Calendar.MONTH)]);
    String currentMonth = new DateFormatSymbols().getMonths()[Calendar
            .getInstance().get(Calendar.MONTH)];

    @Override
    public void insertMiscExpenses(EditText description, EditText quantity, EditText amount,
                                   String type, String username) {
        ContentValues values = new ContentValues();
        values.put(RoomiesContract.Misc_Expenses.COLUMN_DESCRIPTION,
                description.getText().toString());
        if (type.equals(RoomiesConstants.BILLS)) {
            values.put(RoomiesContract.Misc_Expenses.COLUMN_QUANTITY,
                    Float.valueOf("0.0"));
        } else {
            values.put(RoomiesContract.Misc_Expenses.COLUMN_QUANTITY,
                    Float.valueOf(quantity.getText().toString()));
        }
        values.put(RoomiesContract.Misc_Expenses.COLUMN_AMOUNT,
                Float.valueOf(amount.getText().toString()));
        values.put(RoomiesContract.Misc_Expenses.COLUMN_TYPE, type);
        values.put(RoomiesContract.Misc_Expenses.COLUMN_USERNAME, username);
        values.put(RoomiesContract.Misc_Expenses.COLUMN_MONTH, currentMonth);
        values.put(RoomiesContract.Misc_Expenses.COLUMN_DATE, new Date().getTime());
        mContext.getContentResolver().insert(monthUri, values);
    }

    @Override
    public List<MiscExpense> getCurrentTotalMiscExpense() {
        List<MiscExpense> miscExpenses = new ArrayList<>();
        final Uri monthTotalUri = Uri.withAppendedPath(MiscellaneousProvider.CONTENT_URI,
                "month/" + new DateFormatSymbols().getMonths()[Calendar
                        .getInstance().get(Calendar.MONTH)]);
        String[] projection = {RoomiesContract.Misc_Expenses.COLUMN_TYPE,
                RoomiesContract.Misc_Expenses.COLUMN_DESCRIPTION,
                RoomiesContract.Misc_Expenses.COLUMN_QUANTITY,
                RoomiesContract.Misc_Expenses.COLUMN_AMOUNT,
                RoomiesContract.Misc_Expenses.COLUMN_DATE,
                RoomiesContract.Misc_Expenses.COLUMN_MONTH};
        String selection = RoomiesContract.Misc_Expenses.COLUMN_MONTH + "=? AND " + RoomiesContract
                .Misc_Expenses.COLUMN_USERNAME + "=?";
        String username = mContext.getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);
        String[] selectionArgs = new String[]{currentMonth, username};
        Cursor cursor = mContext.getContentResolver().query(monthTotalUri, projection, selection,
                selectionArgs,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MiscExpense miscExpense = new MiscExpense();
            miscExpense.setType(cursor.getString(cursor.getColumnIndex(
                    RoomiesContract.Misc_Expenses.COLUMN_TYPE)));
            miscExpense.setDescription(cursor.getString(cursor.getColumnIndex(
                    RoomiesContract.Misc_Expenses.COLUMN_DESCRIPTION)));
            miscExpense.setQuantity(cursor.getFloat(cursor.getColumnIndex(
                    RoomiesContract.Misc_Expenses.COLUMN_QUANTITY)));
            miscExpense.setAmount(cursor.getFloat(cursor.getColumnIndex(
                    RoomiesContract.Misc_Expenses.COLUMN_AMOUNT)));
            miscExpense.setTransactionDate(new Date(cursor.getLong(cursor.getColumnIndex(
                    RoomiesContract.Misc_Expenses.COLUMN_DATE))));
            miscExpense.setMonth(cursor.getString(
                    cursor.getColumnIndex(RoomiesContract.Misc_Expenses.COLUMN_MONTH)));
            miscExpenses.add(miscExpense);
            cursor.moveToNext();
        }
        return miscExpenses;
    }

    @Override
    public List<String> getMiscMonths(String username) {
        List<String> months = new ArrayList<>();
        Uri allMonthsUri = Uri.withAppendedPath(MiscellaneousProvider.CONTENT_URI,
                "all/" + username);
        String projection[] = {RoomiesContract.Misc_Expenses.COLUMN_MONTH};
        Cursor cursor = mContext.getContentResolver().query(allMonthsUri, projection, null, null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            months.add(cursor.getString(cursor.getColumnIndex(RoomiesContract.Misc_Expenses
                    .COLUMN_MONTH)));
            cursor.moveToNext();
        }
        return months;
    }

    @Override
    public boolean updateUser(String username, String newVal) {
        ContentValues values = new ContentValues();
        values.put(RoomiesContract.Misc_Expenses.COLUMN_USERNAME, newVal);
        Uri updateUserUri = Uri.withAppendedPath(MiscellaneousProvider.CONTENT_URI, "miscexpense");
        String selection = RoomiesContract.Misc_Expenses.COLUMN_USERNAME + "=?";
        String selectionArgs[] = {username};
        int count = mContext.getContentResolver().update(MiscellaneousProvider.CONTENT_URI, values, selection, selectionArgs);
        return true;
    }

}
