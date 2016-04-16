/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.phaseii.rxm.roomies.ui.customviews.CustomMarkerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Snehankur on 10/24/2015.
 */
public class MemberWeeklyTab extends RoomiesFragment {

    private static List<RoomExpenses> memberExpenses = new ArrayList<>();
    private static Context mContext;

    public static MemberWeeklyTab getInstance(Context context, List<RoomExpenses> expenses) {
        if (null != expenses) {
            memberExpenses.clear();
            memberExpenses.addAll(expenses);
        }
        mContext = context;
        return new MemberWeeklyTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.memberweekly, container, false);
        LineChart lineChart = (LineChart) rootView.findViewById(R.id.member_weekly);
        createLineChart(lineChart);
        return rootView;
    }

    public void createLineChart(LineChart lineChart) {
        lineChart.setDescription("");
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerTapEnabled(false);
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
        lineChart.setMarkerView(new CustomMarkerView(mContext, R.layout.custom_marker_view));

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
        xAxis.setTextColor(mContext.getResources().getColor(R.color.primary_dark5));
    }

    private LineData getLineChart(List<RoomExpenses> expenses) {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<>();
        Calendar cal = GregorianCalendar.getInstance(Locale.ENGLISH);
        int month = cal.get(Calendar.MONTH);
        int startOfWeek = 0;
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, -1);//go one day before
        }
        if (month == cal.get(Calendar.MONTH)) {
            startOfWeek = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            startOfWeek = 1;
        }

        cal.add(Calendar.DATE, 6);
        if (month != cal.get(Calendar.MONTH)) {
            while (month != cal.get(Calendar.MONTH)) {
                cal.add(Calendar.DATE, -1);
            }
        }
        int endOfWeek = cal.get(Calendar.DAY_OF_MONTH);

        for (int i = startOfWeek; i <= endOfWeek; i++) {
            float val = 0f;
            for (RoomExpenses memberExpense : memberExpenses) {
                cal.setTime(memberExpense.getExpenseDate());
                if (i == cal.get(Calendar.DAY_OF_MONTH)) {
                    val = val + memberExpense.getAmount();
                }
            }
            entries.add(new Entry(val, i - startOfWeek));
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
