package com.phaseii.rxm.roomies.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.EditText;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.providers.MiscellaneousProvider;
import com.phaseii.rxm.roomies.providers.RoomExpenseProvider;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by Snehankur on 4/12/2015.
 */
public class MiscServiceImpl {

	Context mContext;

	public MiscServiceImpl(Context mContext) {
		this.mContext = mContext;
	}

	final Uri monthUri = Uri.withAppendedPath(MiscellaneousProvider.CONTENT_URI,
			"month/" + new DateFormatSymbols().getMonths()[Calendar
					.getInstance().get(Calendar.MONTH)]);
	String currentMonth = new DateFormatSymbols().getMonths()[Calendar
			.getInstance().get(Calendar.MONTH)];

	public void insertMiscExpenses(EditText description, EditText quantity, EditText amount,
	                               String type, String username) {
		ContentValues values = new ContentValues();
		values.put(RoomiesContract.Misc_Expenses.COLUMN_DESCRIPTION,
				description.getText().toString());
		values.put(RoomiesContract.Misc_Expenses.COLUMN_QUANTITY,
				Float.valueOf(quantity.getText().toString()));
		values.put(RoomiesContract.Misc_Expenses.COLUMN_AMOUNT,
				Float.valueOf(amount.getText().toString()));
		values.put(RoomiesContract.Misc_Expenses.COLUMN_TYPE, type);
		values.put(RoomiesContract.Misc_Expenses.COLUMN_USERNAME, username);
		values.put(RoomiesContract.Misc_Expenses.COLUMN_MONTH, currentMonth);
		mContext.getContentResolver().insert(monthUri, values);
	}

	public float getCurrentTotalMiscExpense() {
		final Uri monthTotalUri = Uri.withAppendedPath(MiscellaneousProvider.CONTENT_URI,
				"total/" + new DateFormatSymbols().getMonths()[Calendar
						.getInstance().get(Calendar.MONTH)]);
		Cursor cursor = mContext.getContentResolver().query(monthTotalUri, null, null, null, null);
		cursor.moveToFirst();
		return cursor.getFloat(0);
	}


}
