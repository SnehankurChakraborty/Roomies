/*
package com.phaseii.rxm.roomies.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.helper.RoomXpnseMngrHelper;


public class LoginActivity extends ActionBarActivity {

	private Toast mToast;
	List<UserCredentials> users;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getAllUserData();
	}

	public void getLoginCredentials(View view) {
		boolean isValidCredentials = false;
		EditText usernameFeild = (EditText) findViewById(R.id.username);
		EditText passwordFeild = (EditText) findViewById(R.id.password);
		Button loginButton = (Button) findViewById(R.id.login);
		String username = null;
		String password = null;
		String emailAddress = null;
		if (!RoomXpnseMngrHelper.isFieldBlankOrEmpty(
				usernameFeild) && !RoomXpnseMngrHelper.isFieldBlankOrEmpty(passwordFeild)) {
			username = usernameFeild.getText().toString().trim();
			password = passwordFeild.getText().toString().trim();
			Log.d(USERNAME, username);
			Log.d(PASSWORD,	password);
			if (users != null) {
				for (UserCredentials user : users) {
					if (username.equalsIgnoreCase(user.getUsername())
							&& password.equalsIgnoreCase(user.getPassword())) {
						isValidCredentials = true;
						emailAddress = user.getEmailAddress();
						break;
					}
				}
			}
		} else {
			RoomXpnseMngrHelper.createToast(this, BLANK_USERNAME_PASSWORD,
					mToast);
		}
		if (isValidCredentials) {
			try {
				RoomXpnseMngrHelper.cacheData(this, username, password, emailAddress);
				RoomXpnseMngrHelper.startActivityHelper(this,
						getString(R.string.HomeScreen_Activity));
			} catch (Exception e) {
				RoomXpnseMngrHelper.createToast(this, APP_ERROR, mToast);
				ActivityCompat.finishAffinity(this);
			}
		} else {
			RoomXpnseMngrHelper.createToast(this, INVALID_USER_CREDENTIALS,
					mToast);
		}
	}

	private void getAllUserData() {
		try {
			UserCredentialService userCredentialService = new UserCredentialServiceImpl(this);
			users = userCredentialService.getAllUsers();
		} catch (RoomXpnseMngrException rxme) {
			RoomXpnseMngrHelper.createToast(this, APP_ERROR, mToast);
			ActivityCompat.finishAffinity(this);
		}
	}

}
*/
