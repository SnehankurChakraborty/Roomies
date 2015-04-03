package com.phaseii.rxm.roomies.activity;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.CurrentRoomStatusFragment;
import com.phaseii.rxm.roomies.view.RoomiesPagerAdapter;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;

public class HomeScreenActivity extends ActionBarActivity
		implements CurrentRoomStatusFragment.OnFragmentInteractionListener {

/*	FragmentManager mFragmentManager;
	FragmentTransaction mTransaction;
	Fragment mFragment;*/
	ActionBarDrawerToggle mDrawerTogggle;
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	Toolbar mtoolbar;
	ViewPager pager;
	RoomiesPagerAdapter adapter;
	RoomiesSlidingTabLayout tabs;
	CharSequence Titles[] = {"Home", "Events"};
	int Numboftabs = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		/*
		* Initializing fragment
		* */

		/*if (savedInstanceState == null) {
			mFragment = CurrentRoomStatusFragment.getInstance();
			mFragmentManager = getSupportFragmentManager();
			mTransaction = mFragmentManager.beginTransaction();
			mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			mTransaction.add(R.id.home_screen_content_frame, mFragment).commit();
		}
*/
		/*
		* Initializing navigation drawer
		* */
		String[] mDrawerOptions = getResources().getStringArray(R.array.home_screen_drawer_options);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.home_screen_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawerOptions));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mtoolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mtoolbar != null) {
			setSupportActionBar(mtoolbar);
		}
		mDrawerTogggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar
				, R.string.open_drawer, R.string.close_drawer) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerTogggle);
		adapter = new RoomiesPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		// Assiging the Sliding Tab Layout View
		tabs = (RoomiesSlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(
				true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new RoomiesSlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.material_deep_teal_200);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
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


/*	*//* Called whenever we call invalidateOptionsMenu() *//*
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}*/


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
