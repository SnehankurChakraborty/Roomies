package com.phaseii.rxm.roomies.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.AddExpenseDialog;
import com.phaseii.rxm.roomies.fragments.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.view.BannerView;
import com.phaseii.rxm.roomies.view.RoomiesPagerAdapter;
import com.phaseii.rxm.roomies.view.RoomiesRecyclerViewAdapter;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
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
	SharedPreferences mSharedPref;
	RoomiesPagerAdapter adapter;
	RoomiesSlidingTabLayout tabs;
	CharSequence Titles[] = {"Budget Report", "Expense Report"};
	int Numboftabs = 2;
	String drawerTitles[] = {"Home", "Savings", "Exceeds", "Profile", "Logout"};
	int drawerIcons[] = {R.drawable.ic_home,
			R.drawable.ic_savings_bank,
			R.drawable.ic_coins,
			R.drawable.ic_profile,
			R.drawable.ic_logout};
	String name = "Snehankur Chakr";
	String email = "snehankurchakraborty@gmail.com";
	int profile = R.drawable.ic_percent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mSharedPref=getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		boolean isLoggedIn = mSharedPref.getBoolean(IS_LOGGED_IN, false);
		if (!isLoggedIn) {
			try {
				startActivityHelper(this,
						getResources().getString(R.string.GetStartedWizard), null);
			} catch (RoomXpnseMngrException e) {
				createToast(this, APP_ERROR, mToast);
				System.exit(0);
			}
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		mSharedPref = getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		mtoolbar = (Toolbar) findViewById(R.id.toolbar);
		mtoolbar.setTitle("");
		if (mtoolbar != null) {
			setSupportActionBar(mtoolbar);
		}
		BannerView title = (BannerView) findViewById(R.id.toolbartitle);
		title.setText(" " + mSharedPref.getString(ROOM_ALIAS, "Room") + " ");
		ImageView addExpenseButton = (ImageView) findViewById(R.id.addexpense);
		addExpenseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment dialog = new AddExpenseDialog();
				dialog.show(getSupportFragmentManager(), "addexpense");
			}
		});

		RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
		mRecyclerView.hasFixedSize();
		RecyclerView.Adapter mRecylerAdapter = new RoomiesRecyclerViewAdapter(drawerTitles,
				drawerIcons, name, email, profile);
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
		adapter = new RoomiesPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		tabs = (RoomiesSlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true);
		tabs.setCustomTabColorizer(new RoomiesSlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.material_deep_teal_200);
			}
		});
		tabs.setViewPager(pager);

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

	private class DrawerItemClickListener
			implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		}
	}
}
