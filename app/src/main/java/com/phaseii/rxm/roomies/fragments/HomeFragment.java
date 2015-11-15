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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.tabs.DashboardTab;
import com.phaseii.rxm.roomies.tabs.MonthlyTab;
import com.phaseii.rxm.roomies.tabs.SummaryTab;
import com.phaseii.rxm.roomies.tabs.TrendsTab;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.ColorUtils;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabStrip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends RoomiesFragment {
    private RoomiesSlidingTabLayout tabs;
    private List<RoomExpenses> roomExpensesList;
    private List<RoomStats> roomStatsList;

    /**
     *
     * @return
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        roomExpensesList = getParceableBundle().getParcelableArrayList(
                ActivityUtils.Extras.ROOM_EXPENSES.getValue());
        roomStatsList = getParceableBundle().getParcelableArrayList(
                ActivityUtils.Extras.ROOM_STATS.getValue());
        Context mContext = getActivity();
        final String titles[] = {"Summary", "Dashboard", "Monthly", "Trends"};
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        final int[] colors = new int[]{mContext.getResources().getColor(R.color.primary_home),
                mContext.getResources().getColor(R.color.primary2_home),
                mContext.getResources().getColor(R.color.primary3_home),
                mContext.getResources().getColor(R.color.primary4_home),
                mContext.getResources().getColor(R.color.primary5)};
        final int[] colorsDark = new int[]{mContext.getResources().getColor(R.color
                .primary_dark_home),
                mContext.getResources().getColor(R.color.primary_dark2_home),
                mContext.getResources().getColor(R.color.primary_dark3_home),
                mContext.getResources().getColor(R.color.primary_dark4_home),
                mContext.getResources().getColor(R.color.primary_dark5)};
        final RoomiesHomePagerAdapter adapter = new RoomiesHomePagerAdapter
                (getChildFragmentManager()
                        , titles, 4);

        pager.setAdapter(adapter);

        tabs = (RoomiesSlidingTabLayout) rootView.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new RoomiesSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        tabs.setViewPager(pager);
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

        return rootView;
    }

    /**
     *
     * @return
     */
    public RoomiesSlidingTabLayout getTab() {
        return tabs;
    }

    /**
     *
     * @return
     */
    @Override
    public View getFragmentView() {
        return rootView;
    }

    /**
     *
     */
    public class RoomiesHomePagerAdapter extends FragmentPagerAdapter {
        String titles[];
        int numbOfTabs;
        private Map<Integer, String> mTags = new HashMap<>();
        private SparseArray<Fragment> registeredFragments = new SparseArray<>();

        /**
         *
         * @param fm
         * @param mTitles
         * @param mNumbOfTabs
         */
        public RoomiesHomePagerAdapter(FragmentManager fm, String mTitles[],
                                       int mNumbOfTabs) {
            super(fm);
            this.titles = mTitles;
            this.numbOfTabs = mNumbOfTabs;
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            RoomiesFragment tab = null;
            String tag = null;
            switch (position) {
                case 0:
                    tab = SummaryTab.getInstance();
                    tag = tab.getTag();
                    mTags.put(position, tag);
                    break;
                case 1:
                    tab = DashboardTab.getInstance(roomExpensesList);
                    tag = tab.getTag();
                    mTags.put(position, tag);
                    break;
                case 2:
                    tab = MonthlyTab.getInstance(roomExpensesList);
                    tag = tab.getTag();
                    mTags.put(position, tag);
                    break;
                case 3:
                    tab = TrendsTab.getInstance(roomStatsList);
                    tag = tab.getTag();
                    mTags.put(position, tag);
                    break;
            }
            return tab;
        }

        /**
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        /**
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        /**
         *
         * @param position
         * @return
         */
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        /**
         *
         * @return
         */
        @Override
        public int getCount() {
            return numbOfTabs;
        }
    }
}
