/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.UserDetails;
import com.phaseii.rxm.roomies.service.UserDetailsManager;
import com.phaseii.rxm.roomies.ui.customviews.AlphaForegroundColorSpan;
import com.phaseii.rxm.roomies.ui.customviews.RoomiesCircularImageView;
import com.phaseii.rxm.roomies.ui.customviews.RoomiesScrollView;
import com.phaseii.rxm.roomies.ui.dialogs.EditProfileDialog;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends RoomiesBaseActivity implements
                                                         EditProfileDialog.EditProfileListener {

    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public static final int REQUEST_PERMISSION = 2;
    private static final int RESULT_LOAD_IMAGE = 1;
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;
    private RoomiesCircularImageView avatar;
    private int lastTopValueAssigned = 0;
    private UserDetails userDetails;
    private String username;
    private SharedPreferences mSharedPreferences;
    private boolean isGoogleFBLogin;
    private Toolbar mToolbar;
    private int currentApiVersion;
    private Bitmap avatarPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configureView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        currentApiVersion = Build.VERSION.SDK_INT;

        mSpannableString = new SpannableString("Profile");
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xFFFFFF);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Profile");
        setSupportActionBar(mToolbar);
        final ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.primary_home));
        cd.setAlpha(0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(cd);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_roomies_back);

        RoomiesScrollView scrollViewHelper = (RoomiesScrollView) findViewById(
                R.id.scrollViewHelper);

        scrollViewHelper.setOnScrollViewListener(new RoomiesScrollView.OnScrollViewListener() {
            @Override
            public void onScrollChanged(RoomiesScrollView v, int l, int t, int oldl, int oldt) {
                setTitleAlpha(255 - getAlphaforActionBar(v.getScrollY()));
                cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
                if (currentApiVersion >= Build.VERSION_CODES.HONEYCOMB) {
                    parallaxImage(avatar);
                }
            }

            private void parallaxImage(View view) {
                Rect rect = new Rect();
                view.getLocalVisibleRect(rect);
                if (lastTopValueAssigned != rect.top) {
                    lastTopValueAssigned = rect.top;
                    view.setY((float) (rect.top / 2.0));
                }
            }

            private int getAlphaforActionBar(int scrollY) {
                int minDist = 0, maxDist = (int) TypedValue.applyDimension(TypedValue
                                .COMPLEX_UNIT_DIP, 250,
                        getResources().getDisplayMetrics());
                if (scrollY > maxDist) {
                    return 255;
                } else {
                    if (scrollY < minDist) {
                        return 0;
                    } else {
                        return (int) ((255.0 / maxDist) * scrollY);
                    }
                }
            }

        });
        setUpProfilePic();
        populateFeilds();
    }

    public void setUpProfilePic() {
        findViewById(R.id.change_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int storagePermission = ActivityCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int cameraPermission = ActivityCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.CAMERA);

                if (storagePermission != PackageManager.PERMISSION_GRANTED ||
                        cameraPermission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            ProfileActivity.this,
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
        username = getSharedPreferences(Constants.PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE).getString(Constants.PREF_USERNAME, null);
        avatar = (RoomiesCircularImageView) findViewById(R.id.avatar);
        avatar.setBorderWidth(5);
        avatar.setBorderColor(getResources().getColor(R.color.white));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(new File(Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + File.separator + "Roomies",
                username + getResources().getString(
                        R.string.PROFILEJPG)).getAbsolutePath(), options);
        if (null != bitmap) {
            avatar.setImageBitmap(bitmap);
        }
    }


    private void setTitleAlpha(float alpha) {
        if (alpha < 1) {
            alpha = 1;
        }
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(mSpannableString);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (null != data) {
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
                        new SaveUserDetailsTask().execute(avatarPic);
                    }
                } else if (data.hasExtra("data")) {
                    avatarPic = data.getParcelableExtra("data");
                    avatar.setImageBitmap(avatarPic);
                    avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    new SaveUserDetailsTask().execute(avatarPic);
                }
            }
        }
    }

    private void populateFeilds() {

        mSharedPreferences = getSharedPreferences(Constants.PREF_ROOMIES_KEY, MODE_PRIVATE);
        String userAlias = mSharedPreferences.getString(Constants.PREF_USER_ALIAS, null);
        String mailVal = mSharedPreferences.getString(Constants.PREF_USERNAME, "");
        String phoneVal = mSharedPreferences.getString(Constants.PREF_USER_PHONE, "0");
        String aboutMeVal = mSharedPreferences.getString(Constants.PREF_USER_ABOUT_ME, "");
        String locationVal = mSharedPreferences.getString(Constants.PREF_USER_LOCATION, "");
        isGoogleFBLogin = mSharedPreferences.getBoolean(Constants.IS_GOOGLE_FB_LOGIN, false);

        ((TextView) findViewById(R.id.name)).setText(userAlias);
        ((TextView) findViewById(R.id.mail)).setText(mailVal);
        ((TextView) findViewById(R.id.email)).setText(mailVal);
        ((TextView) findViewById(R.id.phone)).setText(phoneVal);
        ((TextView) findViewById(R.id.about_me)).setText(aboutMeVal);
        ((TextView) findViewById(R.id.location)).setText(locationVal);

        userDetails = new UserDetails();
        userDetails.setUserAlias(userAlias);
        userDetails.setUsername(mailVal);
        userDetails.setPhone(phoneVal);
        userDetails.setLocation(locationVal);
        userDetails.setAboutMe(aboutMeVal);

        findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DialogFragment editProfile = new EditProfileDialog();
                editProfile.show(getFragmentManager(), "Edit Profile");
            }
        });
    }

    @Override
    public UserDetails getUserDetails() {
        return userDetails;
    }

    @Override
    public void onSaved(UserDetails userDetails) {
        new SaveProfileChanges().execute(userDetails);
    }

    private class SaveUserDetailsTask extends AsyncTask<Bitmap, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ProfileActivity.this);
            dialog.setMessage("Saving your data");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            if (null != bitmaps[0]) {
                try {
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
            return null;
        }

        @Override protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }

    /**
     *
     */
    public class SaveProfileChanges extends AsyncTask<UserDetails, Void, Integer> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Saving your changes");
            progressDialog.show();
        }

        /**
         * Override this method to perform a computation on a background thread. The specified
         * parameters are the parameters passed to {@link #execute} by the caller of this task.
         *
         * This method can call {@link #publishProgress} to publish updates on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Integer doInBackground(UserDetails... params) {
            UserDetailsManager manager = new UserDetailsManager(ProfileActivity.this);
            return manager.updateUserDetails(params[0], username);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (null != progressDialog) {
                progressDialog.dismiss();
            }
            if (integer > 0) {
                ToastUtils.createToast(ProfileActivity.this, "Profile changed successfully", null);
                populateFeilds();
            }
        }
    }
}
