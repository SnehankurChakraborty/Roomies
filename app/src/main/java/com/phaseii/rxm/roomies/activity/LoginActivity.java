package com.phaseii.rxm.roomies.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dialogs.SignupDialog;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.service.RoomService;
import com.phaseii.rxm.roomies.service.RoomServiceImpl;
import com.phaseii.rxm.roomies.service.UserService;
import com.phaseii.rxm.roomies.service.UserServiceImpl;

public class LoginActivity extends BaseActivity {

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
		SignInButton signInButton = (SignInButton) findViewById(R.id.gplus_sign_in_button);
		if (null != signInButton) {
			signInButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					loginGPlus(v);
				}
			});
		}

	}
	@Override
	public void loginGPlus(View v){
		super.loginGPlus(v);
	}


	private void checkValidUser(EditText usernameFeild, EditText passwordFeild) throws
			RoomXpnseMngrException {
		UserService user = new UserServiceImpl(getBaseContext());
		RoomService room = new RoomServiceImpl(getBaseContext());
		String password = user.getPassword(usernameFeild.getText().toString());
		if (null != password && password.equals(passwordFeild.getText().toString())) {
			user.retrieveUserData();
			room.getRoomDetailsWithMargin(usernameFeild.getText().toString());
			RoomiesHelper.startActivityHelper(this, getResources().getString(R
					.string.HomeScreen_Activity), null, true);
		} else {
			RoomiesHelper.createToast(getBaseContext(),
					RoomiesConstants.INVALID_USER_CREDENTIALS, mToast);
		}
	}

	public void setUpAuthenticatedUser(User user) throws RoomXpnseMngrException {
		UserService userService = new UserServiceImpl(this);
		RoomService room = new RoomServiceImpl(this);
		String password = RoomiesConstants.DUMMY_PASSWORD;
		if (null != password && password.equals(userService.getPassword(user.getUsername()))) {
			userService.retrieveUserData();
			room.getRoomDetailsWithMargin(user.getUsername());
			RoomiesHelper.startActivityHelper(this, getResources().getString(R
					.string.HomeScreen_Activity), null, true);
		} else {
			boolean isUserRegistered = userService.registerAuthenticatedUser(user.getUsername(),
					user.getEmail());
			if (isUserRegistered) {
				SharedPreferences sharedPref = getSharedPreferences
						(RoomiesConstants.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
				SharedPreferences.Editor mEditor = sharedPref.edit();
				mEditor.putString(RoomiesConstants.NAME, user.getUsername());
				mEditor.putString(RoomiesConstants.EMAIL_ID, user.getEmail());
				mEditor.putBoolean(RoomiesConstants.IS_LOGGED_IN, true);
				mEditor.apply();
				try {
					RoomiesConstants.googleApiClient=mGoogleApiClient;
					RoomiesHelper.startActivityHelper(this, getResources()
							.getString(R.string.HomeScreen_Activity), null, true);
				} catch (RoomXpnseMngrException e) {
					RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
				}
			}
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		super.onConnected(bundle);
		User user = getProfileInformation();
		try {
			setUpAuthenticatedUser(user);
		} catch (RoomXpnseMngrException e) {
			RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
		}
		/*Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();*/
	}

	public User getProfileInformation() {
		User user = null;
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				user = new User();
				Person currentPerson = currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				user.setUsername(personName);
				user.setEmail(email);
				/*Log.e(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);*/


				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				/*personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);*/

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
