package com.phaseii.rxm.roomies.activity;


import android.app.AlertDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.SignupDialog;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.service.UserServiceImpl;

public class LoginActivity extends ActionBarActivity {

	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final View loginPage = findViewById(R.id.login_page);
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText username = (EditText) findViewById(R.id.username);
				EditText password = (EditText) findViewById(R.id.password);
				boolean isValidUsername = RoomiesHelper.setError("username",
						getBaseContext(), loginPage);
				boolean isValidPassword = RoomiesHelper.setError("password",
						getBaseContext(), loginPage);
				if (isValidPassword && isValidUsername) {
					try {
						checkValidUser(username, password);
					} catch (RoomXpnseMngrException e) {
						RoomiesHelper.createToast(getBaseContext(), RoomiesConstants.APP_ERROR,
								mToast);
						System.exit(0);
					}
				}
			}
		});
		TextView signuplink = (TextView) findViewById(R.id.signuplink);
		signuplink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment dialog = SignupDialog.getInstance(getBaseContext());
				dialog.show(getSupportFragmentManager(), "signup");
			}
		});

	}


	private void checkValidUser(EditText usernameFeild, EditText passwordFeild) throws
			RoomXpnseMngrException {
		UserServiceImpl user = new UserServiceImpl(getBaseContext());
		String password = user.getPassword(usernameFeild.getText().toString());
		if (password.equals(passwordFeild.getText().toString())) {
			SharedPreferences mshared = getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor mEditor = mshared.edit();
			mEditor.putBoolean(RoomiesConstants.IS_LOGGED_IN, true);
			mEditor.apply();
			RoomiesHelper.startActivityHelper(this, getResources().getString(R
					.string.HomeScreen_Activity), null, true);
		} else {
			RoomiesHelper.createToast(getBaseContext(),
					RoomiesConstants.INVALID_USER_CREDENTIALS, mToast);
		}
	}
}
