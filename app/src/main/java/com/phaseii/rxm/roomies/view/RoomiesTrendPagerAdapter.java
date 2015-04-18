package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.tabs.CurrentExpenseReport;
import com.phaseii.rxm.roomies.tabs.DetailExpenseTab;
import com.phaseii.rxm.roomies.tabs.ExpenseSummaryTab;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class RoomiesTrendPagerAdapter extends FragmentStatePagerAdapter {
	CharSequence titles[];
	int numbOfTabs;
	private Map<Integer, String> mTags = new HashMap<>();

	public RoomiesTrendPagerAdapter(FragmentManager fm, CharSequence mTitles[],
	                                int mNumbOfTabs) {
		super(fm);
		this.titles = mTitles;
		this.numbOfTabs = mNumbOfTabs;
	}

	@Override
	public Fragment getItem(int position) {
		RoomiesFragment tab;
		if (position == 0) {
			tab = DetailExpenseTab.getInstance();
			String tag = tab.getTag();
			mTags.put(position, tag);

		} else {
			tab = ExpenseSummaryTab.getInstance();
			String tag = tab.getTag();
			mTags.put(position, tag);
		}
		return tab;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return numbOfTabs;
	}
}

