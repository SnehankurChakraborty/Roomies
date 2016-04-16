/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.ui.customviews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.ui.fragments.MemberDailyTab;
import com.phaseii.rxm.roomies.ui.fragments.MemberMonthlyTab;
import com.phaseii.rxm.roomies.ui.fragments.MemberWeeklyTab;
import com.phaseii.rxm.roomies.ui.fragments.RoomiesFragment;

import java.util.List;

/**
 * Created by Snehankur on 10/24/2015.
 */
public class MemberPagerAdapter extends FragmentPagerAdapter {
    private String mTitles[];
    private int numbOfTabs;
    private List<RoomExpenses> membersExpenses;
    private Context mContext;

    public MemberPagerAdapter(Context mContext, FragmentManager fm, String titles[],
            int mNumbOfTabs, List<RoomExpenses> membersExpenses) {
        super(fm);
        this.mTitles = titles;
        this.numbOfTabs = mNumbOfTabs;
        this.membersExpenses = membersExpenses;
        this.mContext = mContext;
    }

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

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
