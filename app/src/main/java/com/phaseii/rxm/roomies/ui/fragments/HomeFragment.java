package com.phaseii.rxm.roomies.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.ui.customviews.RoomiesSlidingTabLayout;
import com.phaseii.rxm.roomies.utils.ActivityUtils;

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
        final HomePagerAdapter adapter = new HomePagerAdapter
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
    public class HomePagerAdapter extends FragmentPagerAdapter {
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
        public HomePagerAdapter(FragmentManager fm, String mTitles[],
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
