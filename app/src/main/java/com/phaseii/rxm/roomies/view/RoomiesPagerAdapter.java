package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.phaseii.rxm.roomies.fragments.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.fragments.CurrentExpenseReport;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;

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
		RoomiesFragment tab;
		if (position == 0) {
			tab = CurrentBudgetStatus.getInstance();
		} else {
			tab = CurrentExpenseReport.getInstance();
		}
		return tab;
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
