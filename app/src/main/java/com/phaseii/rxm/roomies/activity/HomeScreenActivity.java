package com.phaseii.rxm.roomies.activity;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.phaseii.rxm.roomies.gcm.RegistrationIntentService;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.service.UserService;
import com.phaseii.rxm.roomies.service.UserServiceImpl;
import com.phaseii.rxm.roomies.tabs.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.view.BannerView;
import com.phaseii.rxm.roomies.view.RoomiesRecyclerViewAdapter;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.HOME_FRAGMENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_SETUP_COMPLETED;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PRESS_BACK_AGAIN_TO_EXIT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.createToast;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.startActivityHelper;

public class HomeScreenActivity extends RoomiesBaseActivity
		implements CurrentBudgetStatus.OnFragmentInteractionListener {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "HomeScreenActivity";
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
	int profile = R.drawable.ic_camera;
	private int currentapiVersion;
	private boolean doubleBackToExitPressedOnce;
	private int mShortAnimationDuration;
	private BroadcastReceiver mRegistrationBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentapiVersion = android.os.Build.VERSION.SDK_INT;
		mSharedPref = getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		boolean isLoggedIn = mSharedPref.getBoolean(IS_LOGGED_IN, false);
		if (!isLoggedIn) {
			try {
				startActivityHelper(this,
						getResources().getString(R.string.LoginActivity), null, true);
			} catch (RoomXpnseMngrException e) {
				createToast(this, APP_ERROR, mToast);
				System.exit(0);
			}
		} else {
			setContentView(R.layout.activity_home_screen);
			mGoogleApiClient.connect();
			if (savedInstanceState == null) {
				transaction = getSupportFragmentManager().beginTransaction();
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
				transaction.add(R.id.home_screen_fragment_layout, new HomeFragment(),
						HOME_FRAGMENT).commit();
			}
			mSharedPref = getSharedPreferences(
					ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
			name = mSharedPref.getString(RoomiesConstants.NAME, null);
			email = mSharedPref.getString(RoomiesConstants.EMAIL_ID, null);
			checkSetupCompleted(name);
			mtoolbar = (Toolbar) findViewById(R.id.toolbar);
			mtoolbar.setTitle("");
			DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
			int px = Math.round(8 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
			if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
				mtoolbar.setElevation(px);
			}
			/*mtoolbar.setPadding(0, getStatusBarHeight(), 0, 0);*/
			if (mtoolbar != null) {
				setSupportActionBar(mtoolbar);
			}
			title = (BannerView) findViewById(R.id.toolbartitle);
			title.setText(" " + getSharedPreferences(RoomiesConstants
					.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).
					getString(ROOM_ALIAS, "Room") + " ");

			mShortAnimationDuration = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

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
					/*LinearLayout addRoomiesLayout = (LinearLayout) findViewById(R.id
							.add_roomies_layout);
					LinearLayout addExpenseLayout = (LinearLayout) findViewById(R.id
							.add_expense_layout);
					if (addRoomiesLayout.getVisibility() == View.GONE) {
						addRoomiesLayout.setVisibility(View.VISIBLE);
						fabButton.setVisibility(View.GONE);
						frameLayout.setVisibility(View.VISIBLE);
					} else {
						addRoomiesLayout.setVisibility(View.GONE);
						frameLayout.setVisibility(View.GONE);
					}
					if (addExpenseLayout.getVisibility() == View.GONE) {
						addExpenseLayout.setVisibility(View.VISIBLE);
						fabButton.setVisibility(View.GONE);
						frameLayout.setVisibility(View.VISIBLE);
					} else {
						addExpenseLayout.setVisibility(View.GONE);

						frameLayout.setVisibility(View.GONE);
					}*/

			});

			fabButtonAlt = (ImageView) findViewById(R.id.fab_alt);
			fabButtonAlt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (frameLayout.getVisibility() == View.VISIBLE) {
						frameLayout.setVisibility(View.GONE);
						frameLayout.setAlpha(100);
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
					/*try {
						RoomiesHelper.startActivityHelper(HomeScreenActivity.this,
								getResources().getString(R.string
										.RoomiesConnectActivity), null,false);
					} catch (RoomXpnseMngrException e) {
						RoomiesHelper.createToast(HomeScreenActivity.this, APP_ERROR, mToast);
						System.exit(0);
					}*/
					new GCMSender().execute(new String[]{"Hello",RoomiesConstants.getToken()});
				}
			});


			/*Setting the recycler view for navigation drawer*/

			RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
			mRecyclerView.hasFixedSize();
			mRecylerAdapter = new RoomiesRecyclerViewAdapter(drawerTitles,
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
			if (checkPlayServices()) {
				// Start IntentService to register this application with GCM.
				Intent intent = new Intent(this, RegistrationIntentService.class);
				startService(intent);
			}
		}
	}

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

	@Override
	public void setUpAuthenticatedUser(User user) throws RoomXpnseMngrException {

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

	@Override
	public void signoutGplus() {
		super.signoutGplus();
	}

	@Override
	public void revokeGplusAccess() {
		super.revokeGplusAccess();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_home_screen, menu);
		return true;
	}*/

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/

	@Override
	public void onFragmentInteraction(Uri uri) {

	}

	public ViewPager getViewPager() {
		return pager;
	}

	private class DrawerItemClickListener
			implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void nextFragment(Fragment fragment, String tag) {
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.home_screen_fragment_layout, fragment, tag);
		transaction.commit();

		if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
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

	public void checkSetupCompleted(String username) {
		UserService user = new UserServiceImpl(this);
		mSharedPref = getSharedPreferences(ROOM_INFO_FILE_KEY, MODE_PRIVATE);
		if (mSharedPref.getBoolean(IS_GOOGLE_FB_LOGIN, false)) {
			if (!mSharedPref.getBoolean(IS_SETUP_COMPLETED, false)) {
				try {
					startActivityHelper(this, getResources().getString(R.string
							.GetStartedWizard), null, true);
				} catch (RoomXpnseMngrException e) {
					createToast(this, APP_ERROR, mToast);
				}
			}
		} else {
			if (!user.isSetupCompleted(username)) {
				try {
					startActivityHelper(this, getResources().getString(R.string
							.GetStartedWizard), null, true);
				} catch (RoomXpnseMngrException e) {
					createToast(this, APP_ERROR, mToast);
				}
			}
		}
	}

	public void updateProfilePic(Bitmap profilePicBitmap) {
		View headerView = ((RoomiesRecyclerViewAdapter) mRecylerAdapter).getHeaderView();
		ImageView profileFrame = (ImageView) headerView.findViewById(R.id.profileFrame);
		if (null != profilePicBitmap) {
			profileFrame.setImageBitmap(profilePicBitmap);
		}
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

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
}
