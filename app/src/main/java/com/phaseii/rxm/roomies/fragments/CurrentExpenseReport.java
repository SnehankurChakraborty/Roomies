package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phaseii.rxm.roomies.R;

import java.util.ArrayList;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_EXPENDITURE_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.SPENT;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class CurrentExpenseReport extends RoomiesFragment {

	public static CurrentExpenseReport getInstance() {
		return new CurrentExpenseReport();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_current_expense_report, container, false);
		showBarGraph(getActivity().getBaseContext());
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	private PieChart showBarGraph(Context context) {
		PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_expense_report);
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ROOM_EXPENDITURE_FILE_KEY, Context.MODE_PRIVATE);
		float rent = Float.valueOf(sharedPreferences.getString(RENT, "0"));
		float maid = Float.valueOf(sharedPreferences.getString(MAID, "0"));
		float electricity = Float.valueOf(sharedPreferences.getString(ELECTRICITY, "0"));
		float misc = Float.valueOf(sharedPreferences.getString(MISC, "0"));
		ArrayList<Entry> entries = new ArrayList<Entry>();
		ArrayList<String> labels = new ArrayList<String>();
		if (rent > 0) {
			entries.add(new Entry(rent, 0));
			labels.add(RENT);
		}
		if (maid > 0) {
			entries.add(new Entry(maid, 1));
			labels.add(MAID);
		}
		if (electricity > 0) {
			entries.add(new Entry(electricity, 2));
			labels.add(ELECTRICITY);
		}
		if (misc > 0) {
			entries.add(new Entry(misc, 3));
			labels.add(MISC);
		}
		float total = rent + maid + electricity + misc;
		if (total <= 0) {
			entries.add(new Entry(0, 0));
			labels.add("NO SPENDS");
		}
		PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS,
				null));
		dataSet.setColors(ROOMIES_ALL_COLORS);
		PieData data = new PieData(labels, dataSet);
		mChart.setData(data);
		mChart.animateXY(1000, 1000);
		mChart.setDrawCenterText(true);
		mChart.setCenterText(getPercentageLeft(total, 0));
		mChart.setDescription("");
		mChart.setClickable(true);
		return mChart;
	}

	private String getPercentageLeft(float total, float expense) {
		float percent = 0f;
		if (expense > 0) {
			percent = 100-((expense / total) * 100);
		}
		return percent + "% Spends";
	}
}
