package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.phaseii.rxm.roomies.tabs.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.tabs.CurrentExpenseReport;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;

import java.util.HashMap;
import java.util.Map;

import static com.phaseii.rxm.roomies.fragments.RoomiesFragment.*;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class RoomiesHomePagerAdapter extends FragmentStatePagerAdapter {

	CharSequence titles[];
	int numbOfTabs;
	private Map<Integer, String> mTags=new HashMap<>();

	public RoomiesHomePagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
		super(fm);
		this.titles = mTitles;
		this.numbOfTabs = mNumbOfTabsumb;
	}

	@Override
	public Fragment getItem(int position) {
		RoomiesFragment tab;
		if (position == 0) {
			tab = CurrentBudgetStatus.getInstance();
			String tag=tab.getTag();
			mTags.put(position, tag);

		} else {
			tab = CurrentExpenseReport.getInstance();
			String tag=tab.getTag();
			mTags.put(position,tag);
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

	@Override
	public int getItemPosition(Object item) {
		if (item instanceof UpdatableFragment){
			((UpdatableFragment)item).update(null);
		}
		return super.getItemPosition(item);
	}
	public String getFragment(int position){
		String tag=mTags.get(position);
		return tag;
	}
}
