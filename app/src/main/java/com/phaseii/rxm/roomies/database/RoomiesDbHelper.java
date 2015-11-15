package com.phaseii.rxm.roomies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.phaseii.rxm.roomies.database.RoomiesContract.DATABASE_NAME;
import static com.phaseii.rxm.roomies.database.RoomiesContract.DATABASE_VERSION;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomDetails;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomExpenses;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats;
import static com.phaseii.rxm.roomies.database.RoomiesContract.RoomUserMap;
import static com.phaseii.rxm.roomies.database.RoomiesContract.SQL_CREATE_VIEW;
import static com.phaseii.rxm.roomies.database.RoomiesContract.SQL_DROP_VIEW;
import static com.phaseii.rxm.roomies.database.RoomiesContract.UserDetails;

/**
 * Created by Snehankur on 4/10/2015.
 */
public class RoomiesDbHelper extends SQLiteOpenHelper {

    /**
     *
     * @param context
     */
    public RoomiesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDetails.SQL_CREATE_ENTRIES);
        db.execSQL(RoomDetails.SQL_CREATE_ENTRIES);
        db.execSQL(RoomExpenses.SQL_CREATE_ENTRIES);
        db.execSQL(RoomStats.SQL_CREATE_ENTRIES);
        db.execSQL(RoomUserMap.SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_VIEW);
        db.execSQL(RoomExpenses.SQL_CREATE_RENT_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_CREATE_MAID_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_CREATE_ELEC_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_CREATE_MISC_SPENT_TRIGGER);

    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserDetails.SQL_DELETE_ENTRIES);
        db.execSQL(RoomDetails.SQL_DELETE_ENTRIES);
        db.execSQL(RoomExpenses.SQL_DELETE_ENTRIES);
        db.execSQL(RoomStats.SQL_DELETE_ENTRIES);
        db.execSQL(RoomUserMap.SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DROP_VIEW);
        db.execSQL(RoomExpenses.SQL_DROP_RENT_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_DROP_MAID_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_DROP_ELEC_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_DROP_MISC_SPENT_TRIGGER);
        db.execSQL(UserDetails.SQL_CREATE_ENTRIES);
        db.execSQL(RoomDetails.SQL_CREATE_ENTRIES);
        db.execSQL(RoomExpenses.SQL_CREATE_ENTRIES);
        db.execSQL(RoomStats.SQL_CREATE_ENTRIES);
        db.execSQL(RoomUserMap.SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_VIEW);
        db.execSQL(RoomExpenses.SQL_CREATE_RENT_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_CREATE_MAID_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_CREATE_ELEC_SPENT_TRIGGER);
        db.execSQL(RoomExpenses.SQL_CREATE_MISC_SPENT_TRIGGER);

    }
}
