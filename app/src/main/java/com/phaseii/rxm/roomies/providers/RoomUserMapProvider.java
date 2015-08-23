package com.phaseii.rxm.roomies.providers;

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

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.database.RoomiesDbHelper;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserMapProvider extends ContentProvider {

    public static final String AUTHORITY = "com.phaseii.rxm.roomies.providers.RoomUserMapProvider";
    public static final String URL = "content://" + AUTHORITY + "/roomusermap";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String UNKNOWN_URI = "Unknown URI ";
    private static final UriMatcher uriMatcher;
    private static final int ALL_DETAILS = 0;
    private static final int SPECIFIC_USER_DETAILS = 1;
    private static final int SPECIFIC_ROOM_DETAILS = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "roomusermap", ALL_DETAILS);
        uriMatcher.addURI(AUTHORITY, "roomusermap/month/#", SPECIFIC_USER_DETAILS);
        uriMatcher.addURI(AUTHORITY, "roomusermap/room/#", SPECIFIC_ROOM_DETAILS);
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
        qb.setTables(RoomiesContract.RoomUserMap.ROOM_USER_MAP_TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ALL_DETAILS:
                break;
            case SPECIFIC_USER_DETAILS:
                qb.appendWhere(
                        RoomiesContract.RoomUserMap.ROOM_USER_USER_ID + " = '" +
                                uri.getLastPathSegment() + "'");
                break;
            case SPECIFIC_ROOM_DETAILS:
                qb.appendWhere(RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID + " = " +
                        uri.getLastPathSegment());
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
            case ALL_DETAILS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpenses";
            case SPECIFIC_USER_DETAILS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpenses";
            case SPECIFIC_ROOM_DETAILS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpenses";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mdbHelper.getWritableDatabase();
        Uri newUri = null;
        switch (uriMatcher.match(uri)) {
            case ALL_DETAILS:
                break;
            case SPECIFIC_USER_DETAILS:
                break;
            case SPECIFIC_ROOM_DETAILS:
                break;
            default:
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        try {
            long rowId = db.insertOrThrow(RoomiesContract.RoomUserMap.ROOM_USER_MAP_TABLE_NAME, null, values);
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
        int count = 0;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RoomiesContract.RoomStats.STATS_TABLE_NAME);
        db = mdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_DETAILS:
                break;
            case SPECIFIC_USER_DETAILS:
                if (!TextUtils.isEmpty(selection.trim())) {
                    selection = selection + " AND ";
                }
                selection = selectionArgs + RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID + "= ?";
                selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
                break;
            case SPECIFIC_ROOM_DETAILS:
                if (!TextUtils.isEmpty(selection.trim())) {
                    selection = selection + " AND ";
                }
                selection = selection + RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID + "= ?";
                selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        count = db.delete(RoomiesContract.RoomUserMap.ROOM_USER_MAP_TABLE_NAME, selection,
                selectionArgs);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = mdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_DETAILS:
                break;
            case SPECIFIC_USER_DETAILS:
                if (!TextUtils.isEmpty(selection.trim())) {
                    selection = selection + " AND ";
                }
                selection = selectionArgs + RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID + "= ?";
                selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
                break;
            case SPECIFIC_ROOM_DETAILS:
                if (!TextUtils.isEmpty(selection.trim())) {
                    selection = selection + " AND ";
                }
                selection = selection + RoomiesContract.RoomUserMap.ROOM_USER_ROOM_ID + "= ?";
                selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
                break;
            default:
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        int count = db.update(RoomiesContract.RoomUserMap.ROOM_USER_MAP_TABLE_NAME, values,
                selection, selectionArgs);
        return count;
    }
}
