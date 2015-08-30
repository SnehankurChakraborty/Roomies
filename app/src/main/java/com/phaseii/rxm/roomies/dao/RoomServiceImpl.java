package com.phaseii.rxm.roomies.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.model.RoomBudget;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.phaseii.rxm.roomies.database.RoomiesContract.Room_Expenses;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.MAID_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.MISC;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.MISC_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.RENT_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_INFO_FILE_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.TOTAL_MARGIN;

/**
 * Created by Snehankur on 3/19/2015.
 */
public class RoomServiceImpl {

	final Uri monthUri = null;
	Context mContext;
	String currentMonth = new DateFormatSymbols().getMonths()[Calendar
			.getInstance().get(Calendar.MONTH)];
	SharedPreferences mexpenditurePref;
	SharedPreferences.Editor mEditor;

	public RoomServiceImpl(Context mContext) {
		this.mContext = mContext;
	}


	public void insertRoomDetails(String rent, String maid, String electricity, String username,
	                              String roomAlias, int noOfMembers) {
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
		values.put(Room_Expenses.COLUMN_NO_OF_MEMBERS, noOfMembers);
		mContext.getContentResolver().insert(null, values);
	}


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


	public Cursor getRoomDetails(String username) {
		String[] projection = {Room_Expenses.COLUMN_MONTH,
				Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_MAID,
				Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_MISCELLANEOUS,
		};
		String selection = Room_Expenses.COLUMN_MONTH + " =? AND " + Room_Expenses.COLUMN_USERNAME + " =?";
		String[] selectionArgs = {currentMonth, username};
		Cursor cursor = mContext.getContentResolver().query(monthUri, projection, selection,
				selectionArgs, null);
		return cursor;
	}


	public boolean getRoomDetailsWithMargin(String username) {
		String[] projection = {Room_Expenses.COLUMN_MONTH,
				Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_RENT_MARGIN,
				Room_Expenses.COLUMN_MAID, Room_Expenses.COLUMN_MAID_MARGIN,
				Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_ELECTRICITY_MARGIN,
				Room_Expenses.COLUMN_MISCELLANEOUS, Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN,
				Room_Expenses.COLUMN_TOTAL, Room_Expenses.COLUMN_USERNAME,
				Room_Expenses.COLUMN_ROOM_ALIAS, Room_Expenses.COLUMN_NO_OF_MEMBERS};
		String selection = Room_Expenses.COLUMN_MONTH + "=? AND " + Room_Expenses.COLUMN_USERNAME
				+ "=?";
		String[] selectionArgs = {currentMonth, username};
		Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
				selection, selectionArgs, null);
		cacheData(cursor);
		return cursor.getCount() > 0 ? true : false;
	}


	public List<RoomBudget> getAllMonthDetailsWithMargin(String username) {
		Uri allMonthUri = null;
		String[] projection = {Room_Expenses.COLUMN_MONTH,
				Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_RENT_MARGIN,
				Room_Expenses.COLUMN_MAID, Room_Expenses.COLUMN_MAID_MARGIN,
				Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_ELECTRICITY_MARGIN,
				Room_Expenses.COLUMN_MISCELLANEOUS, Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN,
				Room_Expenses.COLUMN_TOTAL, Room_Expenses.COLUMN_USERNAME, Room_Expenses.COLUMN_ROOM_ALIAS};

		Cursor cursor = mContext.getContentResolver().query(allMonthUri, projection,
				null, null, null);
		cursor.moveToFirst();
		ArrayList<RoomBudget> roomBudgets = new ArrayList<>();
		RoomBudget room = null;
		while (!cursor.isAfterLast()) {
			room = new RoomBudget();
			room.setMonth(
					cursor.getString(cursor.getColumnIndex(Room_Expenses.COLUMN_MONTH)));
			room.setRent(cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_RENT)));
			room.setElectricity(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
					.COLUMN_ELECTRICITY)));
			room.setMaid(cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_MAID)));
			room.setMiscellaneous(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
					.COLUMN_MISCELLANEOUS)));
			room.setRent_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
					.COLUMN_RENT_MARGIN)));
			room.setMaid_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
					.COLUMN_MAID_MARGIN)));
			room.setElectricity_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
					.COLUMN_ELECTRICITY_MARGIN)));
			room.setMiscellaneous_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
					.COLUMN_MISCELLANEOUS_MARGIN)));
			roomBudgets.add(room);
			cursor.moveToNext();
		}
		return roomBudgets;
	}


	public float getTotalSpent(String username) {
		float total = 0f;
		String roomAlias = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
				Context.MODE_PRIVATE).getString(ROOM_ALIAS, null);
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
			int noOfMembers=Integer.valueOf(mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
					Context.MODE_PRIVATE).getString(ROOM_NO_OF_MEMBERS, "0"));
			SharedPreferences mSharedPreferences = mContext.getSharedPreferences
					(RoomiesConstants.ROOM_BUDGET_FILE_KEY, Context.MODE_PRIVATE);
			mSharedPreferences.edit().clear();
			insertRoomDetails(null, null, null, username, roomAlias, noOfMembers);
		}
		return total;
	}

	private void cacheData(Cursor cursor) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(ROOM_BUDGET_FILE_KEY,
				Context.MODE_PRIVATE);
		if (cursor.getCount() > 0) {
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
			int noOfMembers=cursor.getInt(cursor.getColumnIndex(Room_Expenses
					.COLUMN_NO_OF_MEMBERS));
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
			mEditor.putString(RoomiesConstants.ROOM_NO_OF_MEMBERS, String.valueOf(noOfMembers));
			mEditor.apply();
		}
	}


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
			values.put(Room_Expenses.COLUMN_ROOM_ALIAS, newVal);
			count = mContext.getContentResolver().update(monthUri, values, selection,
					selectionArgs);
			if (count > 0) {
				mEditor = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
						Context.MODE_PRIVATE).edit();
				mEditor.putString(ROOM_NO_OF_MEMBERS, newVal);
				mEditor.apply();
			}
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


	public boolean isUserSaved(String username) {
		boolean isUserSaved = false;
		String[] projection = {Room_Expenses.COLUMN_MONTH};
		String selection = Room_Expenses.COLUMN_USERNAME + " =?";
		String[] selectionArgs = {username};
		Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
				selection, selectionArgs, null);
		List<String> months = new ArrayList<>();
		if (cursor.getCount() > 0) {
			isUserSaved = true;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				months.add(cursor.getString(cursor.getColumnIndex(Room_Expenses.COLUMN_MONTH)));
				cursor.moveToNext();
			}
			SharedPreferences.Editor mEditor = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
					Context.MODE_PRIVATE).edit();
			mEditor.putString(RoomiesConstants.PREVIOUS_MONTH, months.get(months.size() - 1));
			mEditor.apply();
		}
		return isUserSaved;
	}


	public void getSpecificMonthDetails(String username, String month) {
		currentMonth = month;
		getRoomDetailsWithMargin(username);
		currentMonth = new DateFormatSymbols().getMonths()[Calendar
				.getInstance().get(Calendar.MONTH)];
	}


	public List<RoomBudget> getSpecificMonthRoomBudget(String username, List<String> months) {
		List<RoomBudget> roomBudgets = new ArrayList<>();
		for (String month : months) {
			Uri monthUri = null;
			String[] projection = {Room_Expenses.COLUMN_MONTH,
					Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_RENT_MARGIN,
					Room_Expenses.COLUMN_MAID, Room_Expenses.COLUMN_MAID_MARGIN,
					Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_ELECTRICITY_MARGIN,
					Room_Expenses.COLUMN_MISCELLANEOUS, Room_Expenses.COLUMN_MISCELLANEOUS_MARGIN,
					Room_Expenses.COLUMN_TOTAL, Room_Expenses.COLUMN_USERNAME, Room_Expenses.COLUMN_ROOM_ALIAS};
			String selection = Room_Expenses.COLUMN_MONTH + "=? AND " + Room_Expenses.COLUMN_USERNAME
					+ " =?";
			String[] selectionArgs = {month, username};
			Cursor cursor = mContext.getContentResolver().query(monthUri, projection,
					selection, selectionArgs, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				RoomBudget room = null;
				while (!cursor.isAfterLast()) {
					room = new RoomBudget();
					room.setMonth(
							cursor.getString(cursor.getColumnIndex(Room_Expenses.COLUMN_MONTH)));
					room.setRent(cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_RENT)));
					room.setElectricity(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
							.COLUMN_ELECTRICITY)));
					room.setMaid(cursor.getFloat(cursor.getColumnIndex(Room_Expenses.COLUMN_MAID)));
					room.setMiscellaneous(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
							.COLUMN_MISCELLANEOUS)));
					room.setRent_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
							.COLUMN_RENT_MARGIN)));
					room.setMaid_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
							.COLUMN_MAID_MARGIN)));
					room.setElectricity_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
							.COLUMN_ELECTRICITY_MARGIN)));
					room.setMiscellaneous_margin(cursor.getFloat(cursor.getColumnIndex(Room_Expenses
							.COLUMN_MISCELLANEOUS_MARGIN)));
					roomBudgets.add(room);
					cursor.moveToNext();
				}
			}
		}
		return roomBudgets;
	}
}