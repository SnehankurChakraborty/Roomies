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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.utils.ColorUtils;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.utils.Constants.ROOM_ALIAS;

/**
 * Created by Snehankur on 9/17/2015.
 */
public class SummaryTab extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {

    private Typeface typeface;
    private SharedPreferences sharedPreferences;
    private float rent;
    private float maid;
    private float electricity;
    private float misc;
    private float spent;
    private Calendar calendar;
    private String rs;
    private Context mContext;

    /**
     * @return
     */
    public static SummaryTab getInstance() {
        return new SummaryTab();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mContext = getActivity().getBaseContext();
        rs = getResources().getString(R.string.Rs);
        rootView = inflater.inflate(R.layout.tab_summary, container, false);
        typeface = Typeface.createFromAsset(getActivity().getBaseContext().getAssets(),
                "fonts/VarelaRound-Regular.ttf");
        sharedPreferences = mContext.getSharedPreferences(PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();

        rent = Float.valueOf(sharedPreferences.getString(PREF_RENT_MARGIN, "0"));
        maid = Float.valueOf(sharedPreferences.getString(PREF_MAID_MARGIN, "0"));
        electricity = Float.valueOf(
                sharedPreferences.getString(PREF_ELECTRICITY_MARGIN, "0"));
        misc = Float.valueOf(sharedPreferences.getString(PREF_MISCELLANEOUS_MARGIN, "0"));
        spent = getSpentDetails();

        prepareBudgetGraph(mContext);
        prepareStatsCard();
        prepareBudgetCard();
        setTypeface(typeface);

        return rootView;
    }

    /**
     * @return
     */
    @Override
    public View getFragmentView() {
        return rootView;
    }

    /**
     * @param typeface
     */
    @Override
    public void setTypeface(Typeface typeface) {

    }

    /**
     * @param context
     */
    private void prepareBudgetGraph(Context context) {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        float total = rent + maid + electricity + misc;
        float spent = getSpentDetails();
        float status = getPercentageLeft(total, spent);
        List<Integer> colors = new ArrayList<>();
        entries.add(new Entry(spent, 0));
        entries.add(new Entry(total - spent, 1));
        labels.add("Spent");
        labels.add("Left");

        PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS, null));
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pie_current_budget);
        dataSet.setColors(ColorUtils.getSummaryColor(mContext));
        dataSet.setDrawValues(false);
        PieData data = new PieData(labels, dataSet);

        pieChart.setData(data);
        pieChart.animateXY(1000, 1000);
        pieChart.setCenterTextTypeface(typeface);
        pieChart.setCenterText(
                RoomiesHelper.generateCenterSpannableText(getActivity(), (int) status, false));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setRotationEnabled(false);
        pieChart.setDescription("");
        pieChart.setClickable(false);
        pieChart.setDrawSliceText(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setTouchEnabled(false);

        ((TextView) rootView.findViewById(R.id.remaining_days)).setText(String.valueOf(
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        - Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + " more " +
                "days to go");
    }


    /**
     * @param total
     * @param spent
     * @return
     */
    private float getPercentageLeft(float total, float spent) {
        float percent = 100f;
        if (spent > 0) {
            percent = 100 - ((spent / total) * 100);
        }
        return percent;
    }

    /**
     * @return
     */
    private float getSpentDetails() {

        float rentSpent = Float.valueOf(sharedPreferences.getString(PREF_RENT_SPENT, "0"));
        float maidSpent = Float.valueOf(sharedPreferences.getString(PREF_MAID_SPENT, "0"));
        float elecSpent = Float.valueOf(sharedPreferences.getString(PREF_ELECTRICITY_SPENT, "0"));
        float miscSpent = Float.valueOf(sharedPreferences.getString(PREF_MISCELLANEOUS_SPENT, "0"));
        return rentSpent + maidSpent + elecSpent + miscSpent;

    }

    /**
     *
     */
    private void prepareStatsCard() {
        ((TextView) rootView.findViewById(R.id.spent_value)).setText(rs + String.valueOf(spent));
        float total = rent + maid + electricity + misc;
        ((TextView) rootView.findViewById(R.id.remaining_value)).setText(rs + String.valueOf
                (total - spent));

        View spentIndex = rootView.findViewById(R.id.spent_index);
        View remainingIndex = rootView.findViewById(R.id.remaining_index);

        ShapeDrawable dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(getResources().getColor(R.color.primary_home));
        spentIndex.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(getResources().getColor(R.color.secondary_text));
        remainingIndex.setBackgroundDrawable(dot);
    }


    /**
     *
     */
    private void prepareBudgetCard() {
        String month = DateUtils.getCurrentMonthYear();
        ((Button) rootView.findViewById(R.id.month_btn_budget)).setText(
                month.substring(0, month.length() - 4));

        ((TextView) rootView.findViewById(R.id.rent_budget)).setText(rs +
                String.valueOf((int) rent));
        ((TextView) rootView.findViewById(R.id.maid_budget)).setText(rs +
                String.valueOf((int) maid));
        ((TextView) rootView.findViewById(R.id.elec_budget)).setText(rs +
                String.valueOf((int) electricity));
        ((TextView) rootView.findViewById(R.id.misc_budget)).setText(rs +
                String.valueOf((int) misc));
    }

    /**
     * @param expenses
     */
    @Override
    public void update(RoomExpenses expenses) {
        prepareBudgetGraph(getActivity().getBaseContext());

    }
}
