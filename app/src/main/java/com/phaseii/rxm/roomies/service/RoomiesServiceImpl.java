package com.phaseii.rxm.roomies.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.providers.RoomExpenseProvider;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import static com.phaseii.rxm.roomies.database.RoomiesContract.Room_Expenses;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;

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

	public RoomiesServiceImpl(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void insertRoomDetails(String rent, String maid, String electricity) {
		ContentValues values = new ContentValues();
		SharedPreferences mSharedPref = mContext.getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		values.put(Room_Expenses.COLUMN_RENT, null != rent ? Float.valueOf(rent) : 0.0);
		values.put(Room_Expenses.COLUMN_ELECTRICITY, null != electricity ? Float.valueOf
				(electricity) : 0.0);
		values.put(Room_Expenses.COLUMN_MAID, null != maid ? Float.valueOf(maid) : 0.0);
		values.put(Room_Expenses.COLUMN_MISCELLANEOUS, 0.0);
		values.put(Room_Expenses.COLUMN_MONTH, currentMonth);
		values.put(Room_Expenses.COLUMN_RENT_MARGIN, Float.valueOf(mSharedPref.getString(RENT,
				"0.0")));
		values.put(Room_Expenses.COLUMN_MAID_MARGIN, Float.valueOf(mSharedPref.getString(RENT,
				"0.0")));
		values.put(Room_Expenses.COLUMN_ELECTRICITY_MARGIN, Float.valueOf(mSharedPref.getString
				(RENT, "0.0")));
		values.put(Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN, Float.valueOf(mSharedPref.getString
				(RENT, "0.0")));
		values.put(Room_Expenses.COLUMN_TOTAL, "0.0");
		mContext.getContentResolver().insert(RoomExpenseProvider.CONTENT_URI, values);
	}

	@Override
	public void updateRoomExpenses(String rent, String maid, String electricity) {
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
			String selection = Room_Expenses.COLUMN_MONTH + "=?";
			String selectionArgs[] = {currentMonth};
			mContext.getContentResolver().update(monthUri, values, selection, selectionArgs);
		}
	}

	@Override
	public Cursor getRoomDetails() {
		String[] projection = {Room_Expenses.COLUMN_MONTH,
				Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_MAID,
				Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_MISCELLANEOUS,
		};
		Cursor cursor = mContext.getContentResolver().query(monthUri, projection, null, null, null);
		return cursor;
	}

	@Override
	public Cursor getRoomDetailsWithMargin() {
		String[] projection = {Room_Expenses.COLUMN_MONTH,
				Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_RENT_MARGIN,
				Room_Expenses.COLUMN_MAID, Room_Expenses.COLUMN_MAID_MARGIN,
				Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_ELECTRICITY_MARGIN,
				Room_Expenses.COLUMN_MISCELLANEOUS, Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN};
		String[] selectionArgs = {currentMonth};
		Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
				Room_Expenses.COLUMN_MONTH + "=?",
				selectionArgs, null);
		return cursor;
	}

	@Override
	public float getTotalSpent() {
		String projection[] = {Room_Expenses.COLUMN_TOTAL};
		String[] selectionArgs = {currentMonth};
		Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
				Room_Expenses.COLUMN_MONTH + "=?",
				selectionArgs, null);
		cursor.moveToFirst();
		float total = cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_TOTAL));
		return total;
	}
}