package com.phaseii.rxm.roomies.tabs;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.AddExpenseDialog;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;
import com.phaseii.rxm.roomies.view.DetailExpenseDataTableAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DetailExpenseTab extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {
	LineChart lineChart;
	int start;
	int day;
	Calendar cal = Calendar.getInstance();

	public static DetailExpenseTab getInstance() {
		return new DetailExpenseTab();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.detail_expense_tab, container, false);
		createCombinedChart();
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public void createCombinedChart() {
		final List<MiscExpense> miscExpenses = new MiscServiceImpl(
				getActivity().getBaseContext()).getCurrentTotalMiscExpense();
		lineChart = (LineChart) rootView.findViewById(R.id.trend_chart);

		lineChart.setDescription("");
		lineChart.setDrawGridBackground(false);
		lineChart.setHighlightEnabled(true);
		lineChart.setTouchEnabled(true);
		lineChart.setDragEnabled(true);
		lineChart.setScaleEnabled(true);


		YAxis rightAxis = lineChart.getAxisRight();
		rightAxis.setDrawGridLines(false);

		YAxis leftAxis = lineChart.getAxisLeft();
		leftAxis.setDrawGridLines(false);

		XAxis xAxis = lineChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

		lineChart.setData(getLineChart(miscExpenses));
		lineChart.setPinchZoom(false);
		lineChart.setDoubleTapToZoomEnabled(false);
		lineChart.setDrawGridBackground(false);
		lineChart.animateY(500);
		lineChart.animateX(500);
		lineChart.setNoDataText("No Miscellaneous expenses yet");
		lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				DialogFragment dialog = new DetailExpenseDataTableAdapter();
				dialog.show(getActivity().getSupportFragmentManager(), "detailExpense");
			}

			@Override
			public void onNothingSelected() {

			}
		});
	}

	private LineData getLineChart(List<MiscExpense> miscExpenses) {
		
		ArrayList<Entry> entries = new ArrayList<Entry>();
		ArrayList<String> labels = new ArrayList<>();
		if (miscExpenses.size() > 0) {
			cal.setTime(miscExpenses.get(0).getTransactionDate());
			start = cal.get(Calendar.DAY_OF_MONTH);
			for (MiscExpense misc : miscExpenses) {
				cal.setTime(misc.getTransactionDate());
				day = cal.get(Calendar.DAY_OF_MONTH);
	            if (!labels.contains(String.valueOf(day))) {
                    labels.add(String.valueOf(day));
                    entries.add(new Entry(misc.getAmount(), (day-start)));
                } else {
                    for (Entry entry : entries) {
                        if (entry.getXIndex() == (day-start)) {
                            float val = entry.getVal();
                            val = val + misc.getAmount();
                            entry.setVal(val);
                        }
                    }
                }
			}
		}

		LineDataSet set = new LineDataSet(entries, "Daily Expense Report");
		set.enableDashedLine(10f, 5f, 0f);
		set.setColor(Color.BLACK);
		set.setLineWidth(1f);
		set.setCircleColor(getResources().getColor(R.color.primary_dark));
		set.setCircleSize(5f);
		set.setFillColor(getResources().getColor(R.color.primary));
		set.setDrawValues(true);
		set.setValueTextSize(10f);
		set.setValueTextColor(getResources().getColor(R.color.primary_text));
		set.setAxisDependency(YAxis.AxisDependency.LEFT);
		set.setDrawFilled(true);
		set.setFillColor(getResources().getColor(R.color.accent));
		LineData lineData = new LineData(labels, set);
		return lineData;
	}

	@Override
	public void update() {
		createCombinedChart();
	}
}
