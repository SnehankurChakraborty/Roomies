package com.phaseii.rxm.roomies.fragments;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TrendFragment extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {
	HorizontalBarChart barChart;
	int start;
	int day;
	Calendar cal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.detail_report_tab, container, false);
		createBarChart();
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public void createBarChart() {
		final List<MiscExpense> miscExpenses = new MiscServiceImpl(
				getActivity().getBaseContext()).getCurrentTotalMiscExpense();
		barChart = (HorizontalBarChart) rootView.findViewById(R.id.trend_chart);
		ArrayList<String> labels = new ArrayList<>();
		ArrayList<BarEntry> entries = new ArrayList<>();
		cal = Calendar.getInstance();
		if (miscExpenses.size() > 0) {
			cal.setTime(miscExpenses.get(0).getTransactionDate());
			start = cal.get(Calendar.DAY_OF_MONTH);
			if (start - 2 > 0) {
				labels.add(String.valueOf(start - 2));
				entries.add(new BarEntry(0, 0));
			}
			if (start - 1 > 0) {
				labels.add(String.valueOf(start - 1));
				entries.add(new BarEntry(0, 1));
			}

			for (MiscExpense misc : miscExpenses) {
				cal.setTime(misc.getTransactionDate());
				day = cal.get(Calendar.DAY_OF_MONTH);
				if (!labels.contains(String.valueOf(day))) {
					labels.add(String.valueOf(day));
					entries.add(new BarEntry(misc.getAmount(), (day - start) + 2));
				} else {
					for (BarEntry entry : entries) {
						if (entry.getXIndex() == ((day - start) + 2)) {
							float val = entry.getVal();
							val = val + misc.getAmount();
							entry.setVal(val);
						}
					}
				}
			}
			int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if ((day + 1) <= daysInMonth) {
				labels.add(String.valueOf(day + 1));
				entries.add(new BarEntry(0, (day - start) + 3));
			}
			if ((day + 2) <= daysInMonth) {
				labels.add(String.valueOf(day + 2));
				entries.add(new BarEntry(0, (day - start) + 4));
			}
		}
		BarDataSet dataset = new BarDataSet(entries, "Trend");
		dataset.setColors(ColorTemplate.JOYFUL_COLORS);
		dataset.setBarShadowColor(Color.WHITE);
		BarData data = new BarData(labels, dataset);
		barChart.setData(data);
		barChart.setScaleMinima(1f, 1f);
		barChart.animateY(500);
		barChart.setDrawValueAboveBar(true);
		barChart.setDescription("");
		barChart.setDrawGridBackground(false);
		barChart.setPinchZoom(true);
		barChart.setDoubleTapToZoomEnabled(false);
		barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			@Override
			public void onValueSelected(Entry entry, int dataSetIndex, Highlight h) {
				if (entry.getVal() > 0) {
					int index = entry.getXIndex() - 3;
					List<MiscExpense> miscExpensesList = new ArrayList<MiscExpense>();
					for (MiscExpense misc : miscExpenses) {
						cal.setTime(misc.getTransactionDate());
						if (cal.get(Calendar.DAY_OF_MONTH) == (start - index)) {
							miscExpensesList.add(misc);
						}
					}
					DetailExpenseDialog dialog = DetailExpenseDialog.getInstance(miscExpensesList);
					dialog.show(getActivity().getSupportFragmentManager(), "DetailExpense");
				}
			}

			@Override
			public void onNothingSelected() {

			}
		});

		Legend l = barChart.getLegend();
		l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
		l.setFormSize(8f);
		l.setXEntrySpace(4f);

		XAxis xl = barChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(false);


		YAxis yl = barChart.getAxisLeft();
		yl.setDrawAxisLine(true);
		yl.setDrawGridLines(false);


		YAxis yr = barChart.getAxisRight();
		yr.setDrawAxisLine(true);
		yr.setDrawGridLines(false);


	}

	@Override
	public void update() {
		createBarChart();
	}
}
