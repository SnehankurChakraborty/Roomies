package com.phaseii.rxm.roomies.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.AddExpenseDialog;
import com.phaseii.rxm.roomies.fragments.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.TrendFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.service.UserService;
import com.phaseii.rxm.roomies.service.UserServiceImpl;
import com.phaseii.rxm.roomies.view.BannerView;
import com.phaseii.rxm.roomies.view.RoomiesRecyclerHomeViewAdapter;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.HOME_FRAGMENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.createToast;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.startActivityHelper;

public class HomeScreenActivity extends ActionBarActivity
		implements CurrentBudgetStatus.OnFragmentInteractionListener {


	Toast mToast;
	ActionBarDrawerToggle mDrawerTogggle;
	DrawerLayout mDrawerLayout;
	Toolbar mtoolbar;
	ViewPager pager;
	BannerView title;
	SharedPreferences mSharedPref;
	RecyclerView.Adapter mRecylerAdapter;
	FragmentTransaction transaction;
	String drawerTitles[] = {"Home", "Trends", "Savings", "Profile", "Logout"};
	int drawerIcons[] = {R.drawable.ic_home,
			R.drawable.ic_trend,
			R.drawable.ic_savings_bank,
			R.drawable.ic_profile,
			R.drawable.ic_logout};
	String name;
	String email;
	int profile = R.drawable.ic_camera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			if (mtoolbar != null) {
				setSupportActionBar(mtoolbar);
			}
			title = (BannerView) findViewById(R.id.toolbartitle);
			title.setText(" " + getSharedPreferences(RoomiesConstants
					.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).
					getString(ROOM_ALIAS, "Room") + " ");
			ImageView addExpenseButton = (ImageView) findViewById(R.id.addexpense);
			addExpenseButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					DialogFragment dialog = AddExpenseDialog.getInstance(R.id.pager);
					dialog.show(getSupportFragmentManager(), "addexpense");
				}

			});

			/*Setting the recycler view for navigation drawer*/

			RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
			mRecyclerView.hasFixedSize();
			mRecylerAdapter = new RoomiesRecyclerHomeViewAdapter(drawerTitles,
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_home_screen, menu);
		return true;
	}

	@Override
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
	}

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

	public void nextFragment(Fragment fragment, String tag) {
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.home_screen_fragment_layout, fragment, tag);
		transaction.commit();
		if (fragment instanceof TrendFragment) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			mtoolbar.setLayoutParams(params);
			mtoolbar.setTitle("Roomies");
			title.setVisibility(View.INVISIBLE);
		}else{
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 400);
			mtoolbar.setLayoutParams(params);
			mtoolbar.setTitle("");
			title.setVisibility(View.VISIBLE);
		}
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}

	public void checkSetupCompleted(String username) {
		UserService user = new UserServiceImpl(this);
		if (!user.isSetupCompleted(username)) {
			try {
				startActivityHelper(this, getResources().getString(R.string
						.GetStartedWizard), null, true);
			} catch (RoomXpnseMngrException e) {
				createToast(this, APP_ERROR, mToast);
			}
		}
	}

	public void updateProfilePic(Bitmap profilePic, int profile) {
		View headerView = ((RoomiesRecyclerHomeViewAdapter) mRecylerAdapter).getHeaderView();
		ImageView profileFrame = (ImageView) headerView.findViewById(R.id.profileFrame);
		if (null != profilePic) {
			profileFrame.setImageBitmap(profilePic);
		} else {
			profileFrame.setImageResource(profile);
		}
	}
}
