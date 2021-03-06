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

package com.phaseii.rxm.roomies.database.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.phaseii.rxm.roomies.database.RoomiesDbHelper;
import com.phaseii.rxm.roomies.database.contract.RoomiesContract;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;

import static com.phaseii.rxm.roomies.database.contract.RoomiesContract.RoomDetails;

/**
 * @author Snehankur Chakraborty
 * @see android.content.ContentProvider
 * @since 6/28/2015 <h3>{@link RoomiesContract.RoomDetails room_details}</h3> <ul> <li>_id integer
 * autoincrement</li> <li>room_alias    text</li> <li>no_of_persons integer</li> </ul><br/> <b>ALL
 * MONTH URL</b> = <u style='color:blue'>content://com.phaseii.rxm.roomies.providers
 * .RoomDetailsProvider/roomdetails</u><br/> <br/> <b>SPECIFIC MONTH URL</b> = <u
 * style='color:blue'>content://com.phaseii.rxm.roomies.providers .RoomDetailsProvider/roomdetails/july15</u>
 */
public class RoomDetailsProvider extends ContentProvider {

    public static final String AUTHORITY = "com.phaseii.rxm.roomies.database.provider.RoomDetailsProvider";
    public static final String URL = "content://" + AUTHORITY + "/roomdetails";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String UNKNOWN_URI = "Unknown URI ";
    private static final UriMatcher uriMatcher;
    private static final int ALL_ROOM_DETAILS = 0;
    private static final int SPECIFIC_ROOM_DETAILS = 1;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "roomdetails", ALL_ROOM_DETAILS);
        uriMatcher.addURI(AUTHORITY, "roomdetails/room/*", SPECIFIC_ROOM_DETAILS);
    }

    private SQLiteOpenHelper mdbHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        mdbHelper = new RoomiesDbHelper(getContext());
        db = mdbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RoomDetails.DETAILS_TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ALL_ROOM_DETAILS:
                break;
            case SPECIFIC_ROOM_DETAILS:
                qb.appendWhere(RoomDetails.DETAILS_ROOM_ID + "= " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_ROOM_DETAILS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpense";
            case SPECIFIC_ROOM_DETAILS:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpense";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mdbHelper.getWritableDatabase();
        Uri newUri = null;
        switch (uriMatcher.match(uri)) {
            case ALL_ROOM_DETAILS:
                break;
            case SPECIFIC_ROOM_DETAILS:
                break;
            default:
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        try {
            long rowId = db.insertOrThrow(RoomDetails.DETAILS_TABLE_NAME, null, values);
            if (rowId > 0) {
                newUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(newUri, null);
            }
        } catch (SQLException e) {
            throw new RoomiesStateException(e);
        }
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = mdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ROOM_DETAILS:
                break;
            case SPECIFIC_ROOM_DETAILS:
                if (!TextUtils.isEmpty(selection.trim())) {
                    selection = selection + " AND ";
                }
                selection = selection + RoomDetails.DETAILS_ROOM_ID + " =?";
                RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        int count = db.delete(RoomDetails.DETAILS_TABLE_NAME, selection,
                selectionArgs);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = mdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ROOM_DETAILS:
                break;
            case SPECIFIC_ROOM_DETAILS:
                if (!TextUtils.isEmpty(selection.trim())) {
                    selection = selection + " AND ";
                }
                selection = selection + RoomDetails.DETAILS_ROOM_ID + " =?";
                RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        int count = db.update(RoomDetails.DETAILS_TABLE_NAME, values,
                selection, selectionArgs);
        return count;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (null != db && db.isOpen()) {
            db.close();
        }
    }
}
