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
 * @author Snehankur Chakraborty
 * @see android.content.ContentProvider
 * @since 6/28/2015
 * <h3>{@link com.phaseii.rxm.roomies.database.RoomiesContract.RoomStats room_stats}</h3>
 * <ul>
 * <li>_id              integer autoincrement</li>
 * <li>room_id          integer</li>
 * <li>month_year       text</li>
 * <li>rent_margin      integer</li>
 * <li>maid_margin      integer</li>
 * <li>elec_margin      integer</li>
 * <li>misc_margin      integer</li>
 * <li>total            integer</li>
 * </ul><br/>
 * <b>ALL ROOM URL</b> = <u style='color:blue'>content://com.phaseii.rxm.roomies.providers
 * .UserDetailsProvider/userdetails</u><br/>
 * <br/>
 * <b>SPECIFIC ROOM URL</b> = <u style='color:blue'>content://com.phaseii.rxm.roomies.providers
 * .UserDetailsProvider/userdetails/room_id</u>
 */

public class RoomStatsProvider extends ContentProvider {

	public static final String AUTHORITY = "com.phaseii.rxm.roomies.providers.RoomStatsProvider";
	public static final String URL = "content://" + AUTHORITY + "/roomstats";
	public static final Uri CONTENT_URI = Uri.parse(URL);
	private static final UriMatcher uriMatcher;
	private static final int ALL_DETAILS = 0;
	private static final int SPECIFIC_MONTH_DETAILS = 1;
	private static final int SPECIFIC_ROOM_DETAILS = 2;
	public static final String UNKNOWN_URI = "Unknown URI ";
	private SQLiteOpenHelper mdbHelper;
	private SQLiteDatabase db;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "roomstats", ALL_DETAILS);
		uriMatcher.addURI(AUTHORITY, "roomstats/month/*", SPECIFIC_MONTH_DETAILS);
		uriMatcher.addURI(AUTHORITY, "roomstats/room/*", SPECIFIC_ROOM_DETAILS);
	}

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
		qb.setTables(RoomiesContract.RoomStats.STATS_TABLE_NAME);
		switch (uriMatcher.match(uri)) {
			case ALL_DETAILS:
				break;
			case SPECIFIC_MONTH_DETAILS:
				qb.appendWhere(
						RoomiesContract.RoomStats.STATS_MONTH_YEAR + " = " +
								uri.getLastPathSegment());
				break;
			case SPECIFIC_ROOM_DETAILS:
				qb.appendWhere(RoomiesContract.RoomStats.STATS_ROOM_ID + " = " +
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
			case SPECIFIC_MONTH_DETAILS:
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
			case SPECIFIC_MONTH_DETAILS:
				break;
			case SPECIFIC_ROOM_DETAILS:
				break;
			default:
				throw new IllegalStateException(UNKNOWN_URI + uri);
		}
		try {
			long rowId = db.insertOrThrow(RoomiesContract.RoomStats.STATS_TABLE_NAME, null, values);
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
			case SPECIFIC_MONTH_DETAILS:
				if (!TextUtils.isEmpty(selection.trim())) {
					selection = selection + " AND ";
				}
				selection = selectionArgs + RoomiesContract.RoomStats.STATS_MONTH_YEAR + "= ?";
				selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
				break;
			case SPECIFIC_ROOM_DETAILS:
				if (!TextUtils.isEmpty(selection.trim())) {
					selection = selection + " AND ";
				}
				selection = selection + RoomiesContract.RoomExpenses.EXPENSE_ROOM_ID + "= ?";
				selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
				break;
			default:
				throw new IllegalStateException(UNKNOWN_URI + uri);
		}
		count = db.delete(RoomiesContract.RoomExpenses.EXPENSE_TABLE_NAME, selection,
				selectionArgs);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		db = mdbHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
			case ALL_DETAILS:
				break;
			case SPECIFIC_MONTH_DETAILS:
				if (!TextUtils.isEmpty(selection.trim())) {
					selection = selection + " AND ";
				}
				selection = selection + RoomiesContract.RoomStats.STATS_MONTH_YEAR + "= ?";
				selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
				break;
			case SPECIFIC_ROOM_DETAILS:
				if (!TextUtils.isEmpty(selection.trim())) {
					selection = selection + " AND ";
				}
				selection = selection + RoomiesContract.RoomExpenses.EXPENSE_ROOM_ID + "= ?";
				selectionArgs = RoomiesHelper.addElement(selectionArgs, uri.getLastPathSegment());
				break;
			default:
				throw new IllegalStateException(UNKNOWN_URI + uri);
		}
		int count = db.update(RoomiesContract.RoomExpenses.EXPENSE_TABLE_NAME, values,
				selection, selectionArgs);
		return count;
	}
}
