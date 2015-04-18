package com.phaseii.rxm.roomies.tabs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class ExpenseSummaryTab extends RoomiesFragment {

	private Context mContext;
	MiscServiceImpl miscService;
	ArrayAdapter<String> monthAdapter;


	public static ExpenseSummaryTab getInstance() {
		return new ExpenseSummaryTab();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.expense_summary_tab, container, false);
		mContext = getActivity().getBaseContext();
		fillSpinnerData();
		createBarChart();
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	public void fillSpinnerData() {
		miscService = new MiscServiceImpl(mContext);
		List<String> months = miscService.getMiscMonths(
				mContext.getSharedPreferences(RoomiesConstants
						.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(RoomiesConstants.NAME,
						null));
		Spinner monthSpinner = (Spinner) rootView.findViewById(R.id.monthspinner);
		monthAdapter = new ArrayAdapter<String>(mContext, R.layout.roomies_spinner_item, months);
		monthSpinner.setAdapter(monthAdapter);
	}

	public void createBarChart() {
		miscService = new MiscServiceImpl(mContext);
		BarChart barChart = (BarChart) rootView.findViewById(R.id.summary);
		ArrayList<String> labels = new ArrayList<>();
		ArrayList<BarEntry> entries = new ArrayList<>();
		List<MiscExpense> miscExpenses = miscService.getCurrentTotalMiscExpense();
		float grocery = 0f;
		float vegetables = 0f;
		float others = 0f;
		float na = 0f;
		String types[] = mContext.getResources().getStringArray(R.array.subcategory);
		for (MiscExpense miscExpense : miscExpenses) {
			String type = miscExpense.getType();
			if (type.equals(types[0])) {
				na = na + miscExpense.getAmount();
			} else if (type.equals(types[1])) {
				grocery = grocery + miscExpense.getAmount();
			} else if (type.equals(types[2])) {
				vegetables = vegetables + miscExpense.getAmount();
			} else if (type.equals(types[3])) {
				others = others + miscExpense.getAmount();
			}
		}
		labels.addAll(Arrays.asList(types));
		entries.add(new BarEntry(na, 0));
		entries.add(new BarEntry(grocery, 1));
		entries.add(new BarEntry(vegetables, 2));
		entries.add(new BarEntry(others, 3));
		BarDataSet dataset=new BarDataSet(entries,"Summary");
		dataset.setColors(ColorTemplate.JOYFUL_COLORS);
		dataset.setBarShadowColor(Color.TRANSPARENT);

		BarData data = new BarData(labels, dataset);
		barChart.setData(data);
		barChart.animateY(500);
		barChart.setDrawValueAboveBar(true);

		barChart.setDescription("");
		barChart.setDrawGridBackground(false);
		barChart.setPinchZoom(true);
		barChart.setDoubleTapToZoomEnabled(false);

		YAxis yl = barChart.getAxisLeft();
		yl.setDrawAxisLine(false);
		yl.setDrawGridLines(false);


		YAxis yr = barChart.getAxisRight();
		yr.setDrawAxisLine(false);
		yr.setDrawGridLines(false);


		XAxis xl = barChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(false);
	}
}
