package com.phaseii.rxm.roomies.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.model.RoomBudget;
import com.phaseii.rxm.roomies.service.MiscService;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;
import com.phaseii.rxm.roomies.service.RoomiesService;
import com.phaseii.rxm.roomies.service.RoomiesServiceImpl;
import com.phaseii.rxm.roomies.view.RoomiesMarkerView;

import java.util.ArrayList;
import java.util.List;


public class SavingsFragment extends RoomiesFragment {

	RoomiesService roomiesService;
	Context mContext;
	List<RoomBudget> roomBudgetList;
	ArrayList<BarDataSet> dataSets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		mContext = getActivity().getBaseContext();
		rootView = inflater.inflate(R.layout.fragment_savings, container, false);
		createBarChart();
		return rootView;

	}

	public void createBarChart() {
		HorizontalBarChart barChart = (HorizontalBarChart) rootView.findViewById(R.id.savings);
		ArrayList<String> labels = new ArrayList<>();
		ArrayList<BarEntry> rentEntries = new ArrayList<>();
		ArrayList<BarEntry> maidEntries = new ArrayList<>();
		ArrayList<BarEntry> elecEntries = new ArrayList<>();
		ArrayList<BarEntry> miscEntries = new ArrayList<>();
		dataSets = new ArrayList<>();
		roomiesService = new RoomiesServiceImpl(mContext);
		roomBudgetList = roomiesService.getAllMonthDetailsWithMargin(mContext
				.getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
						Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null));
		int index = 0;
		for (RoomBudget roomBudget : roomBudgetList) {
			float rentSavings = roomBudget.getRent_margin() - roomBudget.getRent();
			float maidSavings = roomBudget.getMaid_margin() - roomBudget.getMaid();
			float electricitySavings = roomBudget.getElectricity_margin() - roomBudget.getElectricity();
			float miscSavings = roomBudget.getMiscellaneous_margin() - roomBudget.getMiscellaneous();
			labels.add(roomBudget.getMonth());
			rentEntries.add(new BarEntry(rentSavings, index));
			maidEntries.add(new BarEntry(maidSavings, index));
			elecEntries.add(new BarEntry(electricitySavings, index));
			miscEntries.add(new BarEntry(miscSavings, index));
			index++;

		}
		final BarDataSet rentDataSet = new BarDataSet(rentEntries, "Rent");
		rentDataSet.setBarShadowColor(Color.TRANSPARENT);
		rentDataSet.setColor(ColorTemplate.JOYFUL_COLORS[0]);
		dataSets.add(rentDataSet);
		final BarDataSet maidDataSet = new BarDataSet(maidEntries, "Maid");
		maidDataSet.setBarShadowColor(Color.TRANSPARENT);
		maidDataSet.setColor(ColorTemplate.JOYFUL_COLORS[1]);
		dataSets.add(maidDataSet);
		final BarDataSet elecDataSet = new BarDataSet(elecEntries, "Elec");
		elecDataSet.setBarShadowColor(Color.TRANSPARENT);
		elecDataSet.setColor(ColorTemplate.JOYFUL_COLORS[2]);
		dataSets.add(elecDataSet);
		final BarDataSet miscDataSet = new BarDataSet(miscEntries, "Misc");
		miscDataSet.setBarShadowColor(Color.TRANSPARENT);
		miscDataSet.setColor(ColorTemplate.JOYFUL_COLORS[3]);
		dataSets.add(miscDataSet);

		BarData data = new BarData(labels, dataSets);
		barChart.setData(data);
		barChart.animateY(500);
		barChart.setDrawValueAboveBar(true);
		barChart.setDescription("");
		barChart.setDrawGridBackground(false);
		barChart.setPinchZoom(true);
		barChart.setScaleMinima(1f, 1f);
		barChart.setDoubleTapToZoomEnabled(false);
		barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				CardView cardView = (CardView) rootView.findViewById(R.id.savings_card);
				cardView.setVisibility(View.VISIBLE);
				RoomBudget roomBudget = roomBudgetList.get(e.getXIndex());
				if (dataSets.get(dataSetIndex).getLabel().equals("Rent")) {
					showBubble(roomBudget.getMonth(), roomBudget.getRent_margin(),
							roomBudget.getRent(), "Rent");
					cardView.setCardBackgroundColor(ColorTemplate.JOYFUL_COLORS[0]);
				} else if (dataSets.get(dataSetIndex).getLabel().equals("Maid")) {
					showBubble(roomBudget.getMonth(), roomBudget.getMaid_margin(),
							roomBudget.getMaid(), "Maid");
					cardView.setCardBackgroundColor(ColorTemplate.JOYFUL_COLORS[1]);
				} else if (dataSets.get(dataSetIndex).getLabel().equals("Elec")) {
					showBubble(roomBudget.getMonth(), roomBudget.getElectricity_margin(),
							roomBudget.getElectricity(), "Electricity");
					cardView.setCardBackgroundColor(ColorTemplate.JOYFUL_COLORS[2]);
				} else if (dataSets.get(dataSetIndex).getLabel().equals("Misc")) {
					showBubble(roomBudget.getMonth(), roomBudget.getMiscellaneous_margin(),
							roomBudget.getMiscellaneous(), "Miscellaneous");
					cardView.setCardBackgroundColor(ColorTemplate.JOYFUL_COLORS[3]);
				}
			}

			@Override
			public void onNothingSelected() {

			}
		});

		Legend l = barChart.getLegend();
		l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

		YAxis yl = barChart.getAxisLeft();
		yl.setDrawAxisLine(false);
		yl.setDrawGridLines(false);
		yl.setSpaceTop(10f);
		yl.setStartAtZero(false);
		yl.setValueFormatter(new LargeValueFormatter());

		barChart.getAxisRight().setEnabled(false);

		XAxis xl = barChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(false);

	}

	private void showBubble(String selectedMonth, float selectedMargin, float selectedExpense,
	                        String type) {
		Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Thin.ttf");
		float selectedSavings = selectedMargin - selectedExpense;
		TextView month = (TextView) rootView.findViewById(R.id.month);
		TextView description = (TextView) rootView.findViewById(R.id.description);
		TextView savings = (TextView) rootView.findViewById(R.id.savings_value);
		savings.setTypeface(typeface);
		month.setText(selectedMonth);
		String descriptionValue = selectedExpense + " out of " + selectedMargin + " spent on " +
				type;
		description.setText(descriptionValue);
		savings.setText(String.valueOf(selectedSavings));
		if (selectedSavings < 0) {
			savings.setTextColor(Color.RED);
		} else if (selectedSavings == 0) {
			savings.setTextColor(Color.BLUE);
		} else {
			savings.setTextColor(Color.GREEN);
		}

	}


	@Override
	public View getFragmentView() {
		return rootView;
	}
}
