package com.phaseii.rxm.roomies.tabs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.dialogs.DetailExpenseDialog;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomExpenses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.EMAIL_ID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_INFO_FILE_KEY;


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
        String username = getActivity().getSharedPreferences(ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);

        boolean isGoogleFBLogin = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
        if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
                    (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
        }

        String roomId = getActivity().getSharedPreferences(PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE).getString(
                RoomiesConstants.PREF_ROOM_ID, null);
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> params = new ArrayList<QueryParam>();
        List<String> selectionArgs = new ArrayList<String>();

        params.add(QueryParam.ROOMID);
        paramMap.put(ServiceParam.SELECTION, params);

        selectionArgs.add(roomId);
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        paramMap.put(ServiceParam.QUERYARGS, QueryParam.MONTH_YEAR);

        RoomiesDao service = new RoomExpensesDaoImpl(getActivity());

        final List<RoomExpenses> roomExpensesList = (List<RoomExpenses>) service.getDetails
                (paramMap);
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

        lineChart.setData(getLineChart(roomExpensesList));
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(500);
        lineChart.animateX(500);
        lineChart.setNoDataText("No Miscellaneous expenses yet");
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                DialogFragment dialog = DetailExpenseDialog.getInstance(roomExpensesList,
                        getActivity().getBaseContext());
                dialog.show(getActivity().getSupportFragmentManager(), "detailExpense");
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private LineData getLineChart(List<RoomExpenses> roomExpensesList) {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<>();
        if (roomExpensesList.size() > 0) {
            cal.setTime(roomExpensesList.get(0).getExpenseDate());
            start = cal.get(Calendar.DAY_OF_MONTH);
            for (RoomExpenses misc : roomExpensesList) {
                cal.setTime(misc.getExpenseDate());
                day = cal.get(Calendar.DAY_OF_MONTH);
                if (!labels.contains(String.valueOf(day))) {
                    labels.add(String.valueOf(day));
                    entries.add(new Entry(misc.getAmount(), (day - start)));
                } else {
                    for (Entry entry : entries) {
                        if (entry.getXIndex() == (day - start)) {
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
    public void update(String username) {
        createCombinedChart();
    }
}
