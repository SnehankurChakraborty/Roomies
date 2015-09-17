package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phaseii.rxm.roomies.R;

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
public class SummaryFragment extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {

    private Typeface typeface;
    private TextView spentData;
    private TextView remainingData;
    private SharedPreferences sharedPreferences;

    public static SummaryFragment getInstance() {
        return new SummaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context mContext = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        spentData = (TextView) rootView.findViewById(R.id.spent_data);
        remainingData = (TextView) rootView.findViewById(R.id.remaining_data);
        TextView remainingDays = (TextView) rootView.findViewById(R.id.remaining_days);
        remainingDays.setText((Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
                - Calendar.getInstance().get(Calendar.MONTH)) + " more days left in this month ");
        remainingDays.setTypeface(typeface);
        ((TextView) rootView.findViewById(R.id.last_update)).setTypeface(typeface);
        typeface = Typeface.createFromAsset(getActivity().getBaseContext().getAssets(),
                "fonts/VarelaRound-Regular.ttf");
        sharedPreferences = mContext.getSharedPreferences(PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE);

        showBarGraph(getActivity().getBaseContext());

        TextView month = (TextView) rootView.findViewById(R.id.this_month);
        month.setTypeface(typeface, Typeface.BOLD);
        ((TextView) rootView.findViewById(R.id.last_update)).setTypeface(typeface);
        return rootView;
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }

    private PieChart showBarGraph(Context context) {

        float rent = Float.valueOf(sharedPreferences.getString(PREF_RENT_MARGIN, "0"));
        float maid = Float.valueOf(sharedPreferences.getString(PREF_MAID_MARGIN, "0"));
        float electricity = Float.valueOf(
                sharedPreferences.getString(PREF_ELECTRICITY_MARGIN, "0"));
        float misc = Float.valueOf(sharedPreferences.getString(PREF_MISCELLANEOUS_MARGIN, "0"));
        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        float total = rent + maid + electricity + misc;
        float spent = getSpentDetails();
        float status = getPercentageLeft(total, spent);
        List<Integer> colors = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            entries.add(new Entry(1, i - 1));
            labels.add(i - 1, String.valueOf(i));
        }

        for (int i = 0; i < 100 - Math.round(status); i++) {
            colors.add(Color.parseColor("#0091ea"));
        }

        for (int i = 100 - Math.round(status); i < 100; i++) {
            colors.add(Color.parseColor("#ACABAB"));
        }


        remainingData.setText(String.valueOf(total));
        spentData.setText(String.valueOf(spent));

        PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS, null));
        PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_current_budget);
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        PieData data = new PieData(labels, dataSet);

        mChart.setData(data);
        mChart.animateXY(1000, 1000);
        mChart.setDrawCenterText(true);
        mChart.setCenterText(String.valueOf((int) status) + "%");
        mChart.setCenterTextTypeface(typeface);
        mChart.setCenterTextColor(getResources().getColor(R.color.accent));
        mChart.setHoleColor(getResources().getColor(R.color.white));
        mChart.setCenterTextSize(25);
        mChart.setRotationEnabled(false);
        mChart.setDescription("");
        mChart.setClickable(false);
        mChart.setDrawSliceText(false);
        mChart.getLegend().setEnabled(false);
        return mChart;
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

    @Override
    public void update(String username) {
        showBarGraph(getActivity().getBaseContext());

    }
}
