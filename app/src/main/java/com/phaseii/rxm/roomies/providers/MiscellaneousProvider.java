package com.phaseii.rxm.roomies.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.database.RoomiesDbHelper;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;

/**
 * Created by Snehankur on 4/6/2015.
 */
public class MiscellaneousProvider extends ContentProvider {

    public static final String AUTHORITY = "com.phaseii.rxm.roomies.providers.MiscellaneousProvider";
    public static final String URL = "content://" + AUTHORITY + "/miscexpense";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private static final UriMatcher uriMatcher;
    private static final int ALL_MISC_DETAILS = 0;
    private static final int MISC_ROW_WISE = 1;
    private static final int SPECIFIC_MONTH_DETAILS = 2;
    private static final int MONTH_TOTAL_DETAILS = 3;
    private static final int ALL_MONTHS = 4;
    public static final String UNKNOWN_URI = "Unknown URI ";
    private SQLiteOpenHelper mdbHelper;
    private SQLiteDatabase db;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "miscexpense", ALL_MISC_DETAILS);
        uriMatcher.addURI(AUTHORITY, "miscexpense/#", MISC_ROW_WISE);
        uriMatcher.addURI(AUTHORITY, "miscexpense/all/*", ALL_MONTHS);
        uriMatcher.addURI(AUTHORITY, "miscexpense/month/*", SPECIFIC_MONTH_DETAILS);
        uriMatcher.addURI(AUTHORITY, "miscexpense/total/*", MONTH_TOTAL_DETAILS);
    }

    @Override
    public boolean onCreate() {
        Context mContext = getContext();
        mdbHelper = new RoomiesDbHelper(mContext);
        db = mdbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RoomiesContract.Misc_Expenses.TABLE_NAME);
        /*checkColumns(projection);*/
        switch (uriMatcher.match(uri)) {
            case ALL_MISC_DETAILS:
                break;
            case MISC_ROW_WISE:
                qb.appendWhere(RoomiesContract.Misc_Expenses._ID + "=" + uri.getLastPathSegment());
                break;
            case SPECIFIC_MONTH_DETAILS:

				/*qb.appendWhere(
						RoomiesContract.Misc_Expenses.COLUMN_MONTH + "='" + uri
								.getLastPathSegment() + "'");*/
                break;
            case MONTH_TOTAL_DETAILS:
				/*qb.appendWhere(
						RoomiesContract.Misc_Expenses.COLUMN_MONTH + "=" + uri.getLastPathSegment());*/
                break;
            case ALL_MONTHS:
                qb.appendWhere(
                        RoomiesContract.Misc_Expenses.COLUMN_USERNAME + "= '" + uri
                                .getLastPathSegment() + "'");
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
            case ALL_MISC_DETAILS:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpense";
            case MISC_ROW_WISE:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpense";
            case SPECIFIC_MONTH_DETAILS:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/vnd.com.rxm.providers.roomexpense.month";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mdbHelper.getWritableDatabase();
        Uri newUri = null;
        try {
            long rowId = db.insertOrThrow(RoomiesContract.Misc_Expenses.TABLE_NAME, null, values);
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
        db = mdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_MISC_DETAILS:
                count = db.delete(RoomiesContract.Misc_Expenses.TABLE_NAME, null,
                        null);
                break;
            case MISC_ROW_WISE:
                count = db.delete(RoomiesContract.Misc_Expenses.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case SPECIFIC_MONTH_DETAILS:
                count = db.delete(RoomiesContract.Misc_Expenses.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                db.close();
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        db = mdbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_MISC_DETAILS:
                count = db.update(RoomiesContract.Misc_Expenses.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case MISC_ROW_WISE:
                count = db.update(RoomiesContract.Misc_Expenses.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case SPECIFIC_MONTH_DETAILS:
                count = db.update(RoomiesContract.Misc_Expenses.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case ALL_MONTHS:
                count = db.update(RoomiesContract.Misc_Expenses.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                db.close();
                throw new IllegalStateException(UNKNOWN_URI + uri);
        }
        return count;
    }
}
