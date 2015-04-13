package com.phaseii.rxm.roomies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.phaseii.rxm.roomies.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TrendFragment extends RoomiesFragment {
	BarChart barChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_trend, container, false);
		barChart = (BarChart) rootView.findViewById(R.id.trend_chart);
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	public void createBarChart() {
		String[] dates = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
		ArrayList<String> labels = new ArrayList<>(Arrays.asList(dates));
		ArrayList<BarDataSet> dataset = new ArrayList<>();

		BarData data = new BarData(labels, dataset);
	}
}
