package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.tabs.RoommateExpensesTab;
import com.phaseii.rxm.roomies.tabs.RoommatesContactsTab;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.ColorUtils;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabStrip;

import java.util.List;

/**
 * Created by Snehankur on 10/25/2015.
 */
public class RoommatesFragment extends RoomiesFragment {

    /**
     *
     * @return
     */
    public static RoommatesFragment newInstance() {
        return new RoommatesFragment();
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_roommates, container,
                false);
        Context mContext = getActivity();
        List<RoomExpenses> roomExpensesList = getParceableBundle().getParcelableArrayList
                (ActivityUtils
                .Extras.ROOM_EXPENSES.getValue());


        final String mTitles[] = {"Expenses", "Details"};

        final int numOfTabs = 2;
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.roommate_pager);
        final int[] colors = new int[]{mContext.getResources().getColor(R.color.primary_roommate),
                mContext.getResources().getColor(R.color.primary2_roommate)};
        final int[] colorsDark = new int[]{mContext.getResources().getColor(R.color
                .primary_dark_roommate),
                mContext.getResources().getColor(R.color.primary_dark2_home)};
        final RoommatePagerAdapter adapter = new RoommatePagerAdapter(getChildFragmentManager(),
                mTitles, numOfTabs, roomExpensesList);
        pager.setAdapter(adapter);

        RoomiesSlidingTabLayout tabs = (RoomiesSlidingTabLayout) rootView.findViewById(R.id
                .roommates_tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new RoomiesSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (position >= adapter.getCount() - 1) {
                    // Guard against ArrayIndexOutOfBoundsException
                    return;
                }

                // Blend the colors and adjust the ActionBar
                final int blended = ColorUtils.blendColors(colors[position + 1], colors[position],
                        positionOffset);
                final int blendedDark = ColorUtils.blendColors(colorsDark[position + 1],
                        colorsDark[position],
                        positionOffset);
                ((ActionBarActivity) getActivity())
                        .getSupportActionBar().setBackgroundDrawable(
                        new ColorDrawable(blended));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setNavigationBarColor(blendedDark);
                    getActivity().getWindow().setStatusBarColor(blendedDark);
                }
            }

        });

        tabs.setThemeChanger(new RoomiesSlidingTabLayout.ThemeChanger() {
            @Override
            public void onSwipe(RoomiesSlidingTabStrip mTabStrip, int position, float
                    positionOffset) {
                if (position >= adapter.getCount() - 1) {
                    // Guard against ArrayIndexOutOfBoundsException
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mTabStrip.setBackground(
                            new ColorDrawable(ColorUtils.blendColors(colors[position + 1],
                                    colors[position], positionOffset)));
                } else {
                    mTabStrip.setBackgroundDrawable(
                            new ColorDrawable(ColorUtils.blendColors(colors[position + 1],
                                    colors[position], positionOffset)));
                }
            }

            @Override
            public void onTabSelected(RoomiesSlidingTabStrip mTabStrip, int position) {

            }
        });
        tabs.setViewPager(pager);
        return rootView;
    }

    /**
     *
     * @return
     */
    @Override
    public View getFragmentView() {
        return null;
    }

    /**
     *
     */
    public class RoommatePagerAdapter extends FragmentPagerAdapter {

        private CharSequence[] mTitles;
        private int numOfTabs;
        private List<RoomExpenses> roomExpensesList;


        /**
         *
         * @param fm
         * @param mTitles
         * @param numoFtabs
         * @param roomExpensesList
         */
        public RoommatePagerAdapter(FragmentManager fm, String mTitles[], int numoFtabs,
                                    List<RoomExpenses> roomExpensesList) {
            super(fm);
            this.mTitles = mTitles;
            this.numOfTabs = numoFtabs;
            this.roomExpensesList = roomExpensesList;
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            RoomiesFragment tab = null;
            switch (position) {
                case 0:
                    tab = RoommateExpensesTab.getInstance(roomExpensesList);
                    break;
                case 1:
                    tab = RoommatesContactsTab.getInstance(roomExpensesList);
                    break;
            }

            return tab;
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        /**
         *
         * @return
         */
        @Override
        public int getCount() {
            return numOfTabs;
        }
    }
}
