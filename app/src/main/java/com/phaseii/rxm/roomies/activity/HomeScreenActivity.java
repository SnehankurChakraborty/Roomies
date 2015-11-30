package com.phaseii.rxm.roomies.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dialogs.AddExpenseDialog;
import com.phaseii.rxm.roomies.fragments.BlankFragment;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.ToastUtils;
import com.phaseii.rxm.roomies.view.BannerView;
import com.phaseii.rxm.roomies.view.RoomiesNavigationDrawerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.HOME_FRAGMENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOM_ALIAS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_USERNAME;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PRESS_BACK_AGAIN_TO_EXIT;

/**
 * @author Snehankur
 * @since 23/03/2015
 * <p/>
 * This is the launcher activity for the application.
 */
public class HomeScreenActivity extends RoomiesBaseActivity implements AddExpenseDialog
                                                                               .OnSubmitListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "HomeScreenActivity";
    private Toast mToast;
    private ViewPager pager;
    private RecyclerView.Adapter mRecylerAdapter;
    private boolean doubleBackToExitPressedOnce;
    private ActionBarDrawerToggle mDrawerTogggle;
    private DrawerLayout mDrawerLayout;
    private FrameLayout frameLayout;
    private int currentapiVersion;

    /**
     * on create
     *
     * @param savedInstanceState
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.show();

        Map<ActivityUtils.Extras, List<? extends Parcelable>> fragmentArgsMap = new HashMap<>();
        List<RoomExpenses> roomExpensesList = getIntent().getParcelableArrayListExtra
                (ActivityUtils.Extras.ROOM_EXPENSES.getValue());
        List<RoomStats> roomStatsList = getIntent().getParcelableArrayListExtra(ActivityUtils
                                                                                        .Extras
                                                                                        .ROOM_STATS.getValue());
        if (null != roomExpensesList) {
            fragmentArgsMap.put(ActivityUtils.Extras.ROOM_EXPENSES, roomExpensesList);
        }
        if (null != roomStatsList) {
            fragmentArgsMap.put(ActivityUtils.Extras.ROOM_STATS, roomStatsList);
        }

        /**
         * load details of the user currently logged in
         */
        String name = getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE).getString
                (PREF_USER_ALIAS, null);
        String email = getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE).getString
                (PREF_USERNAME, null);
        String roomId = getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE).getString
                (PREF_ROOM_ID, null);

        /**
         * Setup Navigation Drawer
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_screen_drawer_layout);
        setupNavigationDrawer(fragmentArgsMap, name, email, null != roomId);
        /**
         * Setup toolbar
         */
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle("");

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {

            /**
             * since the method is currently available only for the devices running
             * {@link
             * android.os.Build.VERSION_CODES.LOLLIPOP LOLLIPOP}, checking if the api
             * version is
             * greater than or equal to LOLLIPOP.
             */
            getWindow().setNavigationBarColor(
                    getResources().getColor(R.color.primary_dark_home));
        }
        if (mtoolbar != null) {
            setSupportActionBar(mtoolbar);
        }
        /**
         * set room alias as title
         */
        BannerView title = (BannerView) findViewById(R.id.toolbartitle);
        title.setText(" " + getSharedPreferences(PREF_ROOMIES_KEY,
                                                 Context.MODE_PRIVATE).getString(PREF_ROOM_ALIAS,
                                                                                 "Roomies") + " ");
        title.setTextColor(Color.WHITE);


        mDrawerTogggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                                                   mtoolbar
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

        /**
         * connect to Google plus
         */
        mGoogleApiClient.connect();


        /**
         * Setup FAB button
         */
        setupFAB();

        /**
         * load home fragment
         */
        if (savedInstanceState == null) {

            if (null != getSharedPreferences(PREF_ROOMIES_KEY, Context
                    .MODE_PRIVATE).getString(RoomiesConstants.PREF_ROOM_ID, null)) {
                Bundle fragmentBundle = new Bundle();
                if (null != fragmentArgsMap.get(ActivityUtils.Extras.ROOM_EXPENSES)) {
                    fragmentBundle.putParcelableArrayList(ActivityUtils.Extras.ROOM_EXPENSES
                                                                  .getValue(), (ArrayList)
                                                                  fragmentArgsMap.get
                                                                          (ActivityUtils.Extras
                                                                                   .ROOM_EXPENSES));
                }
                if (null != fragmentArgsMap.get(ActivityUtils.Extras.ROOM_STATS)) {
                    fragmentBundle.putParcelableArrayList(ActivityUtils.Extras.ROOM_STATS
                                                                  .getValue(), (ArrayList)
                                                                  fragmentArgsMap.get
                                                                          (ActivityUtils.Extras
                                                                                   .ROOM_STATS));
                }
                HomeFragment fragment = new HomeFragment();
                fragment.setParceableBundle(fragmentBundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.add(R.id.home_screen_fragment_layout, fragment, HOME_FRAGMENT).commit();
            } else {
                BlankFragment fragment = new BlankFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.add(R.id.home_screen_fragment_layout, fragment, HOME_FRAGMENT).commit();
            }
        }

        dialog.dismiss();
        /**
         * Register for google cloud messaging
         */
                /*if (checkPlayServices()) {
                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }*/

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
    private void setupNavigationDrawer(Map<ActivityUtils.Extras, List<? extends Parcelable>>
                                               fragmentArgsMap, String name, String email,
                                       boolean isRoomAdded) {

        String drawerTitles[] = {"Home", "Roommates", "Profile", "Share", "Rate", "Contact me",
                                 "Help Improve", "Logout"};
        int drawerIcons[] = {R.drawable.ic_home,
                             R.drawable.ic_friends_white,
                             R.drawable.ic_profile,
                             R.drawable.ic_share_white,
                             R.drawable.ic_play_store_logo,
                             R.drawable.ic_mail_white,
                             R.drawable.ic_idea_bulb,
                             R.drawable.ic_logout};
        int profile = R.drawable.ic_camera;
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecylerAdapter = new RoomiesNavigationDrawerAdapter(drawerTitles,
                                                             drawerIcons, name, email, profile,
                                                             this, mDrawerLayout,
                                                             fragmentArgsMap);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setAdapter(mRecylerAdapter);

    }

    /**
     * Sets up FAB button
     */
    private void setupFAB() {
        final ImageButton fabButton = (ImageButton) findViewById(R.id.fab);
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fabButton
                .getLayoutParams();
        frameLayout = (FrameLayout) findViewById(R.id.fab_layout);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayout.getVisibility() == View.INVISIBLE) {
                    if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                        int cx = frameLayout.getWidth() - (fabButton.getWidth() / 2 + params
                                .rightMargin);
                        int cy = frameLayout.getHeight() - (fabButton.getHeight() / 2 + params
                                .bottomMargin);
                        int finalRadius = Math.max(frameLayout.getWidth(), frameLayout.getHeight());
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy, 0,
                                                                        finalRadius);
                        anim.setDuration(200);
                        frameLayout.setVisibility(View.VISIBLE);
                        anim.start();
                    } else {
                        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
                        alpha.setDuration(200);
                        frameLayout.setAnimation(alpha);
                        alpha.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                frameLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                    RotateAnimation rotate = new RotateAnimation(0f, -135f,
                                                                 Animation.RELATIVE_TO_SELF,
                                                                 0.5f, Animation
                                                                         .RELATIVE_TO_SELF, 0.5f);

                    // prevents View from restoring to original direction.
                    rotate.setDuration(200);
                    rotate.setFillAfter(true);
                    fabButton.startAnimation(rotate);
                    ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f, Animation
                            .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
                    scale.setDuration(300);
                    TranslateAnimation slide = new TranslateAnimation(300, -20, 0, 0);
                    slide.setDuration(300);
                    findViewById(R.id.add_expense).startAnimation(scale);
                    findViewById(R.id.add_roomies).startAnimation(scale);
                    findViewById(R.id.add_roomies_label).startAnimation(slide);
                    findViewById(R.id.add_expense_label).startAnimation(slide);
                } else {
                    if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                        int cx = frameLayout.getWidth() - (fabButton.getWidth() / 2 + params
                                .rightMargin);
                        int cy = frameLayout.getHeight() - (fabButton.getHeight() / 2 + params
                                .bottomMargin);
                        int initialRadius = frameLayout.getWidth();
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy,
                                                                        initialRadius, 0);
                        anim.setDuration(200);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                frameLayout.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.start();
                    } else {
                        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
                        alpha.setDuration(200);
                        frameLayout.setAnimation(alpha);
                        alpha.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                frameLayout.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                    RotateAnimation rotate = new RotateAnimation(-135f, 0f,
                                                                 Animation.RELATIVE_TO_SELF,
                                                                 0.5f, Animation
                                                                         .RELATIVE_TO_SELF, 0.5f);

                    // prevents View from restoring to original direction.
                    rotate.setDuration(200);
                    rotate.setFillAfter(true);
                    fabButton.startAnimation(rotate);
                }
            }
        });

        ImageView addExpenseButton = (ImageView) findViewById(R.id.add_expense);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog = AddExpenseDialog.getInstance(HomeScreenActivity.this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                dialog.show(transaction,"dialog");
            }
        });


        ImageView addRoomiesButton = (ImageView) findViewById(R.id.add_roomies);
        /*addRoomiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
                new GCMSender().execute("Hello", RoomiesConstants.getToken());
            }
        });*/
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

    public ViewPager getViewPager() {
        return pager;
    }

    /**
     * Takes to play store for user rating
     */
    public void goToMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                                     Uri.parse(
                                             "http://play.google.com/store/apps/details?id=" +
                                             getPackageName())));
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"snehankurchakraborty@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Roomies");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                        "Checkout the new Roomies app on play store. It's awesome. " +
                        "http://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, "Share with"));
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    /**
     * Update the profile pic in the navigation drawer
     *
     * @param profilePicBitmap
     */
    public void updateProfilePic(Bitmap profilePicBitmap) {
        View headerView = ((RoomiesNavigationDrawerAdapter) mRecylerAdapter).getHeaderView();
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
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
            findViewById(R.id.toolbartitle).setVisibility(View.VISIBLE);
            super.onBackPressed();
        } else {

            if (frameLayout.getVisibility() == View.VISIBLE) {
                final ImageButton fabButton = (ImageButton) findViewById(R.id.fab);
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                            fabButton

                                    .getLayoutParams();
                    int cx = frameLayout.getWidth() - (fabButton.getWidth() / 2 + params
                            .rightMargin);
                    int cy = frameLayout.getHeight() - (fabButton.getHeight() / 2 + params
                            .bottomMargin);
                    int initialRadius = frameLayout.getWidth();
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy,
                                                                    initialRadius, 0);
                    anim.setDuration(200);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            frameLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
                    alpha.setDuration(200);
                    frameLayout.setAnimation(alpha);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            frameLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                RotateAnimation rotate = new RotateAnimation(-135f, 0f,
                                                             Animation.RELATIVE_TO_SELF, 0.5f,
                                                             Animation.RELATIVE_TO_SELF, 0.5f);

                // prevents View from restoring to original direction.
                rotate.setDuration(200);
                rotate.setFillAfter(true);
                fabButton.startAnimation(rotate);
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                ToastUtils.createToast(this, PRESS_BACK_AGAIN_TO_EXIT, mToast);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void getAllDetails(UserDetails userDetails, boolean isGoogleFBlogin) {

    }

    @Override
    public void onSubmit() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            final ImageButton fabButton = (ImageButton) findViewById(R.id.fab);
            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fabButton
                        .getLayoutParams();
                int cx = frameLayout.getWidth() - (fabButton.getWidth() / 2 + params
                        .rightMargin);
                int cy = frameLayout.getHeight() - (fabButton.getHeight() / 2 + params
                        .bottomMargin);
                int initialRadius = frameLayout.getWidth();
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy,
                                                                initialRadius, 0);
                anim.setDuration(200);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        frameLayout.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            } else {
                AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
                alpha.setDuration(200);
                frameLayout.setAnimation(alpha);
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        frameLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            RotateAnimation rotate = new RotateAnimation(-135f, 0f,
                                                         Animation.RELATIVE_TO_SELF, 0.5f,
                                                         Animation.RELATIVE_TO_SELF, 0.5f);

            // prevents View from restoring to original direction.
            rotate.setDuration(200);
            rotate.setFillAfter(true);
            fabButton.startAnimation(rotate);
        }
    }
}
