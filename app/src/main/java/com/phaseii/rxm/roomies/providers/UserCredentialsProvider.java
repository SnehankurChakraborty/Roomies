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
 * Created by Snehankur on 4/9/2015.
 */
public class UserCredentialsProvider extends ContentProvider {


	public static final String AUTHORITY = "com.phaseii.rxm.roomies.providers.UserCredentialsProvider";
	public static final String URL = "content://" + AUTHORITY + "/usercredentials";
	public static final Uri CONTENT_URI = Uri.parse(URL);
	private static final UriMatcher uriMatcher;
	private static final int ALL_USER_DETAILS = 0;
	private static final int USER_ROW_WISE = 1;
	private static final int SPECIFIC_USER_DETAILS = 2;
	public static final String UNKNOWN_URI = "Unknown URI ";
	private SQLiteOpenHelper mdbHelper;
	private SQLiteDatabase db;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "usercredentials", ALL_USER_DETAILS);
		uriMatcher.addURI(AUTHORITY, "usercredentials/#", USER_ROW_WISE);
		uriMatcher.addURI(AUTHORITY, "usercredentials/username/*", SPECIFIC_USER_DETAILS);
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
		qb.setTables(RoomiesContract.UserCredentials.TABLE_NAME);
		switch (uriMatcher.match(uri)) {
			case ALL_USER_DETAILS:
				break;
			case USER_ROW_WISE:
				qb.appendWhere(RoomiesContract.Room_Expenses._ID + "=" + uri.getLastPathSegment());
				break;
			case SPECIFIC_USER_DETAILS:
				selectionArgs = new String[]{uri.getLastPathSegment()};
				/*qb.appendWhere(
						RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME + "=" + uri.getLastPathSegment());*/
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
			case ALL_USER_DETAILS:
				return ContentResolver.CURSOR_DIR_BASE_TYPE +
						"/vnd.com.phaseii.rxm.roomies.providers.usercredentials";
			case USER_ROW_WISE:
				return ContentResolver.CURSOR_ITEM_BASE_TYPE +
						"/vnd.com.phaseii.rxm.roomies.providers.usercredentials";
			case SPECIFIC_USER_DETAILS:
				return ContentResolver.CURSOR_ITEM_BASE_TYPE +
						"/vnd.com.phaseii.rxm.roomies.providers.usercredentials.username";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = mdbHelper.getWritableDatabase();
		Uri newUri = null;
		try {
			long rowId = db.insertOrThrow(RoomiesContract.UserCredentials.TABLE_NAME, null, values);
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
			case ALL_USER_DETAILS:
				count = db.delete(RoomiesContract.UserCredentials.TABLE_NAME, null,
						null);
				break;
			case USER_ROW_WISE:
				count = db.delete(RoomiesContract.UserCredentials.TABLE_NAME, selection,
						selectionArgs);
				break;
			case SPECIFIC_USER_DETAILS:
				count = db.delete(RoomiesContract.UserCredentials.TABLE_NAME, selection,
						selectionArgs);
				break;
			default:
				throw new IllegalStateException(UNKNOWN_URI + uri);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 0;
		db = mdbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
			case ALL_USER_DETAILS:
				count = db.update(RoomiesContract.UserCredentials.TABLE_NAME, values,
						null, null);
				break;
			case USER_ROW_WISE:
				count = db.update(RoomiesContract.UserCredentials.TABLE_NAME, values,
						selection, selectionArgs);
				break;
			case SPECIFIC_USER_DETAILS:
				count = db.update(RoomiesContract.UserCredentials.TABLE_NAME, values,
						selection, selectionArgs);
				break;
			default:
				throw new IllegalStateException(UNKNOWN_URI + uri);
		}
		return count;
	}
}
