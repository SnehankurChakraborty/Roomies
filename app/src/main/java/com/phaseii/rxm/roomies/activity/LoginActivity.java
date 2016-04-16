package com.phaseii.rxm.roomies.activity;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.UserDetails;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.factory.ViewBuilder;
import com.phaseii.rxm.roomies.factory.ViewBuilderFactory;
import com.phaseii.rxm.roomies.google.ConnectClient;
import com.phaseii.rxm.roomies.service.UserDetailsManager;
import com.phaseii.rxm.roomies.ui.customviews.RoomiesCircularImageView;
import com.phaseii.rxm.roomies.utils.ActivityUtils;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;
import com.phaseii.rxm.roomies.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import static com.phaseii.rxm.roomies.utils.Constants.APP_ERROR;


public class LoginActivity extends RoomiesBaseActivity {

    public static final int REQUEST_PERMISSION = 2;
    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int PROFILE_PIC_SIZE = 400;
    private static final String ERROR_FIRSTNAME = "Please enter a first name";
    private static final String ERROR_TITlE = "Please enter a title";
    private static final String ERROR_EMAIL = "Please enter a valid email address";
    private static final int RESULT_LOAD_IMAGE = 1;
    private ProgressDialog dialog;
    private TextInputLayout firstNameLayout;
    private TextInputLayout titleLayout;
    private TextInputLayout emailLayout;
    private EditText firstName;
    private EditText title;
    private EditText email;
    private RoomiesCircularImageView avatar;
    private Bitmap avatarPic;
    private CoordinatorLayout coordinatorLayout;
    private ConnectClient connectClient;

    /**
     * on create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configureView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        final ViewBuilder builder = ViewBuilderFactory.newInstance(LoginActivity.this)
                .newViewBuilder();
        connectClient = ConnectClient.getInstance(this);
        SignInButton signInButton = (SignInButton) builder.getView(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(connectClient.googleSignInOptions.getScopeArray());

        coordinatorLayout = (CoordinatorLayout) builder.getView(R.id.coordinatorLayout);
        firstNameLayout = (TextInputLayout) builder.getView(R.id.input_layout_first_name);
        titleLayout = (TextInputLayout) builder.getView(R.id.input_layout_title);
        emailLayout = (TextInputLayout) builder.getView(R.id.input_layout_email);
        firstName = builder.getEditText(R.id.input_first_name);
        title = builder.getEditText(R.id.input_title);
        email = builder.getEditText(R.id.input_email);
        avatar = (RoomiesCircularImageView) builder.getView(R.id.avatar);

        RoomiesHelper.setupTextInputLayout(firstNameLayout, firstName, ERROR_FIRSTNAME);
        RoomiesHelper.setupTextInputLayout(titleLayout, title, ERROR_TITlE);
        RoomiesHelper.setupTextInputLayout(emailLayout, email, ERROR_EMAIL);
        builder.getView(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(builder);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int storagePermission = ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int cameraPermission = ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.CAMERA);

                if (storagePermission != PackageManager.PERMISSION_GRANTED ||
                        cameraPermission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            LoginActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_PERMISSION
                    );
                }

                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String pickTitle = "Take or select a photo";
                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
                startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);

            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                connectClient.signinGPlus(new ConnectClient.OnConnectListener() {
                    @Override public void onConnect(GoogleSignInResult result) {
                        GoogleSignInAccount acct = result.getSignInAccount();
                        new LoadGPlusDetails().execute(acct);
                    }

                    @Override public void onFail() {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Check your network connection",
                                        Snackbar.LENGTH_LONG).setAction("SETTINGS",
                                        new View.OnClickListener() {
                                            @Override public void onClick(View v) {
                                                startActivity(new Intent(
                                                        Settings.ACTION_WIRELESS_SETTINGS));
                                            }
                                        });
                        snackbar.show();
                    }
                });
            }
        });
    }

    /**
     * This method is invoked when user tries to login with username/password
     */
    public void onSubmit(ViewBuilder builder) {
        if (null == firstName.getText() || firstName.getText().toString().trim().length() == 0) {
            firstNameLayout.setError(ERROR_FIRSTNAME);
        } else if (null == title.getText() || title.getText().toString().trim().length() == 0) {
            titleLayout.setError(ERROR_TITlE);
        } else if (null == email.getText() || email.getText().toString().trim()
                .length() == 0 || !Patterns.EMAIL_ADDRESS
                .matcher(email.getText().toString()).matches()) {
            emailLayout.setError(ERROR_EMAIL);
        } else {
            UserDetails userDetails = new UserDetails();
            userDetails
                    .setUserAlias(builder.getEditText(R.id.input_first_name).getText().toString());
            userDetails.setUsername(builder.getEditText(R.id.input_email).getText().toString());
            saveUserDetails(userDetails);
        }
    }

    public void saveUserDetails(UserDetails userDetails) {
        new SaveUserDetailsTask().execute(userDetails);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (null != data) {
                if (avatarPic != null) {
                    avatarPic.recycle();
                }
                Uri fullPhotoUri = data.getData();
                if (null != fullPhotoUri) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(fullPhotoUri,
                            filePathColumn, null, null, null);
                    String picturePath = null;

                    if (null != cursor) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    if (null != picturePath) {
                        avatarPic = BitmapFactory.decodeFile(picturePath);
                        avatar.setImageBitmap(avatarPic);
                        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                } else if (data.hasExtra("data")) {
                    avatarPic = data.getParcelableExtra("data");
                    avatar.setImageBitmap(avatarPic);
                    avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }
        } else if (requestCode == ConnectClient.RC_SIGN_IN) {
            connectClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class SaveUserDetailsTask extends AsyncTask<UserDetails, Void, Integer> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Saving your data");
            dialog.show();
        }

        @Override
        protected Integer doInBackground(UserDetails... userDetails) {
            UserDetailsManager manager = new UserDetailsManager(LoginActivity.this);
            int userId = manager.storeUserDetails(userDetails[0]);
            userDetails[0].setUserId(userId);
            if (userId > 0) {
                if (null != avatarPic) {
                    try {
                        String username = userDetails[0].getUsername();
                        File folder = new File(
                                Environment
                                        .getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES), "Roomies");
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        File file = new File(folder, username + getResources()
                                .getString(R.string.PROFILEJPG));
                        FileOutputStream fos = new FileOutputStream(file);
                        avatarPic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                RoomiesHelper.cacheDBtoPreferences(LoginActivity.this, null,
                        userDetails[0], null, null, false);
                return 1;
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
            if (integer > 0) {
                try {
                    ActivityUtils.startActivityHelper(LoginActivity.this,
                            getString(R.string.HomeScreen_Activity), null,
                            true, false);
                } catch (RoomXpnseMngrException e) {
                    ToastUtils.createToast(LoginActivity.this, APP_ERROR, null);
                    System.exit(0);
                }
            } else {
                ToastUtils.createToast(LoginActivity.this, APP_ERROR, null);
                System.exit(0);
            }

        }
    }


    /**
     * Async task to load profile picture from google profile Loads and stores the picture in the
     * file <username>profile.jpg
     */
    private class LoadGPlusDetails extends AsyncTask<GoogleSignInAccount, Void, UserDetails> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Fetching your info");
            dialog.show();
        }

        /**
         * Override this method to perform a computation on a background thread. The specified
         * parameters are the parameters passed to {@link #execute} by the caller of this task.
         *
         * This method can call {@link #publishProgress} to publish updates on the UI thread.
         *
         * @param accts The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override protected UserDetails doInBackground(GoogleSignInAccount... accts) {
            UserDetails gPlusProfileDetails = new UserDetails();
            String personName = accts[0].getDisplayName();
            String email = accts[0].getEmail();
            Uri personPhoto = accts[0].getPhotoUrl();
            /**
             * Save the details into the database
             * */
            gPlusProfileDetails.setUserAlias(personName);
            gPlusProfileDetails.setUsername(email);
            if (null != personPhoto) {
                try {
                    URL url = new URL(personPhoto.toString());
                    avatarPic = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return gPlusProfileDetails;
        }

        @Override
        protected void onPostExecute(UserDetails gPlusProfileDetails) {
            dialog.dismiss();
            avatar.setImageBitmap(avatarPic);
            avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
            saveUserDetails(gPlusProfileDetails);
        }
    }
}
