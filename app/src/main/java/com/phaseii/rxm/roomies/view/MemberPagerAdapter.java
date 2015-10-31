package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.tabs.MemberDailyTab;
import com.phaseii.rxm.roomies.tabs.MemberMonthlyTab;
import com.phaseii.rxm.roomies.tabs.MemberWeeklyTab;

import java.util.List;

/**
 * Created by Snehankur on 10/24/2015.
 */
public class MemberPagerAdapter extends FragmentPagerAdapter {
    private String mTitles[];
    private int numbOfTabs;
    private List<RoomExpenses> membersExpenses;
    private Context mContext;

    @Override
    public Fragment getItem(int position) {
        RoomiesFragment tab = null;
        switch (position) {
            case 0:
                tab = MemberDailyTab.getInstance(mContext, membersExpenses);
                break;
            case 1:
                tab = MemberWeeklyTab.getInstance(mContext, membersExpenses);
                break;
            case 2:
                tab = MemberMonthlyTab.getInstance(mContext, membersExpenses);
                break;
        }
        return tab;
    }

    public MemberPagerAdapter(Context mContext, FragmentManager fm, String titles[],
                              int mNumbOfTabs, List<RoomExpenses> membersExpenses) {
        super(fm);
        this.mTitles = titles;
        this.numbOfTabs = mNumbOfTabs;
        this.membersExpenses = membersExpenses;
        this.mContext = mContext;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
