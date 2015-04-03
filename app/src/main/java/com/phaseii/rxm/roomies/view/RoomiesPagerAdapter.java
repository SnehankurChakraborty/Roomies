package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.phaseii.rxm.roomies.fragments.CurrentRoomStatusFragment;
import com.phaseii.rxm.roomies.fragments.Tab2;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class RoomiesPagerAdapter extends FragmentStatePagerAdapter {

	CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
	int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

	public RoomiesPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
		super(fm);
		this.Titles = mTitles;
		this.NumbOfTabs = mNumbOfTabsumb;
	}

	@Override
	public Fragment getItem(int position) {

		if(position == 0) // if the position is 0 we are returning the First tab
		{
			CurrentRoomStatusFragment tab1 = new CurrentRoomStatusFragment();
			return tab1;
		}
		else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
		{
			Tab2 tab2 = new Tab2();
			return tab2;
		}


	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	// This method return the Number of tabs for the tabs Strip

	@Override
	public int getCount() {
		return NumbOfTabs;
	}
}
