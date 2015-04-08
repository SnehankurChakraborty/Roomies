package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.fragments.CurrentBudgetStatus;
import com.phaseii.rxm.roomies.fragments.CurrentExpenseReport;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;

import java.util.HashMap;
import java.util.Map;

import static com.phaseii.rxm.roomies.fragments.RoomiesFragment.*;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class RoomiesPagerAdapter extends FragmentStatePagerAdapter {

	CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
	int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
	private Map<Integer, String> mTags=new HashMap<>();

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
			String tag=tab.getTag();
			mTags.put(position, tag);

		} else {
			tab = CurrentExpenseReport.getInstance();
			String tag=tab.getTag();
			mTags.put(position,tag);
		}
		return tab;
	}
/*
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Object obj=super.instantiateItem(container, position);;
		if(obj instanceof RoomiesFragment){
			RoomiesFragment f=(RoomiesFragment)obj;
			String tag=f.getTag();
			mTags.put(position,tag);
		}
		return obj;
	}*/

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	// This method return the Number of tabs for the tabs Strip

	@Override
	public int getCount() {
		return NumbOfTabs;
	}

	@Override
	public int getItemPosition(Object item) {
		if (item instanceof UpdatableFragment){
			((UpdatableFragment)item).update();
		}
		return super.getItemPosition(item);
	}
	public String getFragment(int position){
		String tag=mTags.get(position);
		return tag;
	}
}
