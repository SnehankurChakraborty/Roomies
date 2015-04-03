package com.phaseii.rxm.roomies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class CurrentExpenseReport extends RoomiesFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_current_expense_report, container, false);
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}
}
