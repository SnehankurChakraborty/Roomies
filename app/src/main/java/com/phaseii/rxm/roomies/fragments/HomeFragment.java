package com.phaseii.rxm.roomies.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.view.RoomiesPagerAdapter;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;


public class HomeFragment extends RoomiesFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container,
                false);
        String titles[] = {"Summary", "Dashboard", "Monthly", "Trends"};
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        RoomiesPagerAdapter adapter = new RoomiesPagerAdapter(getChildFragmentManager()
                , titles, 4);
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
        tabs.setThemeChangeListener(
                new RoomiesSlidingTabLayout.ThemeChangeListener() {
                    @Override
                    public void changeTheme(int i) {

                        switch (i) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().setNavigationBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark));
                                    getActivity().getWindow().setStatusBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark));
                                }
                                ((ActionBarActivity) getActivity())
                                        .getSupportActionBar().setBackgroundDrawable(
                                        new ColorDrawable(getResources()
                                                .getColor(R.color.primary)));
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().setNavigationBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark2));
                                    getActivity().getWindow().setStatusBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark2));
                                }
                                ((ActionBarActivity) getActivity())
                                        .getSupportActionBar().setBackgroundDrawable(
                                        new ColorDrawable(getResources()
                                                .getColor(R.color.primary2)));

                                break;
                            case 2:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().setNavigationBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark3));
                                    getActivity().getWindow().setStatusBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark3));
                                }
                                ((ActionBarActivity) getActivity())
                                        .getSupportActionBar().setBackgroundDrawable(
                                        new ColorDrawable(getResources()
                                                .getColor(R.color.primary3)));
                                break;
                            case 3:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getActivity().getWindow().setNavigationBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark4));
                                    getActivity().getWindow().setStatusBarColor(
                                            getResources().getColor(
                                                    R.color.primary_dark4));
                                }
                                ((ActionBarActivity) getActivity())
                                        .getSupportActionBar().setBackgroundDrawable(
                                        new ColorDrawable(getResources()
                                                .getColor(R.color.primary4)));
                                break;
                        }

                    }
                }

        );
        return rootView;
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }
}
