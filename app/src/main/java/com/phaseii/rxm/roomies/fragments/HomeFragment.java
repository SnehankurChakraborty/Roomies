package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
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
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabStrip;


public class HomeFragment extends RoomiesFragment {
    private RoomiesSlidingTabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container,
                false);
        Context mContext = getActivity();
        String titles[] = {"Summary", "Dashboard", "Monthly", "Trends", "Members"};
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        final int[] colors = new int[]{mContext.getResources().getColor(R.color.primary),
                mContext.getResources().getColor(R.color.primary2),
                mContext.getResources().getColor(R.color.primary3),
                mContext.getResources().getColor(R.color.primary4),
                mContext.getResources().getColor(R.color.primary5)};
        final int[] colorsDark = new int[]{mContext.getResources().getColor(R.color.primary_dark),
                mContext.getResources().getColor(R.color.primary_dark2),
                mContext.getResources().getColor(R.color.primary_dark3),
                mContext.getResources().getColor(R.color.primary_dark4),
                mContext.getResources().getColor(R.color.primary_dark5)};
        final RoomiesPagerAdapter adapter = new RoomiesPagerAdapter(getChildFragmentManager()
                , titles, 5);
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
                final int blended = blendColors(colors[position + 1], colors[position],
                        positionOffset);
                final int blendedDark = blendColors(colorsDark[position + 1], colorsDark[position],
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
                            new ColorDrawable(blendColors(colors[position + 1],
                                    colors[position], positionOffset)));
                } else {
                    mTabStrip.setBackgroundDrawable(
                            new ColorDrawable(blendColors(colors[position + 1],
                                    colors[position], positionOffset)));
                }
            }

            @Override
            public void onTabSelected(RoomiesSlidingTabStrip mTabStrip, int position) {

            }
        });

        return rootView;
    }

    public RoomiesSlidingTabLayout getTab() {
        return tabs;
    }

    private int blendColors(int from, int to, float ratio) {
        final float inverseRation = 1f - ratio;
        final float r = Color.red(from) * ratio + Color.red(to) * inverseRation;
        final float g = Color.green(from) * ratio + Color.green(to) * inverseRation;
        final float b = Color.blue(from) * ratio + Color.blue(to) * inverseRation;
        return Color.rgb((int) r, (int) g, (int) b);
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }
}
