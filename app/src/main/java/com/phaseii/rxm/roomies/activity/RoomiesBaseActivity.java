package com.phaseii.rxm.roomies.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;

/**
 * Created by Snehankur on 5/10/2015.
 */
public abstract class RoomiesBaseActivity extends ActionBarActivity
		implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {
	public GoogleApiClient mGoogleApiClient;
	public boolean mIntentInProgress;
	public boolean mSignInClicked;
	public ProgressDialog mConnectionProgressDialog;
	public int RC_SIGN_IN = 0;
	public Toast mToast;
	public LoginButton loginButton;
	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	public LoginType loginType;
	private ProfileTracker mProfileTracker;

	public enum LoginType {
		GOOGLE,
		FACEBOOK
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.build();
		FacebookSdk.sdkInitialize(this.getApplicationContext());

		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(callbackManager,this);
		accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(
					AccessToken oldAccessToken,
					AccessToken currentAccessToken) {
				Profile.fetchProfileForCurrentAccessToken();
			}
		};
	}

	@Override
	public void onSuccess(LoginResult loginResult) {
		loginType = LoginType.FACEBOOK;

		mProfileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
				mProfileTracker.stopTracking();
			}
		};
		mProfileTracker.startTracking();
	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onError(FacebookException error) {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onConnected(Bundle bundle) {
		mSignInClicked = false;
		if (null != mConnectionProgressDialog) {
			mConnectionProgressDialog.dismiss();
		}
		loginType = LoginType.GOOGLE;
	}

	public abstract void setUpAuthenticatedUser(User user) throws RoomXpnseMngrException;

	public abstract void getProfileInformation(LoginResult loginResult);

	@Override
	public void onConnectionSuspended(int i) {
		if (null != mConnectionProgressDialog) {
			mConnectionProgressDialog.dismiss();
		}
		mConnectionProgressDialog = ProgressDialog.show(this, "Google Plus Log In",
				"Logging In", true);
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (null != mConnectionProgressDialog) {
			mConnectionProgressDialog.dismiss();
		}
		if (!mIntentInProgress) {
			if (mSignInClicked && result.hasResolution()) {

				// The user has already clicked 'sign-in' so we attempt to resolve all
				// errors until the user is signed in, or they cancel.
				try {
					mConnectionProgressDialog = ProgressDialog.show(this, "Google Plus Log In",
							"Logging In", true);
					result.startResolutionForResult(this, RC_SIGN_IN);
					mIntentInProgress = true;
				} catch (IntentSender.SendIntentException e) {
					// The intent was canceled before it was sent.  Return to the default
					// state and attempt to connect to get an updated ConnectionResult.
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != mConnectionProgressDialog) {
			mConnectionProgressDialog.dismiss();
		}
		if (requestCode == RC_SIGN_IN) {
			if (requestCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnected()) {
				mConnectionProgressDialog = ProgressDialog.show(this, "Google Plus Log In",
						"Logging In", true);
				mGoogleApiClient.reconnect();
			}
		} else {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void loginGPlus(View view) {
		if (view.getId() == R.id.gplus_sign_in_button && !mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			mConnectionProgressDialog = ProgressDialog.show(this, "Google Plus Log In",
					"Logging In", true);
			mGoogleApiClient.connect();
		}
	}


	public class User {
		private String username;
		private String email;
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	public void signoutGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
	}

	public void revokeGplusAccess() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(
					new ResultCallback<Status>() {
						@Override
						public void onResult(Status status) {

						}
					});
		}
	}

}
