package com.phaseii.rxm.roomies.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends RoomiesBaseActivity {

	private static final int PROFILE_PIC_SIZE = 400;
	private Toast mToast;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		/*LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
		loginButton.setReadPermissions(Arrays.asList("public_profile, email"));*/
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
	public void loginGPlus(View v) {
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
		RoomService room = new RoomServiceImpl(this);
		SharedPreferences sharedPref = getSharedPreferences
				(RoomiesConstants.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = sharedPref.edit();
		mEditor.putString(RoomiesConstants.NAME, user.getUsername());
		mEditor.putString(RoomiesConstants.EMAIL_ID, user.getEmail());
		mEditor.putBoolean(RoomiesConstants.IS_LOGGED_IN, true);
		mEditor.putBoolean(RoomiesConstants.IS_GOOGLE_FB_LOGIN, true);

		if (room.getRoomDetailsWithMargin(user.getEmail())) {
			mEditor.putBoolean(RoomiesConstants.IS_SETUP_COMPLETED, true);
		} else {
			if (room.isUserSaved(user.getEmail())) {
				mEditor.putBoolean(RoomiesConstants.IS_SETUP_COMPLETED, true);
				room.getSpecificMonthDetails(user.getEmail(), sharedPref.getString(RoomiesConstants
						.PREVIOUS_MONTH, null));
				room.getTotalSpent(user.getEmail());
			}
		}
		mEditor.apply();
		RoomiesHelper.startActivityHelper(this, getResources().getString(R
				.string.HomeScreen_Activity), null, true);
		try {
			RoomiesHelper.startActivityHelper(this, getResources()
					.getString(R.string.HomeScreen_Activity), null, true);
		} catch (RoomXpnseMngrException e) {
			RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		super.onConnected(bundle);
		getProfileInformation(null);

	}

	@Override
	public void onSuccess(LoginResult loginResult) {
		super.onSuccess(loginResult);
		getProfileInformation(loginResult);
	}

	@Override
	public void getProfileInformation(LoginResult loginResult) {
		User user = null;
		if (loginType != LoginType.FACEBOOK) {

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
					personPhotoUrl = personPhotoUrl.substring(0,
							personPhotoUrl.length() - 2)
							+ PROFILE_PIC_SIZE;
					File imgProfilePic = new File(getFilesDir(),
							personName + getResources().getString(R.string.PROFILEJPG));
					new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
					try {
						setUpAuthenticatedUser(user);
					} catch (RoomXpnseMngrException e) {
						RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
					}

				} else {
					Toast.makeText(getApplicationContext(),
							"Person information is null", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {

			}
		} else {

			final AccessToken accessToken = loginResult.getAccessToken();
			final User fbUser = new User();
			mConnectionProgressDialog = ProgressDialog.show(this, "Facebook Log In",
					"Logging In...", true);
			GraphRequest request = GraphRequest.newMeRequest(
					accessToken,
					new GraphRequest.GraphJSONObjectCallback() {
						@Override
						public void onCompleted(
								JSONObject user,
								GraphResponse response) {
							fbUser.setUsername(user.optString("name"));
							fbUser.setEmail(user.optString("email"));
							fbUser.setId(user.optString("id"));
							File imgProfilePic = new File(getFilesDir(),
									fbUser.getUsername() + getResources().getString(
											R.string.PROFILEJPG));
							String personPhotoUrl =
									"https://graph.facebook.com/" + fbUser.getId() +
											"/picture?width=10000&height=10000";
							new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
							if (null != mConnectionProgressDialog) {
								mConnectionProgressDialog.dismiss();
							}
							try {
								setUpAuthenticatedUser(fbUser);
							} catch (RoomXpnseMngrException e) {
								RoomiesHelper.createToast(LoginActivity.this,
										RoomiesConstants.APP_ERROR, mToast);
							}
						}
					});
			Bundle parameters = new Bundle();
			parameters.putString("fields", "id,name,email,link");
			request.setParameters(parameters);
			request.executeAsync();
		}

	}

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		private File imgProfilePic;

		public LoadProfileImage(File imgProfilePic) {
			this.imgProfilePic = imgProfilePic;
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {

			}
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imgProfilePic);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
