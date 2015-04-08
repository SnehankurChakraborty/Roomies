package com.phaseii.rxm.roomies.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.phaseii.rxm.roomies.database.RoomiesContract;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.phaseii.rxm.roomies.database.RoomiesContract.*;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;

/**
 * Created by Snehankur on 3/19/2015.
 */
public class RoomExpenseProvider extends ContentProvider {

	public static final String AUTHORITY = "com.phaseii.rxm.roomies.providers";
	public static final String URL = "content://" + AUTHORITY + "/roomexpense";
	public static final Uri CONTENT_URI = Uri.parse(URL);
	private static final UriMatcher uriMatcher;
	private static final int ALL_ROOM_DETAILS = 0;
	private static final int ROOM_ROW_WISE = 1;
	private static final int SPECIFIC_MONTH_DETAILS = 2;
	public static final String UNKNOWN_URI = "Unknown URI ";
	private SQLiteOpenHelper mdbHelper;
	private SQLiteDatabase db;


	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "roomexpense", ALL_ROOM_DETAILS);
		uriMatcher.addURI(AUTHORITY, "roomexpense/#", ROOM_ROW_WISE);
		uriMatcher.addURI(AUTHORITY, "roomexpense/month/*", SPECIFIC_MONTH_DETAILS);
	}

	@Override
	public boolean onCreate() {
		Context mContext = getContext();
		mdbHelper = new RoomExpenseHelper(mContext);
		db = mdbHelper.getWritableDatabase();
		return (db != null);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
	                    String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(Room_Expenses.TABLE_NAME);
		/*checkColumns(projection);*/
		switch (uriMatcher.match(uri)) {
			case ALL_ROOM_DETAILS:
				break;
			case ROOM_ROW_WISE:
				qb.appendWhere(Room_Expenses._ID + "=" + uri.getLastPathSegment());
				break;
			case SPECIFIC_MONTH_DETAILS:
				/*qb.appendWhere(Room_Expenses.COLUMN_MONTH + "=" + uri.getLastPathSegment());*/

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
			case ROOM_ROW_WISE:
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
			long rowId = db.insertOrThrow(Room_Expenses.TABLE_NAME, null, values);
			if (rowId > 0) {
				newUri = ContentUris.withAppendedId(uri, rowId);
				getContext().getContentResolver().notifyChange(newUri, null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		db = mdbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
			case ALL_ROOM_DETAILS:
				count = db.delete(Room_Expenses.TABLE_NAME, null,
						null);
				break;
			case ROOM_ROW_WISE:
				count = db.delete(Room_Expenses.TABLE_NAME, selection,
						selectionArgs);
				break;
			case SPECIFIC_MONTH_DETAILS:
				count = db.delete(Room_Expenses.TABLE_NAME, selection,
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
			case ALL_ROOM_DETAILS:
				count = db.update(Room_Expenses.TABLE_NAME, values,
						null, null);
				break;
			case ROOM_ROW_WISE:
				count = db.update(Room_Expenses.TABLE_NAME, values,
						selection, selectionArgs);
				break;
			case SPECIFIC_MONTH_DETAILS:
				count = db.update(Room_Expenses.TABLE_NAME, values,
						selection, selectionArgs);
				break;
			default:
				db.close();
				throw new IllegalStateException(UNKNOWN_URI + uri);
		}
		return count;
	}

	private void checkColumns(String[] projection) {
		String[] available = {Room_Expenses.COLUMN_MONTH,
				Room_Expenses.COLUMN_RENT, Room_Expenses.COLUMN_MAID,
				Room_Expenses.COLUMN_ELECTRICITY, Room_Expenses.COLUMN_MISCELLANEOUS};
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

	private class RoomExpenseHelper extends SQLiteOpenHelper {

		public RoomExpenseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Room_Expenses.SQL_CREATE_ENTRIES);
			db.execSQL(Room_Expenses.SQL_CREATE_TRIGGER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(Room_Expenses.SQL_DELETE_ENTRIES);
			onCreate(db);
		}
	}

}
