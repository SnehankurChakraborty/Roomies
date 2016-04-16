package com.phaseii.rxm.roomies.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snehankur on 5/24/2015.
 */
public class TrendsTab extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {

    private static List<RoomStats> roomStatsList = new ArrayList<>();
    private Context mContext;
    private List<String> months;
    private BarChart rentChart;
    private BarChart maidChart;
    private BarChart elecChart;
    private BarChart miscChart;

    /**
     *
     * @param roomStatses
     * @return
     */
    public static TrendsTab getInstance(List<RoomStats> roomStatses) {
        if (null != roomStatses) {
            roomStatsList = roomStatses;
        }
        return new TrendsTab();
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }

    /**
     * @return fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_trends, container, false);
        mContext = getActivity().getBaseContext();
        months = DateUtils.getLastTwoMonths();
        rentChart = (BarChart) rootView.findViewById(R.id.rent_savings);
        maidChart = (BarChart) rootView.findViewById(R.id.maid_savings);
        elecChart = (BarChart) rootView.findViewById(R.id.elec_savings);
        miscChart = (BarChart) rootView.findViewById(R.id.misc_savings);
        createBarChart(rentChart, Constants.RENT, roomStatsList);
        createBarChart(maidChart, Constants.MAID, roomStatsList);
        createBarChart(elecChart, Constants.ELECTRICITY, roomStatsList);
        createBarChart(miscChart, Constants.MISC, roomStatsList);
        return rootView;
    }

    /**
     *
     * @param chart
     * @param category
     * @param roomStatsList
     */
    private void createBarChart(BarChart chart, String category, List<RoomStats> roomStatsList) {
        int index = 0;
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        if (Constants.RENT.equals(category)) {
            for (String month : months) {
                for (RoomStats roomStats : roomStatsList) {
                    if (roomStats.getMonthYear().equals(month)) {
                        entries.add(
                                new BarEntry(roomStats.getRentMargin() - roomStats.getRentSpent(),
                                        index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    } else {
                        entries.add(new BarEntry(0, index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    }
                    index++;
                }
            }
        } else if (Constants.MAID.equals(category)) {
            for (String month : months) {
                for (RoomStats roomStats : roomStatsList) {
                    if (roomStats.getMonthYear().equals(month)) {
                        entries.add(
                                new BarEntry(roomStats.getMaidMargin() - roomStats.getMaidSpent(),
                                        index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    } else {
                        entries.add(new BarEntry(0, index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    }
                    index++;
                }
            }
        } else if (Constants.ELECTRICITY.equals(category)) {
            for (String month : months) {
                for (RoomStats roomStats : roomStatsList) {
                    if (roomStats.getMonthYear().equals(month)) {
                        entries.add(new BarEntry(
                                roomStats.getElectricityMargin() - roomStats.getElectricitySpent(),
                                index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    } else {
                        entries.add(new BarEntry(0, index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    }
                    index++;
                }
            }
        } else if (Constants.MISC.equals(category)) {
            for (String month : months) {
                for (RoomStats roomStats : roomStatsList) {
                    if (roomStats.getMonthYear().equals(month)) {
                        entries.add(new BarEntry(roomStats.getMiscellaneousMargin() - roomStats
                                .getMiscellaneousSpent(), index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    } else {
                        entries.add(new BarEntry(0, index));
                        labels.add(month.substring(0, 3) + month
                                .substring(month.length() - 2, month.length()));
                    }
                    index++;
                }
            }
        }
        BarDataSet dataset = new BarDataSet(entries, category);
        dataset.setColor(Color.WHITE);
        dataset.setBarShadowColor(Color.GRAY);
        dataset.setValueTextSize(10);
        dataset.setValueTextColor(Color.WHITE);
        dataset.setValueTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf"));

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.animateY(500);
        chart.setDrawValueAboveBar(true);
        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.setScaleMinima(1f, 1f);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.getLegend().setEnabled(false);

        /**format the left y axis**/
        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setEnabled(false);
        yl.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf"));
        yl.setTextColor(Color.WHITE);

        /**format the right y axis**/
        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setEnabled(false);
        yr.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf"));
        yr.setTextColor(Color.WHITE);

        /**format the bottom x axis*/
        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setEnabled(true);
        xl.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf"));
        xl.setTextSize(7);
        xl.setTextColor(Color.WHITE);
    }

    /**
     *
     * @param expenses
     */
    @Override
    public void update(RoomExpenses expenses) {

        String category = expenses.getExpenseCategory();
        String month = DateUtils.getCurrentMonthYear();
        for (RoomStats roomStats : roomStatsList) {
            if (month.equals(roomStats.getMonthYear())) {
                int spent = 0;
                if (category.equals(Constants.RENT)) {
                    spent = roomStats.getRentSpent();
                    roomStats.setRentSpent(spent + (int) expenses.getAmount());
                } else if (category.equals(Constants.MAID)) {
                    spent = roomStats.getMaidSpent();
                    roomStats.setMaidSpent(spent + (int) expenses.getAmount());
                } else if (category.equals(Constants.ELECTRICITY)) {
                    spent = roomStats.getElectricitySpent();
                    roomStats.setElectricitySpent(spent + (int) expenses.getAmount());
                } else if (category.equals(Constants.MISC)) {
                    spent = roomStats.getMiscellaneousSpent();
                    roomStats.setMiscellaneousSpent(spent + (int) expenses.getAmount());
                }
            }
        }
        createBarChart(rentChart, Constants.RENT, roomStatsList);
        createBarChart(maidChart, Constants.MAID, roomStatsList);
        createBarChart(elecChart, Constants.ELECTRICITY, roomStatsList);
        createBarChart(miscChart, Constants.MISC, roomStatsList);
    }
}
