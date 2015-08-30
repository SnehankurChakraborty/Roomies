package com.phaseii.rxm.roomies.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dialogs.AddExpenseDialog;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.DashboardFragment;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.StatsFragment;
import com.phaseii.rxm.roomies.gcm.GCMSender;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.RoomiesHelper;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.tabs.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.view.BannerView;
import com.phaseii.rxm.roomies.view.RoomiesNavDrawerViewAdapter;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.HOME_FRAGMENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOM_ALIAS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_SETUP_COMPLETED;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_USERNAME;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PRESS_BACK_AGAIN_TO_EXIT;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.createToast;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.startActivityHelper;

/**
 * @author Snehankur
 * @since 23/03/2015
 * <p/>
 * This is the launcher activity for the application.
 */
public class HomeScreenActivity extends RoomiesBaseActivity
        implements CurrentBudgetStatus.OnFragmentInteractionListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "HomeScreenActivity";
    int profile = R.drawable.ic_camera;
    private Toast mToast;
    private ActionBarDrawerToggle mDrawerTogggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mtoolbar;
    private ViewPager pager;
    private BannerView title;
    private SharedPreferences mSharedPref;
    private RecyclerView.Adapter mRecylerAdapter;
    private FragmentTransaction transaction;
    private String drawerTitles[] = {"Home", "Dashboard", "Stats", "Profile", "Logout"};
    private int drawerIcons[] = {R.drawable.ic_home,
            R.drawable.ic_trend,
            R.drawable.ic_savings_bank,
            R.drawable.ic_profile,
            R.drawable.ic_logout};
    private Bitmap bitmap;
    private String name;
    private String email;
    private ImageView addExpenseButton;
    private ImageView fabButton;
    private ImageView fabButtonAlt;
    private ImageView addRoomiesButton;
    private FrameLayout frameLayout;
    private int currentapiVersion;
    private boolean doubleBackToExitPressedOnce;
    private int mShortAnimationDuration;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    /**
     * on create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * Get the current api version the app is running on
         */
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        mSharedPref = getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE);

        /**
         * Check if the user is logged in
         */
        boolean isLoggedIn = mSharedPref.getBoolean(IS_LOGGED_IN, false);
        if (!isLoggedIn) {
            try {

                /**
                 * If the user is not logged in, send him to the LoginActivity
                 */
                startActivityHelper(this, getResources().getString(R.string.LoginActivity),
                        null, true);
            } catch (RoomXpnseMngrException e) {
                createToast(this, APP_ERROR, mToast);
                System.exit(0);
            }
        } else {
            /**
             * Checks if the user has completed the initial setup required to run the app and
             * sends then user to {@link com.phaseii.rxm.roomies.activity.GetStartedWizard
             * GetStartedWizard} if the setup is required.
             */
            if (!mSharedPref.getBoolean(PREF_SETUP_COMPLETED, false)) {
                try {
                    startActivityHelper(this, getResources().getString(R.string.GetStartedWizard),
                            null, true);
                } catch (RoomXpnseMngrException e) {
                    createToast(this, APP_ERROR, mToast);
                    System.exit(0);
                }
            } else {

                RoomiesHelper.setupAlarm(HomeScreenActivity.this);


                setContentView(R.layout.activity_home_screen);
                /**
                 * connect to Google plus
                 */
                mGoogleApiClient.connect();

                /**
                 * load details of the user currently logged in
                 */
                mSharedPref = getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
                name = mSharedPref.getString(PREF_USER_ALIAS, null);
                email = mSharedPref.getString(PREF_USERNAME, null);

                /**
                 * start the {@link com.phaseii.rxm.roomies.fragments.HomeFragment homefragment}
                 */
                if (savedInstanceState == null) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    transaction.add(R.id.home_screen_fragment_layout, new HomeFragment(),
                            HOME_FRAGMENT).commit();
                }

                /**
                 * Setup toolbar
                 */
                mtoolbar = (Toolbar) findViewById(R.id.toolbar);
                mtoolbar.setTitle("");
                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                int px = Math.round(8 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {

                    /**
                     * since the method is currently available only for the devices running {@link
                     * android.os.Build.VERSION_CODES.LOLLIPOP LOLLIPOP}, checking if the api version is
                     * greater than or equal to LOLLIPOP.
                     */
                    mtoolbar.setElevation(px);
                }
                if (mtoolbar != null) {
                    setSupportActionBar(mtoolbar);
                }

                /**
                 * set room alias as title
                 */
                title = (BannerView) findViewById(R.id.toolbartitle);
                title.setText(" " + getSharedPreferences(PREF_ROOMIES_KEY,
                        Context.MODE_PRIVATE).getString(PREF_ROOM_ALIAS, "Roomies") + " ");
                mShortAnimationDuration = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);

                /**
                 * Setup FAB button
                 */
                setupFAB();

                /**
                 * Setup Navigation Drawer
                 */
                setupNavigationDrawer();

                /**
                 * Register for google cloud messaging
                 */
                /*if (checkPlayServices()) {
                    // Start IntentService to register this application with GCM.
					Intent intent = new Intent(this, RegistrationIntentService.class);
					startService(intent);
				}*/

            }
        }
    }

    /**
     * check if the version of play services required to run the application present in the device
     *
     * @return true if the play services are present.
     */

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * sets up navigation drawer
     */
    private void setupNavigationDrawer() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.hasFixedSize();
        mRecylerAdapter = new RoomiesNavDrawerViewAdapter(drawerTitles,
                drawerIcons, name, email, profile, this);
        mRecyclerView.setAdapter(mRecylerAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_screen_drawer_layout);
        mDrawerTogggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar
                , R.string.open_drawer, R.string.close_drawer) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerTogggle);
        mDrawerTogggle.syncState();
    }

    /**
     * Sets up FAB button
     */
    private void setupFAB() {
        fabButton = (ImageView) findViewById(R.id.fab);
        addRoomiesButton = (ImageView) findViewById(R.id.add_roomies);
        frameLayout = (FrameLayout) findViewById(R.id.fab_layout);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayout.getVisibility() == View.GONE) {
                    frameLayout.setVisibility(View.VISIBLE);
                    fabButton.setVisibility(View.GONE);
                }
            }
        });

        fabButtonAlt = (ImageView) findViewById(R.id.fab_alt);
        fabButtonAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    frameLayout.setAlpha(1);
                    fabButton.setVisibility(View.VISIBLE);
                }
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
                fabButton.setVisibility(View.VISIBLE);
            }
        });

        addExpenseButton = (ImageView) findViewById(R.id.add_expense);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog = AddExpenseDialog.getInstance(R.id.pager);
                dialog.show(getSupportFragmentManager(), "addexpense");
                frameLayout.setVisibility(View.GONE);
                fabButton.setVisibility(View.VISIBLE);
            }

        });

        addRoomiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
                new GCMSender().execute("Hello", RoomiesConstants.getToken());
            }
        });
    }

    @Override
    public void getProfileInformation(LoginResult loginResult) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerTogggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerTogggle.onConfigurationChanged(newConfig);
    }

    /**
     * sign out from google plus
     */
    @Override
    public void signoutGplus() {
        super.signoutGplus();
    }

    /**
     * revoke google plus access to the user for this particular app
     */
    @Override
    public void revokeGplusAccess() {
        super.revokeGplusAccess();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public ViewPager getViewPager() {
        return pager;
    }

    /**
     * Loads new fragment into the home screen activity
     *
     * @param fragment
     * @param tag
     */
    public void nextFragment(Fragment fragment, String tag) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_screen_fragment_layout, fragment, tag);
        transaction.commit();

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            /**
             * since the method is currently available only for the devices running {@link
             * android.os.Build.VERSION_CODES.LOLLIPOP LOLLIPOP}, checking if the api version
             * is greater than or equal to LOLLIPOP.
             */
            mtoolbar.setElevation(0);
        }
        if (!(fragment instanceof HomeFragment)) {
            title.setVisibility(View.INVISIBLE);
            addExpenseButton.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            mtoolbar.setLayoutParams(params);
            if (fragment instanceof DashboardFragment) {
                mtoolbar.setTitle("Dashboard");
                addExpenseButton.setVisibility(View.VISIBLE);
                DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
                int px = Math.round(8 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    mtoolbar.setElevation(px);
                }
            } else if (fragment instanceof StatsFragment) {
                mtoolbar.setTitle("Stats");
            }

        } else {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                    getResources().getDisplayMetrics());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, height);
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            int px = Math.round(8 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                mtoolbar.setElevation(px);
            }
            mtoolbar.setLayoutParams(params);
            mtoolbar.setTitle("");
            title.setVisibility(View.VISIBLE);
            addExpenseButton.setVisibility(View.VISIBLE);

        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    /**
     * Update the profile pic in the navigation drawer
     *
     * @param profilePicBitmap
     */
    public void updateProfilePic(Bitmap profilePicBitmap) {
        View headerView = ((RoomiesNavDrawerViewAdapter) mRecylerAdapter).getHeaderView();
        ImageView profileFrame = (ImageView) headerView.findViewById(R.id.profileFrame);
        if (null != profilePicBitmap) {
            profileFrame.setImageBitmap(profilePicBitmap);
        }
    }

    /**
     * Change what happens when back is pressed
     */
    @Override
    public void onBackPressed() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            createToast(this, PRESS_BACK_AGAIN_TO_EXIT, mToast);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void getAllDetails(UserDetails userDetails, boolean isGoogleFBlogin) {

    }

    private class DrawerItemClickListener
            implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
}
