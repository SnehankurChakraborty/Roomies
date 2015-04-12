package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.view.BannerView;
import com.phaseii.rxm.roomies.view.RoomiesPagerAdapter;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;


public class HomeFragment extends RoomiesFragment{

	ViewPager pager;
	RoomiesSlidingTabLayout tabs;
	RoomiesPagerAdapter adapter;
	int numboftabs = 2;
	String titles[] = {"Room Budget", "Room Expense"};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		pager = (ViewPager) rootView.findViewById(R.id.pager);
		adapter = new RoomiesPagerAdapter(getChildFragmentManager(), titles, numboftabs);
		pager.setAdapter(adapter);
		tabs = (RoomiesSlidingTabLayout) rootView.findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true);
		tabs.setCustomTabColorizer(new RoomiesSlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.material_deep_teal_200);
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
