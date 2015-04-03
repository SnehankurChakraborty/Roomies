package com.phaseii.rxm.roomies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.HashMap;

import static com.phaseii.rxm.roomies.database.RoomiesContract.*;

/**
 * Created by Snehankur on 3/19/2015.
 */
public class RoomInfoProvider extends ContentProvider {


	private static final String AUTHORITY = "com.phaseii.rxm.roomies.database";
	private static final String URL = "content://" + AUTHORITY + "/room_info";
	public static final Uri CONTENT_URI = Uri.parse(URL);

	public static final String UNKNOWN_URI = "Unknown URI ";
	private static final String EQUALS = "=";

	private static HashMap<String, String> ROOM_INFO_PROJECTION_MAP;

	private static final int ALL_ROOM_INFO = 1;
	private static final int ROOM_INFO_ROW_WISE = 2;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "room_info", ALL_ROOM_INFO);
		uriMatcher.addURI(AUTHORITY, "room_info/#", ROOM_INFO_ROW_WISE);
	}

	private SQLiteOpenHelper mRoomInfoDb;
	private SQLiteDatabase db;

	@Override
	public boolean onCreate() {
		Context context = getContext();
		mRoomInfoDb = new RoomInfo(context);
		return (db != null);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
	                    String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

	private class RoomInfo extends SQLiteOpenHelper {

		public RoomInfo(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Room.SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(Room.SQL_DELETE_ENTRIES);
			onCreate(db);

		}
	}
}
