package com.phaseii.rxm.roomies.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.fragments.DashboardFragment;
import com.phaseii.rxm.roomies.fragments.MemberFragment;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.fragments.SummaryFragment;
import com.phaseii.rxm.roomies.tabs.CurrentExpenseReport;
import com.phaseii.rxm.roomies.tabs.LastMonthsTab;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class RoomiesPagerAdapter extends FragmentPagerAdapter {
    String titles[];
    int numbOfTabs;
    private Map<Integer, String> mTags = new HashMap<>();
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

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
            case 4:
                tab = MemberFragment.getInstance();
                tag = tab.getTag();
                mTags.put(position, tag);
                break;
        }
        return tab;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
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

