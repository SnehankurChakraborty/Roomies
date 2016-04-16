package com.phaseii.rxm.roomies.activity;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.factory.ViewBuilderFactory;
import com.phaseii.rxm.roomies.google.ConnectClient;
import com.phaseii.rxm.roomies.service.RoomUserStatManager;
import com.phaseii.rxm.roomies.ui.customviews.BannerView;
import com.phaseii.rxm.roomies.ui.customviews.RoomiesNavigationDrawerAdapter;
import com.phaseii.rxm.roomies.ui.dialogs.AddExpenseDialog;
import com.phaseii.rxm.roomies.ui.fragments.BlankFragment;
import com.phaseii.rxm.roomies.ui.fragments.HomeFragment;
import com.phaseii.rxm.roomies.utils.ActivityUtils;
import com.phaseii.rxm.roomies.utils.Category;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.utils.Constants.APP_ERROR;
import static com.phaseii.rxm.roomies.utils.Constants.HOME_FRAGMENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOM_ALIAS;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USERNAME;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PRESS_BACK_AGAIN_TO_EXIT;

/**
 * @author Snehankur
 * @since 23/03/2015 <p/> This is the launcher activity for the application.
 */
public class HomeScreenActivity extends RoomiesBaseActivity
        implements AddExpenseDialog.OnSubmitListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "HomeScreenActivity";
    private static ArrayList<RoomExpenses> roomExpensesList;
    private static ArrayList<RoomStats> roomStatsList;
    private Toast mToast;
    private ViewPager pager;
    private RecyclerView.Adapter mRecylerAdapter;
    private boolean doubleBackToExitPressedOnce;
    private ActionBarDrawerToggle mDrawerTogggle;
    private DrawerLayout mDrawerLayout;
    private FrameLayout frameLayout;
    private int currentapiVersion;
    private FloatingActionButton fabButton;
    private FloatingActionButton addExpenseButton;
    private FloatingActionButton addRoomButton;
    private FloatingActionButton addRoomiesButton;
    private TextView addExpenseLabel;
    private TextView addRoomiesLabel;
    private TextView addRoomLabel;
    private ConnectClient connectClient;

    /**
     * on create
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void configureView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home_screen);
        connectClient = ConnectClient.getInstance(this);
        connectClient.loginGPlus();
        builder = ViewBuilderFactory.newInstance(HomeScreenActivity.this)
                .newViewBuilder();

        if (getIntent()
                .getBooleanExtra(ActivityUtils.Extras.IS_ROOM_DATA_UPDATED.getValue(), false)) {
            roomExpensesList = getIntent().getParcelableArrayListExtra
                    (ActivityUtils.Extras.ROOM_EXPENSES.getValue());
            roomStatsList = getIntent().getParcelableArrayListExtra(ActivityUtils
                    .Extras.ROOM_STATS.getValue());
        }

        /**
         * Setup toolbar
         */
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle("");
        setSupportActionBar(mtoolbar);

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
                    getResources().getColor(R.color.primary_dark));
        }

        /**
         * set room alias as title
         */
        BannerView title = (BannerView) builder.getView(R.id.toolbartitle);
        String roomAlias = " " + getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE)
                .getString(PREF_ROOM_ALIAS, "Roomies") + " ";
        title.setText(roomAlias);
        title.setTextColor(Color.WHITE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_screen_drawer_layout);
        mDrawerTogggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mtoolbar, R.string.open_drawer, R.string.close_drawer) {
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
         * Setup FAB button
         */
        setupFAB();

        /**
         * load home fragment
         */
        if (savedInstanceState == null) {
            setUpFragments(null);
        }

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

        String drawerTitles[] = {"Home", "Roommates", "Manage Rooms", "Profile", "Share", "Rate",
                "Contact me",
                "Help Improve", "Logout"};
        int drawerIcons[] = {R.drawable.ic_home,
                R.drawable.ic_friends_white,
                android.R.drawable.ic_menu_search,
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
        fabButton = (FloatingActionButton) builder.getView(R.id.fab);
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fabButton
                .getLayoutParams();
        frameLayout = (FrameLayout) findViewById(R.id.fab_layout);
        addExpenseButton = (FloatingActionButton) builder.getView(R.id.add_expense);
        addRoomButton = (FloatingActionButton) builder.getView(R.id.add_room);
        addRoomiesButton = (FloatingActionButton) builder.getView(R.id.add_roomies);
        addExpenseLabel = builder.getTextView(R.id.add_expense_label);
        addRoomiesLabel = builder.getTextView(R.id.add_roomies_label);
        addRoomLabel = builder.getTextView(R.id.add_room_label);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameLayout.getVisibility() == View.INVISIBLE) {
                    animateUpFAB(params);
                } else {
                    animateDownFAB();
                }
            }
        });


        addExpenseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog = AddExpenseDialog.getInstance(HomeScreenActivity.this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                dialog.show(transaction, "dialog");
            }
        });

        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ActivityUtils.startActivityHelper(HomeScreenActivity.this,
                            getString(R.string.AddRoomActivity), null, false, false);
                } catch (RoomXpnseMngrException e) {
                    e.printStackTrace();
                }
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    animateDownFAB();
                }
            }
        });
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
    public void signoutGplus() {
        connectClient.signoutGplus();
    }

    /**
     * revoke google plus access to the user for this particular app
     */
    public void revokeGplusAccess() {
        connectClient.revokeGplusAccess();
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
            builder.getTextView(R.id.toolbartitle).setVisibility(View.VISIBLE);
            super.onBackPressed();
        } else {
            if (frameLayout.getVisibility() == View.VISIBLE) {
                animateDownFAB();
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

    public void setUpFragments(RoomExpenses expense) {
        Map<ActivityUtils.Extras, List<? extends Parcelable>> fragmentArgsMap = new HashMap<>();
        if (null != roomExpensesList && null != expense) {
            roomExpensesList.add(expense);
        }
        if (null != roomStatsList && null != expense) {
            String month = DateUtils.getCurrentMonthYear();
            Category category = Category.getCategory(expense.getExpenseCategory());
            for (RoomStats roomStats : roomStatsList) {
                if (month.equals(roomStats.getMonthYear())) {
                    int spent = 0;
                    if (category.equals(Category.RENT)) {
                        spent = roomStats.getRentSpent();
                        roomStats.setRentSpent(spent + (int) expense.getAmount());
                    } else if (category.equals(Category.MAID)) {
                        spent = roomStats.getMaidSpent();
                        roomStats.setMaidSpent(spent + (int) expense.getAmount());
                    } else if (category.equals(Category.ELECTRICITY)) {
                        spent = roomStats.getElectricitySpent();
                        roomStats.setElectricitySpent(spent + (int) expense.getAmount());
                    } else if (category.equals(Category.MISCELLANEOUS)) {
                        spent = roomStats.getMiscellaneousSpent();
                        roomStats.setMiscellaneousSpent(spent + (int) expense.getAmount());
                    }
                }
            }
        }
        Bundle fragmentBundle = new Bundle();
        fragmentArgsMap.put(ActivityUtils.Extras.ROOM_STATS, roomStatsList);
        fragmentArgsMap.put(ActivityUtils.Extras.ROOM_EXPENSES, roomExpensesList);
        fragmentBundle.putParcelableArrayList(ActivityUtils.Extras.ROOM_EXPENSES
                .getValue(), roomExpensesList);
        fragmentBundle.putParcelableArrayList(ActivityUtils.Extras.ROOM_STATS
                .getValue(), roomStatsList);

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
        setupNavigationDrawer(fragmentArgsMap, name, email, null != roomId);
        /**
         * Setup home fragment
         * */
        if (null != getSharedPreferences(PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE).getString(Constants.PREF_ROOM_ID, null)) {
            HomeFragment fragment = new HomeFragment();
            fragment.setParceableBundle(fragmentBundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.add(R.id.home_screen_fragment_layout, fragment, HOME_FRAGMENT).commit();
        } else {
            addExpenseButton.setVisibility(View.GONE);
            addRoomiesButton.setVisibility(View.GONE);
            addExpenseLabel.setVisibility(View.GONE);
            addRoomiesLabel.setVisibility(View.GONE);
            BlankFragment fragment = new BlankFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.add(R.id.home_screen_fragment_layout, fragment, HOME_FRAGMENT).commit();
        }

    }

    @Override
    public void onSubmit(RoomExpenses expense) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment frag : fragments) {
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }
        setUpFragments(expense);
        animateDownFAB();
    }

    private void animateUpFAB(ViewGroup.MarginLayoutParams params) {
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = frameLayout.getWidth() - (fabButton.getWidth() / 2 + params.rightMargin);
            int cy = frameLayout.getHeight() - (fabButton.getHeight() / 2 + params.bottomMargin);
            int finalRadius = Math.max(frameLayout.getWidth(), frameLayout.getHeight());
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy, 0, finalRadius);
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
        RotateAnimation rotate = new RotateAnimation(0f, -135f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // prevents View from restoring to original direction.
        rotate.setDuration(200);
        rotate.setFillAfter(true);
        fabButton.startAnimation(rotate);
        ScaleAnimation scale = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1f);
        scale.setDuration(300);
        TranslateAnimation slide = new TranslateAnimation(300, -20, 0, 0);
        slide.setDuration(300);
        addExpenseButton.startAnimation(scale);
        addRoomiesButton.startAnimation(scale);
        addRoomButton.startAnimation(scale);
        addExpenseLabel.startAnimation(slide);
        addRoomiesLabel.startAnimation(slide);
        addRoomLabel.startAnimation(slide);
    }

    private void animateDownFAB() {
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fabButton
                    .getLayoutParams();
            int cx = frameLayout.getWidth() - (fabButton.getWidth() / 2 + params.rightMargin);
            int cy = frameLayout.getHeight() - (fabButton.getHeight() / 2 + params.bottomMargin);
            int initialRadius = frameLayout.getWidth();
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(frameLayout, cx, cy, initialRadius, 0);
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
        RotateAnimation rotate = new RotateAnimation(-135f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // prevents View from restoring to original direction.
        rotate.setDuration(200);
        rotate.setFillAfter(true);
        fabButton.startAnimation(rotate);
    }

    public void deleteRoomDetails() {
        new DeleteRoomDetails()
                .execute(Integer.parseInt(getSharedPreferences(PREF_ROOMIES_KEY, MODE_PRIVATE)
                        .getString(PREF_USER_ID, "0")));
    }

    private class DeleteRoomDetails extends AsyncTask<Integer, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(HomeScreenActivity.this);
            dialog.setMessage("Deleting room data");
            dialog.show();
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
        protected Boolean doInBackground(Integer... params) {
            RoomUserStatManager manager = new RoomUserStatManager(HomeScreenActivity.this);
            return manager.deleteRoomDetails(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean deleted) {
            if (null != dialog) {
                dialog.dismiss();
            }
            if (!deleted) {
                ToastUtils.createToast(HomeScreenActivity.this,
                        "Room could not be deleted due to technical error", mToast);
            }
            SharedPreferences mSharedPref = getSharedPreferences(
                    PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.clear();
            mEditor.apply();
            try {
                revokeGplusAccess();
                ActivityUtils.startActivityHelper(HomeScreenActivity.this,
                        getResources()
                                .getString(R.string.LoginActivity), null,
                        true, false);

            } catch (RoomXpnseMngrException e) {
                ToastUtils.createToast(HomeScreenActivity.this, APP_ERROR, mToast);
                System.exit(0);
            }
        }
    }
}
