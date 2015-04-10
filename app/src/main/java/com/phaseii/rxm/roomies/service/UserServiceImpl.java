package com.phaseii.rxm.roomies.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.providers.UserCredentialsProvider;

/**
 * Created by Snehankur on 4/9/2015.
 */
public class UserServiceImpl implements UserService {

	Context mContext;
	Uri userUri;
	Toast mToast;

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
				RoomiesContract.UserCredentials.COLUMN_NAME_EMAIL_ID};
		String selection = RoomiesContract.UserCredentials.COLUMN_NAME_USERNAME + "=?";
		Cursor cursor = mContext.getContentResolver().query(userUri, projection, selection, null,
				null);
		cursor.moveToFirst();
		password = cursor.getString(
				cursor.getColumnIndex(RoomiesContract.UserCredentials.COLUMN_NAME_PASSWORD));
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
		try {
			mContext.getContentResolver().insert(UserCredentialsProvider.CONTENT_URI, values);
		} catch (RoomiesStateException e) {
			RoomiesHelper.createToast(mContext, "USER ALREADY EXISTS", mToast);
			return false;
		}
		return true;
	}
}
