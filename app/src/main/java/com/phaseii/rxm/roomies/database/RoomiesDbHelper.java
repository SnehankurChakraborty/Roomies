/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.DATABASE_NAME;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.DATABASE_VERSION;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomDetails;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomExpenses;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomStats;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomUserMap;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.SQL_CREATE_VIEW;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.SQL_DROP_VIEW;
import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.UserDetails;

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
