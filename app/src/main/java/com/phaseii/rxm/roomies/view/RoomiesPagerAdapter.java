package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.phaseii.rxm.roomies.fragments.DashboardFragment;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.fragments.SummaryFragment;
import com.phaseii.rxm.roomies.tabs.CurrentExpenseReport;
import com.phaseii.rxm.roomies.tabs.LastMonthsTab;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class RoomiesPagerAdapter extends FragmentStatePagerAdapter {
    String titles[];
    int numbOfTabs;
    private Map<Integer, String> mTags = new HashMap<>();

    public RoomiesPagerAdapter(FragmentManager fm, String mTitles[],
                               int mNumbOfTabs) {
        super(fm);
        this.titles = mTitles;
        this.numbOfTabs = mNumbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        RoomiesFragment tab = null;
        String tag = null;
        switch (position) {
            case 0:
                tab = SummaryFragment.getInstance();
                tag = tab.getTag();
                mTags.put(position, tag);
                break;
            case 1:
                tab = DashboardFragment.getInstance();
                tag = tab.getTag();
                mTags.put(position, tag);
                break;
            case 2:
                tab = CurrentExpenseReport.getInstance();
                tag = tab.getTag();
                mTags.put(position, tag);
                break;
            case 3:
                tab = LastMonthsTab.getInstance();
                tag = tab.getTag();
                mTags.put(position, tag);
                break;
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

