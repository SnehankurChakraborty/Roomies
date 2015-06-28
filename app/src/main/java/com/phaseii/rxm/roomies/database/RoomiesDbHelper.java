package com.phaseii.rxm.roomies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.phaseii.rxm.roomies.database.RoomiesContract.DATABASE_NAME;
import static com.phaseii.rxm.roomies.database.RoomiesContract.DATABASE_VERSION;
import static com.phaseii.rxm.roomies.database.RoomiesContract.Room_Expenses;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserCredentials;
import static com.phaseii.rxm.roomies.database.RoomiesContract.Misc_Expenses;
import static com.phaseii.rxm.roomies.database.RoomiesContract.*;

/**
 * Created by Snehankur on 4/10/2015.
 */
public class RoomiesDbHelper extends SQLiteOpenHelper {
	public RoomiesDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Room_Expenses.SQL_CREATE_ENTRIES);
		db.execSQL(Room_Expenses.SQL_CREATE_TRIGGER);
		db.execSQL(UserCredentials.SQL_CREATE_ENTRIES);
		db.execSQL(Misc_Expenses.SQL_CREATE_ENTRIES);
		db.execSQL(Misc_Expenses.SQL_CREATE_TRIGGER);

		db.execSQL(UserDetails.SQL_CREATE_ENTRIES);
		db.execSQL(RoomDetails.SQL_CREATE_ENTRIES);
		db.execSQL(RoomExpenses.SQL_CREATE_ENTRIES);
		db.execSQL(RoomExpenses.SQL_CREATE_TRIGGER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Room_Expenses.SQL_DELETE_ENTRIES);
		db.execSQL(Room_Expenses.SQL_DROP_TRIGGER);
		db.execSQL(Room_Expenses.SQL_CREATE_ENTRIES);
		db.execSQL(Room_Expenses.SQL_CREATE_TRIGGER);
		db.execSQL(UserCredentials.SQL_DELETE_ENTRIES);
		db.execSQL(UserCredentials.SQL_CREATE_ENTRIES);
		db.execSQL(Misc_Expenses.SQL_DELETE_ENTRIES);
		db.execSQL(Misc_Expenses.SQL_DROP_TRIGGER);
		db.execSQL(Misc_Expenses.SQL_CREATE_ENTRIES);
		db.execSQL(Misc_Expenses.SQL_CREATE_TRIGGER);

		db.execSQL(UserDetails.SQL_DELETE_ENTRIES);
		db.execSQL(RoomDetails.SQL_DELETE_ENTRIES);
		db.execSQL(RoomExpenses.SQL_DELETE_ENTRIES);
		db.execSQL(RoomExpenses.SQL_DROP_TRIGGER);
		db.execSQL(UserDetails.SQL_CREATE_ENTRIES);
		db.execSQL(RoomDetails.SQL_CREATE_ENTRIES);
		db.execSQL(RoomExpenses.SQL_CREATE_ENTRIES);
		db.execSQL(RoomExpenses.SQL_CREATE_TRIGGER);
	}
}
