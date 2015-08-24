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
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.dao.UserDetailsDaoImpl;
import com.phaseii.rxm.roomies.dialogs.SignupDialog;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.manager.RoomUserStatManager;
import com.phaseii.rxm.roomies.model.UserDetails;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USERNAME;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USER_ID;


public class LoginActivity extends RoomiesBaseActivity {

    private static final int PROFILE_PIC_SIZE = 400;
    private Toast mToast;
    private List<UserDetails> userDetailsList;
    private RoomiesDao roomiesDao;
    private RoomUserStatManager manager;

    /**
     * on create
     *
     * @param savedInstanceState
     */
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
                    /**
                     * check if username/password match
                     */
                    Map<ServiceParam, Object> paramMap = new HashMap<>();
                    List<QueryParam> params = new ArrayList<QueryParam>();
                    params.add(QueryParam.USERNAME);
                    paramMap.put(ServiceParam.SELECTION, params);
                    List<String> selectionArgs = new ArrayList<String>();
                    selectionArgs.add(username.getText().toString());
                    paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);


                    /**
                     * get user details based on user id
                     */
                    roomiesDao = new UserDetailsDaoImpl(LoginActivity.this);
                    userDetailsList = (List<UserDetails>) roomiesDao.getDetails(paramMap);
                    if (null != userDetailsList && userDetailsList.size() > 0) {
                        UserDetails userDetails = userDetailsList.get(0);
                        if (null != userDetails.getPassword() && userDetails.getPassword().equals(
                                password.getText().toString())) {

                            SharedPreferences.Editor mEditor = getSharedPreferences
                                    (PREF_ROOMIES_KEY, Context.MODE_PRIVATE).edit();
                            mEditor.putString(PREF_USER_ALIAS, userDetails.getUserAlias());
                            mEditor.putString(PREF_USER_ID, String.valueOf(
                                    userDetails.getUserId()));
                            mEditor.putString(PREF_USERNAME, userDetails.getUsername());
                            mEditor.putBoolean(IS_LOGGED_IN, true);
                            mEditor.apply();
                            /**
                             * call method to get all details of the user and cache into shared
                             * preferences
                             */
                            getAllDetails(userDetails, false);

                        } else {

                            /**
                             * display toast when userid/password doesn't match
                             */
                            RoomiesHelper.createToast(getBaseContext(),
                                    RoomiesConstants.INVALID_USER_CREDENTIALS, mToast);
                        }
                    } else {
                        RoomiesHelper.createToast(getBaseContext(),
                                RoomiesConstants.PLEASE_SIGN_UP_TO_USE_ROOMIES, mToast);
                        DialogFragment dialog = SignupDialog.getInstance();
                        dialog.show(getSupportFragmentManager(), "signup");
                    }

                }
            }
        });

        /**
         * for signup new user
         */
        TextView signuplink = (TextView) findViewById(R.id.signuplink);
        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = SignupDialog.getInstance();
                dialog.show(getSupportFragmentManager(), "signup");
            }
        });

        /**
         * for google sign in
         */
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

    /**
     * google plus login
     *
     * @param v
     */
    @Override
    public void loginGPlus(View v) {
        super.loginGPlus(v);
    }

    /**
     * for google login once the connection is established and user is logged in
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        getProfileInformation(null);

    }

    /**
     * for FB login once the connection is established and user is logged in
     *
     * @param loginResult
     */
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

                    /**
                     * get user details based on email address of the user
                     */
                    RoomiesDao service = new UserDetailsDaoImpl(getBaseContext());
                    Map<ServiceParam, Object> paramMap = new HashMap<>();
                    List<QueryParam> params = new ArrayList<QueryParam>();
                    params.add(QueryParam.USERNAME);
                    paramMap.put(ServiceParam.SELECTION, params);
                    paramMap.put(ServiceParam.SELECTIONARGS, user.getEmail());
                    UserDetails userDetails = (UserDetails) service.getDetails(paramMap);

                    if (null == userDetails.getUserAlias()) {

                        /**
                         * if the user details from database is null, user has not signed up yet
                         * So save the details into the database
                         */
                        userDetails = new UserDetails();
                        userDetails.setUsername(email);
                        userDetails.setUserAlias(personName);

                        Map<ServiceParam, UserDetails> userMap = new HashMap<>();
                        userMap.put(ServiceParam.MODEL, userDetails);

                        if (service.insertDetails(userMap) > 0) {

                            /**
                             * save the same details in shared preferences
                             */
                            SharedPreferences.Editor mEditor = getSharedPreferences
                                    (PREF_ROOMIES_KEY, Context.MODE_PRIVATE).edit();
                            mEditor.putString(PREF_USER_ALIAS, personName);
                            mEditor.putString(PREF_USERNAME, email);
                            mEditor.putBoolean(IS_LOGGED_IN, true);
                            mEditor.apply();

                            /**
                             * start the Get started activity
                             */
                            try {
                                RoomiesHelper.startActivityHelper(this, getResources()
                                        .getString(R.string.GetStartedWizard), null, true);
                            } catch (RoomXpnseMngrException e) {
                                RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
                                System.exit(0);
                            }
                        }
                    }

                    /**
                     * call method to get all details of the user and cache into shared preferences
                     */
                    getAllDetails(userDetails, true);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Person information is null", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
                System.exit(0);
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
                            /*try {
                                setUpAuthenticatedUser(fbUser);
							} catch (RoomXpnseMngrException e) {
								RoomiesHelper.createToast(LoginActivity.this,
										RoomiesConstants.APP_ERROR, mToast);
							}*/
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,link");
            request.setParameters(parameters);
            request.executeAsync();
        }

    }


    /**
     * get all the other details from database based on user details
     *
     * @param userDetails
     * @param isGoogleFBlogin
     */
    @Override
    protected void getAllDetails(UserDetails userDetails, boolean isGoogleFBlogin) {
        RoomiesHelper.createToast(this, "logged In", mToast);
        manager = new RoomUserStatManager(LoginActivity.this);

        if (!manager.loadRoomDetails(userDetails.getUsername())) {
            /**
             * if the user has not completed the initial setup, then save the same status in
             * shared preferences and load the HomeScreen activity*/

            SharedPreferences.Editor mEditor = getSharedPreferences(
                    RoomiesConstants.PREF_ROOMIES_KEY, Context.MODE_PRIVATE).edit();
            mEditor.putBoolean(RoomiesConstants.IS_SETUP_COMPLETED, false);
            mEditor.apply();
            try {
                RoomiesHelper.startActivityHelper(this, getResources()
                        .getString(R.string.HomeScreen_Activity), null, true);
            } catch (RoomXpnseMngrException e) {
                RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
            }
        } else {

            /**
             * start HomeScreen activity
             * */

            try {
                RoomiesHelper.startActivityHelper(this, getResources()
                        .getString(R.string.HomeScreen_Activity), null, true);
            } catch (RoomXpnseMngrException e) {
                RoomiesHelper.createToast(this, RoomiesConstants.APP_ERROR, mToast);
                System.exit(0);
            }
        }
    }

    /**
     * Async task to load profile picture from google profile
     * Loads and stores the picture in the file <username>profile.jpg
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        private File imgProfilePic;

        public LoadProfileImage(File imgProfilePic) {
            this.imgProfilePic = imgProfilePic;
        }

        /**
         * Load the image from the given url
         *
         * @param urls
         * @return
         */
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

        /**
         * save the picture in the given location
         *
         * @param bitmap
         */
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
