package com.phaseii.rxm.roomies.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Snehankur on 10/24/2015.
 */
public class MemberDailyTab extends RoomiesFragment {
    private static List<RoomExpenses> memberExpenses = new ArrayList<>();
    private static Context mContext;

    public static MemberDailyTab getInstance(Context context, List<RoomExpenses> expenses) {
        if (null != expenses) {
            memberExpenses.clear();
            memberExpenses.addAll(expenses);
        }
        mContext = context;
        return new MemberDailyTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.memberdaily, container, false);
        LineChart lineChart = (LineChart) rootView.findViewById(R.id.member_daily);
        createLineChart(lineChart);
        return rootView;
    }

    public void createLineChart(LineChart lineChart) {
        lineChart.setDescription("");
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setData(getLineChart(memberExpenses));
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(500);
        lineChart.animateX(500);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setNoDataText("No Room expenses yet");
        lineChart.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        /*lineChart.setMarkerView(new CustomMarkerView(mContext, R.layout.custom_marker_view));*/

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(mContext.getResources().getColor(R.color.primary5));
    }

    private LineData getLineChart(List<RoomExpenses> expenses) {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i <= 23; i++) {
            float val = 0f;
            for (RoomExpenses memberExpense : memberExpenses) {
                cal.setTime(memberExpense.getExpenseDate());
                if (day == cal.get(Calendar.DAY_OF_MONTH) && i == cal.get(Calendar.HOUR_OF_DAY)) {
                    val = val + memberExpense.getAmount();
                }
            }
            entries.add(new Entry(val, i - 1));
            labels.add(String.valueOf(i));
        }

        LineDataSet set = new LineDataSet(entries, "Daily Expense Report");
        set.setLineWidth(1.5f);
        set.setCircleColor(mContext.getResources().getColor(R.color.primary5));
        set.setCircleColorHole(mContext.getResources().getColor(R.color.primary_dark5));
        set.setCircleSize(3f);
        /*set.setFillColor(mContext.getResources().getColor(R.color.primary2));*/
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        /*set.setDrawFilled(true);*/
        set.setColor(mContext.getResources().getColor(R.color.primary_dark5));
        /*set.setValueTextColor(mContext.getResources().getColor(R.color.primary_dark5));*/
        set.setDrawCubic(false);
        /*set.setFillColor(mContext.getResources().getColor(R.color.primary5));*/
        LineData lineData = new LineData(labels, set);
        return lineData;
    }

    @Override
    public View getFragmentView() {
        return null;
    }
}
