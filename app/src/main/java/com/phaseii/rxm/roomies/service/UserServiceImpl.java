package com.phaseii.rxm.roomies.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.providers.UserCredentialsProvider;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.EMAIL_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.NAME;

/**
 * Created by Snehankur on 4/9/2015.
 */
public class UserServiceImpl implements UserService {

	Context mContext;
	Uri userUri;
	Toast mToast;
	Cursor mCursor;
	SharedPreferences.Editor mEditor;

	public UserServiceImpl(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public String getPassword(String username) {
		String password = null;
		userUri = Uri.withAppendedPath(UserCredentialsProvider.CONTENT_URI,
				"username/" + username);
		String[] projection = {RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME,
				RoomiesContract.UserCredentials.COLUMN_NAME_PASSWORD,
				RoomiesContract.UserCredentials.COLUMN_NAME_EMAIL_ID,
				RoomiesContract.UserCredentials.COLUMN_SETUP_COMPLETED};
		String selection = RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME + "=?";
		mCursor = mContext.getContentResolver().query(userUri, projection, selection, null,
				null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			password = mCursor.getString(
					mCursor.getColumnIndex(RoomiesContract.UserCredentials.COLUMN_NAME_PASSWORD));
		}
		return password;
	}

	@Override
	public boolean registerUser(EditText username, EditText email, EditText password) {
		boolean isUserRegistered = false;
		ContentValues values = new ContentValues();
		values.put(RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME,
				username.getText().toString());
		values.put(RoomiesContract.UserCredentials.COLUMN_NAME_EMAIL_ID,
				email.getText().toString());
		values.put(RoomiesContract.UserCredentials.COLUMN_NAME_PASSWORD,
				password.getText().toString());
		values.put(RoomiesContract.UserCredentials.COLUMN_SETUP_COMPLETED, "false");
		try {
			mContext.getContentResolver().insert(UserCredentialsProvider.CONTENT_URI, values);
			isUserRegistered = true;
		} catch (RoomiesStateException e) {
			RoomiesHelper.createToast(mContext, "USER ALREADY EXISTS", mToast);
		}
		return isUserRegistered;
	}

	@Override
	public void retrieveUserData() {
		if (null != mCursor) {
			SharedPreferences mSharedPreferences = mContext.getSharedPreferences(RoomiesConstants
					.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
			mEditor = mSharedPreferences.edit();
			mEditor.putBoolean(RoomiesConstants.IS_LOGGED_IN, true);
			mCursor.moveToFirst();
			mEditor.putString(RoomiesConstants.NAME, mCursor.getString(mCursor.getColumnIndex(
					RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME)));
			mEditor.putString(RoomiesConstants.EMAIL_ID, mCursor.getString(mCursor.getColumnIndex(
					RoomiesContract.UserCredentials.COLUMN_NAME_EMAIL_ID)));
			mEditor.apply();
		} else {
			RoomiesHelper.createToast(mContext, RoomiesConstants.APP_ERROR, mToast);
			System.exit(0);
		}
	}

	@Override
	public void completeSetup(String username) {
		userUri = Uri.withAppendedPath(UserCredentialsProvider.CONTENT_URI,
				"username/" + username);
		String selection = RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME + " =?";
		String selectionArgs[] = {username};
		ContentValues values = new ContentValues();
		values.put(RoomiesContract.UserCredentials.COLUMN_SETUP_COMPLETED, "true");
		mContext.getContentResolver().update(userUri, values, selection, selectionArgs);
	}

	@Override
	public boolean isSetupCompleted(String username) {
		userUri = Uri.withAppendedPath(UserCredentialsProvider.CONTENT_URI,
				"username/" + username);
		String selection = RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME + " =?";
		String selectionArgs[] = {username};
		String projection[] = {RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME,
				RoomiesContract.UserCredentials.COLUMN_SETUP_COMPLETED};
		Cursor mCursor = mContext.getContentResolver().query(userUri, projection, selection,
				selectionArgs, null);
		mCursor.moveToFirst();
		return Boolean.valueOf(mCursor.getString(
				mCursor.getColumnIndex(RoomiesContract.UserCredentials.COLUMN_SETUP_COMPLETED)));
	}

	@Override
	public boolean update(String username, String newVal, String column) {
		userUri = Uri.withAppendedPath(UserCredentialsProvider.CONTENT_URI,
				"username/" + username);
		String selection = RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME + " =?";
		String selectionArgs[] = {username};
		ContentValues values = new ContentValues();
		values.put(column, newVal);
		int count = mContext.getContentResolver().update(userUri, values, selection, selectionArgs);
		if(count>0){
			mEditor = mContext.getSharedPreferences(
					RoomiesConstants.ROOM_INFO_FILE_KEY,
					Context.MODE_PRIVATE).edit();
			if(RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME.equals(column)){
				mEditor.putString(NAME, newVal);
				mEditor.apply();
			}else if(RoomiesContract.UserCredentials.COLUMN_NAME_EMAIL_ID.equals(column)){
				mEditor.putString(EMAIL_ID, newVal);
				mEditor.apply();
			}
		}
		return count > 0 ? true : false;
	}
}