package com.phaseii.rxm.roomies.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;
import com.phaseii.rxm.roomies.view.RoomiesPagerAdapter;


public class StatsFragment extends RoomiesFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_savings, container,
                false);
        String titles[] = {"Current", "Trends"};
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        RoomiesPagerAdapter adapter = new RoomiesPagerAdapter(getChildFragmentManager()
                , titles, 2);
        pager.setAdapter(adapter);
        RoomiesSlidingTabLayout tabs = (RoomiesSlidingTabLayout) rootView.findViewById(R.id.tabs);
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


    @Override
    public View getFragmentView() {
        return rootView;
    }
}

