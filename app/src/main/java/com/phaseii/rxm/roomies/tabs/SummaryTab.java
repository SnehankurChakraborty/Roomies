package com.phaseii.rxm.roomies.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.util.RoomiesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_RENT_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_ALIAS;

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

    public static SummaryTab getInstance() {
        return new SummaryTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context mContext = getActivity().getBaseContext();
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
        prepareSpentCard();
        prepareRemainingCard();
        prepareBudgetCard();
        setTypeface(typeface);
        
        return rootView;
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }

    @Override
    public void setTypeface(Typeface typeface) {
        ((TextView) rootView.findViewById(R.id.this_month)).setTypeface(typeface, Typeface.BOLD);
        ((TextView) rootView.findViewById(R.id.last_update)).setTypeface(typeface);
    }

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
        colors.add(Color.parseColor("#3F51B5"));
        colors.add(Color.parseColor("#B6B6B6"));

        PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS, null));
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pie_current_budget);
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(labels, dataSet);

        pieChart.setData(data);
        pieChart.animateXY(1000, 1000);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(String.valueOf((int) status) + "%");
        pieChart.setCenterTextTypeface(typeface);
        pieChart.setCenterTextColor(getResources().getColor(R.color.primary_home));
        pieChart.setHoleColor(getResources().getColor(R.color.white));
        pieChart.setCenterTextSize(25);
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

    private float getPercentageLeft(float total, float spent) {
        float percent = 100f;
        if (spent > 0) {
            percent = 100 - ((spent / total) * 100);
        }
        return percent;
    }

    private float getSpentDetails() {

        float rentSpent = Float.valueOf(sharedPreferences.getString(PREF_RENT_SPENT, "0"));
        float maidSpent = Float.valueOf(sharedPreferences.getString(PREF_MAID_SPENT, "0"));
        float elecSpent = Float.valueOf(sharedPreferences.getString(PREF_ELECTRICITY_SPENT, "0"));
        float miscSpent = Float.valueOf(sharedPreferences.getString(PREF_MISCELLANEOUS_SPENT, "0"));
        return rentSpent + maidSpent + elecSpent + miscSpent;

    }

    private void prepareSpentCard() {
        ((Button) rootView.findViewById(R.id.month_btn_spent)).setText(
                calendar.get(
                        Calendar.DAY_OF_MONTH) + " DAYS");
        ((TextView) rootView.findViewById(R.id.spent_data)).setText(String.valueOf(spent));
    }

    private void prepareRemainingCard() {
        ((Button) rootView.findViewById(R.id.month_btn_remaining)).setText((calendar.
                getActualMaximum(Calendar.DAY_OF_MONTH) - calendar.get(
                Calendar.DAY_OF_MONTH)) + " DAYS");

        float total = rent + maid + electricity + misc;
        ((TextView) rootView.findViewById(R.id.remaining_data)).setText(String.valueOf
                (total - spent));

    }

    private void prepareBudgetCard() {
        String month = RoomiesHelper.getCurrentMonthYear();
        ((Button) rootView.findViewById(R.id.month_btn_budget)).setText(
                month.substring(0, month.length() - 4));

        ((TextView) rootView.findViewById(R.id.rent_budget)).setText(String.valueOf(rent));
        ((TextView) rootView.findViewById(R.id.maid_budget)).setText(String.valueOf(maid));
        ((TextView) rootView.findViewById(R.id.elec_budget)).setText(String.valueOf(electricity));
        ((TextView) rootView.findViewById(R.id.misc_budget)).setText(String.valueOf(misc));
    }

    @Override
    public void update(String username) {
        prepareBudgetGraph(getActivity().getBaseContext());

    }
}
