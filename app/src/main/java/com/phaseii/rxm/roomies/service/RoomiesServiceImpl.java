package com.phaseii.rxm.roomies.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.model.RoomBudget;
import com.phaseii.rxm.roomies.providers.RoomExpenseProvider;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.phaseii.rxm.roomies.database.RoomiesContract.Room_Expenses;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.NAME;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.TOTAL_MARGIN;

/**
 * Created by Snehankur on 3/19/2015.
 */
public class RoomiesServiceImpl implements RoomiesService {

    Context mContext;
    final Uri monthUri = Uri.withAppendedPath(RoomExpenseProvider.CONTENT_URI,
            "month/" + new DateFormatSymbols().getMonths()[Calendar
                    .getInstance().get(Calendar.MONTH)]);
    String currentMonth = new DateFormatSymbols().getMonths()[Calendar
            .getInstance().get(Calendar.MONTH)];
    SharedPreferences mexpenditurePref;
    SharedPreferences.Editor mEditor;

    public RoomiesServiceImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void insertRoomDetails(String rent, String maid, String electricity, String username,
                                  String roomAlias) {
        ContentValues values = new ContentValues();
        SharedPreferences mSharedPref = mContext.getSharedPreferences(
                ROOM_BUDGET_FILE_KEY, Context.MODE_PRIVATE);
        values.put(Room_Expenses.COLUMN_RENT, null != rent ? Float.valueOf(rent) : 0.0);
        values.put(Room_Expenses.COLUMN_ELECTRICITY, null != electricity ? Float.valueOf
                (electricity) : 0.0);
        values.put(Room_Expenses.COLUMN_MAID, null != maid ? Float.valueOf(maid) : 0.0);
        values.put(Room_Expenses.COLUMN_MISCELLANEOUS, 0.0);
        values.put(Room_Expenses.COLUMN_MONTH, currentMonth);
        values.put(Room_Expenses.COLUMN_RENT_MARGIN,
                Float.valueOf(mSharedPref.getString(RENT_MARGIN,
                        "0.0")));
        values.put(Room_Expenses.COLUMN_MAID_MARGIN,
                Float.valueOf(mSharedPref.getString(MAID_MARGIN,
                        "0.0")));
        values.put(Room_Expenses.COLUMN_ELECTRICITY_MARGIN, Float.valueOf(mSharedPref.getString
                (ELECTRICITY_MARGIN, "0.0")));
        values.put(Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN, Float.valueOf(mSharedPref.getString
                (MISC_MARGIN, "0.0")));
        values.put(Room_Expenses.COLUMN_TOTAL, "0.0");
        values.put(Room_Expenses.COLUMN_USERNAME, username);
        values.put(Room_Expenses.COLUMN_ROOM_ALIAS, roomAlias);
        mContext.getContentResolver().insert(RoomExpenseProvider.CONTENT_URI, values);
    }

    @Override
    public void updateRoomExpenses(String rent, String maid, String electricity, String username) {
        mexpenditurePref = mContext.getSharedPreferences(RoomiesConstants
                .ROOM_EXPENDITURE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mexpenditurePref.edit();
        ContentValues values = new ContentValues();
        if (null != rent) {
            mEditor.putString(RENT, rent);
            values.put(Room_Expenses.COLUMN_RENT, Float.valueOf(rent));
        }
        if (null != maid) {
            mEditor.putString(MAID, rent);
            values.put(Room_Expenses.COLUMN_MAID, Float.valueOf(maid));
        }
        if (null != electricity) {
            mEditor.putString(ELECTRICITY, rent);
            values.put(Room_Expenses.COLUMN_ELECTRICITY, Float.valueOf(electricity));
        }
        if (values.size() != 0) {
            String selection = Room_Expenses.COLUMN_MONTH + "=? AND " + Room_Expenses
                    .COLUMN_USERNAME + "=?";
            String selectionArgs[] = {currentMonth, username};
            mContext.getContentResolver().update(monthUri, values, selection, selectionArgs);
        }
    }

    @Override
    public Cursor getRoomDetails() {
        String[] projection = {Room_Expenses.COLUMN_MONTH,
                Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_MAID,
                Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_MISCELLANEOUS,
        };
        String username = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString
                (RoomiesConstants.NAME, null);
        String selection = Room_Expenses.COLUMN_MONTH + " =? AND " + Room_Expenses.COLUMN_USERNAME + " =?";
        String[] selectionArgs = {currentMonth, username};
        Cursor cursor = mContext.getContentResolver().query(monthUri, projection, selection,
                selectionArgs,
                null);
        return cursor;
    }

    @Override
    public Cursor getRoomDetailsWithMargin(String username) {
        String[] projection = {Room_Expenses.COLUMN_MONTH,
                Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_RENT_MARGIN,
                Room_Expenses.COLUMN_MAID, Room_Expenses.COLUMN_MAID_MARGIN,
                Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_ELECTRICITY_MARGIN,
                Room_Expenses.COLUMN_MISCELLANEOUS, Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN,
                Room_Expenses.COLUMN_TOTAL, Room_Expenses.COLUMN_USERNAME, Room_Expenses.COLUMN_ROOM_ALIAS};
        String selection = Room_Expenses.COLUMN_MONTH + "=? AND " + Room_Expenses.COLUMN_USERNAME
                + "=?";
        String[] selectionArgs = {currentMonth, username};
        Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
                selection, selectionArgs, null);
        cacheData(cursor);
        return cursor;
    }

    @Override
    public List<RoomBudget> getAllMonthDetailsWithMargin(String username) {
        Uri allMonthUri = Uri.withAppendedPath(RoomExpenseProvider.CONTENT_URI,
                "all/" + username);
        List<RoomBudget> roomBudgets = new ArrayList<>();
        String[] projection = {Room_Expenses.COLUMN_MONTH,
                Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_RENT_MARGIN,
                Room_Expenses.COLUMN_MAID, Room_Expenses.COLUMN_MAID_MARGIN,
                Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_ELECTRICITY_MARGIN,
                Room_Expenses.COLUMN_MISCELLANEOUS, Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN,
                Room_Expenses.COLUMN_TOTAL, Room_Expenses.COLUMN_USERNAME, Room_Expenses.COLUMN_ROOM_ALIAS};

        Cursor cursor = mContext.getContentResolver().query(allMonthUri, projection,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RoomBudget roomBudget = new RoomBudget();
            roomBudget.setMonth(
                    cursor.getString(cursor.getColumnIndex(Room_Expenses.COLUMN_MONTH)));
            roomBudget.setRent(cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_RENT)));
            roomBudget.setElectricity(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_ELECTRICITY)));
            roomBudget.setMaid(cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_MAID)));
            roomBudget.setMiscellaneous(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_MISCELLANEOUS)));
            roomBudget.setRent_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_RENT_MARGIN)));
            roomBudget.setMaid_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_MAID_MARGIN)));
            roomBudget.setElectricity_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_ELECTRICITY_MARGIN)));
            roomBudget.setMiscellaneous_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_MISCELLANEOUS_MARGIN)));
            roomBudgets.add(roomBudget);
            cursor.moveToNext();
        }
        return roomBudgets;
    }

    @Override
    public float getTotalSpent() {
        float total = 0f;
        String username = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString
                (RoomiesConstants.NAME, null);
        String roomAlias = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(ROOM_ALIAS, null);
        String projection[] = {Room_Expenses.COLUMN_TOTAL};
        String selection = Room_Expenses.COLUMN_MONTH + "=? AND " + Room_Expenses.COLUMN_USERNAME
                + " =?";
        String[] selectionArgs = {currentMonth, username};
        Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
                selection, selectionArgs, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            total = cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_TOTAL));
        } else {
            insertRoomDetails(null, null, null, username, roomAlias);
        }
        return total;
    }

    private void cacheData(Cursor cursor) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(ROOM_BUDGET_FILE_KEY,
                Context.MODE_PRIVATE);
        if (cursor != null) {
            cursor.moveToFirst();
            mEditor = sharedPreferences.edit();
            float rent_margin = cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_RENT_MARGIN));
            float maid_margin = cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_MAID_MARGIN));
            float electricity_margin = cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_ELECTRICITY_MARGIN));
            float misc_margin = cursor.getFloat(cursor.getColumnIndex(Room_Expenses
                    .COLUMN_MISCELLANEOUS_MARGIN));
            float total_margin = rent_margin + maid_margin + electricity_margin + misc_margin;
            float rent = cursor.getFloat(
                    cursor.getColumnIndex(RoomiesContract.Room_Expenses.COLUMN_RENT));
            float maid = cursor.getFloat(cursor.getColumnIndex(RoomiesContract.Room_Expenses
                    .COLUMN_MAID));
            float electricity = cursor.getFloat(cursor.getColumnIndex(RoomiesContract.Room_Expenses
                    .COLUMN_ELECTRICITY));
            float misc = cursor.getFloat(cursor.getColumnIndex(RoomiesContract.Room_Expenses
                    .COLUMN_MISCELLANEOUS));
            mEditor.putString(RENT_MARGIN, String.valueOf(rent_margin));
            mEditor.putString(MAID_MARGIN, String.valueOf(maid_margin));
            mEditor.putString(ELECTRICITY_MARGIN, String.valueOf(electricity_margin));
            mEditor.putString(MISC_MARGIN, String.valueOf(misc_margin));
            mEditor.putString(TOTAL_MARGIN, String.valueOf(total_margin));
            mEditor.putString(RENT, String.valueOf(rent));
            mEditor.putString(MAID, String.valueOf(maid));
            mEditor.putString(ELECTRICITY, String.valueOf(electricity));
            mEditor.putString(MISC, String.valueOf(misc));
            mEditor.apply();
            sharedPreferences = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
                    Context.MODE_PRIVATE);
            mEditor = sharedPreferences.edit();
            mEditor.putString(ROOM_ALIAS, cursor.getString(cursor.getColumnIndex
                    (Room_Expenses.COLUMN_ROOM_ALIAS)));
            mEditor.apply();
        }
    }


    @Override
    public boolean updateRoomMargins(String username, String column, String newVal) {

        int count = 0;
        mEditor = mContext.getSharedPreferences(RoomiesConstants
                .ROOM_BUDGET_FILE_KEY, Context.MODE_PRIVATE).edit();
        ContentValues values = new ContentValues();
        String selection = Room_Expenses.COLUMN_MONTH + "=? AND " + Room_Expenses
                .COLUMN_USERNAME + "=?";
        String selectionArgs[] = {currentMonth, username};
        if ("room_alias".equals(column)) {
            values.put(Room_Expenses.COLUMN_ROOM_ALIAS, newVal);
            count = mContext.getContentResolver().update(monthUri, values, selection,
                    selectionArgs);
            if (count > 0) {
                mEditor = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
                        Context.MODE_PRIVATE).edit();
                mEditor.putString(ROOM_ALIAS, newVal);
                mEditor.apply();
            }
        } else if ("no_of_members".equals(column)) {
            mEditor = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
                    Context.MODE_PRIVATE).edit();
            mEditor.putString(ROOM_NO_OF_MEMBERS, newVal);
            mEditor.apply();

        } else if ("rent".equals(column)) {
            values.put(Room_Expenses.COLUMN_RENT_MARGIN, Float.valueOf(newVal));
            count = mContext.getContentResolver().update(monthUri, values, selection,
                    selectionArgs);
            if (count > 0) {
                mEditor.putString(RENT_MARGIN, newVal);
                mEditor.apply();
            }
        } else if ("maid".equals(column)) {
            values.put(Room_Expenses.COLUMN_MAID_MARGIN, Float.valueOf(newVal));
            count = mContext.getContentResolver().update(monthUri, values, selection,
                    selectionArgs);
            if (count > 0) {
                mEditor.putString(MAID_MARGIN, newVal);
                mEditor.apply();
            }

        } else if ("electricity".equals(column)) {
            values.put(Room_Expenses.COLUMN_ELECTRICITY_MARGIN, Float.valueOf(newVal));
            count = mContext.getContentResolver().update(monthUri, values, selection,
                    selectionArgs);
            if (count > 0) {
                mEditor.putString(ELECTRICITY_MARGIN, newVal);
                mEditor.apply();
            }
        } else if ("misc".equals(column)) {
            values.put(Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN, Float.valueOf(newVal));
            count = mContext.getContentResolver().update(monthUri, values, selection,
                    selectionArgs);
            if (count > 0) {
                mEditor.putString(MISC_MARGIN, newVal);
                mEditor.apply();
            }
        } else if ("name".equals(column)) {
            values.put(Room_Expenses.COLUMN_USERNAME, newVal);
            count = mContext.getContentResolver().update(monthUri, values, selection,
                    selectionArgs);
        }

        return count > 0 ? true : false;
    }
}