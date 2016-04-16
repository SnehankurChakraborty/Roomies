package com.phaseii.rxm.roomies.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.exception.RoomiesStateException;
import com.phaseii.rxm.roomies.utils.Category;
import com.phaseii.rxm.roomies.utils.ColorUtils;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;
import com.phaseii.rxm.roomies.utils.SubCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.phaseii.rxm.roomies.utils.Constants.EMAIL_ID;
import static com.phaseii.rxm.roomies.utils.Constants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.utils.Constants.NAME;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.utils.Constants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.utils.Constants.ROOM_INFO_FILE_KEY;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class MonthlyTab extends RoomiesFragment
        implements RoomiesFragment.UpdatableFragment {

    private static List<RoomExpenses> roomExpensesList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private ShapeDrawable dot;
    private Context mContext;

    public static MonthlyTab getInstance(List<RoomExpenses> roomExpenses) {
        if (null != roomExpenses) {
            roomExpensesList = roomExpenses;
        }
        return new MonthlyTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_monthly, container, false);
        mContext = getActivity().getBaseContext();
        sharedPreferences = mContext.getSharedPreferences(
                PREF_ROOMIES_KEY, Context.MODE_PRIVATE);

        View rentDot = rootView.findViewById(R.id.rent_mark);
        View maidDot = rootView.findViewById(R.id.maid_mark);
        View electricityDot = rootView.findViewById(R.id.electricity_mark);
        View miscDot = rootView.findViewById(R.id.misc_mark);
        int[] colorTemplate = ColorUtils.getCategoryColor(mContext);


        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(colorTemplate[0]);
        rentDot.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(colorTemplate[1]);
        maidDot.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(colorTemplate[2]);
        electricityDot.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(colorTemplate[3]);
        miscDot.setBackgroundDrawable(dot);

        createPieChart(roomExpensesList);
        createBarChart(roomExpensesList);
        return rootView;
    }

    public void createBarChart(List<RoomExpenses> roomExpensesList) {
        HorizontalBarChart barChart = (HorizontalBarChart) rootView.findViewById(R.id.summary);
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        String username = getActivity().getSharedPreferences(ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString(Constants.NAME, null);

        boolean isGoogleFBLogin = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
        if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
                    (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
        }

        int grocery = 0;
        int vegetables = 0;
        int others = 0;
        int bills = 0;
        int entertainment = 0;
        for (RoomExpenses roomExpenses : roomExpensesList) {
            if (Category.getCategory(
                    roomExpenses.getExpenseCategory()).equals(Category.MISCELLANEOUS)) {
                switch (SubCategory.getSubcategory(
                        roomExpenses.getExpenseSubcategory())) {
                    case BILLS:
                        bills = bills + roomExpenses.getAmount();
                        break;
                    case GROCERY:
                        grocery = grocery + roomExpenses.getAmount();
                        break;
                    case FOOD:
                        vegetables = vegetables + roomExpenses.getAmount();
                        break;
                    case ENTERTAINMENT:
                        entertainment = entertainment + roomExpenses.getAmount();
                        break;
                    case OTHERS:
                        others = others + roomExpenses.getAmount();
                        break;
                    default:
                        throw new RoomiesStateException("Unsupported sub category");
                }
            }
        }
        List<String> labelList = Arrays.asList(SubCategory.getSubCategories());
        Collections.reverse(labelList);
        labels.addAll(labelList);
        entries.add(new BarEntry(bills, 4));
        entries.add(new BarEntry(grocery, 3));
        entries.add(new BarEntry(vegetables, 2));
        entries.add(new BarEntry(entertainment, 1));
        entries.add(new BarEntry(others, 0));
        BarDataSet dataset = new BarDataSet(entries, "Summary");
        dataset.setColors(ColorUtils.getSubcategoryColor(mContext));
        dataset.setBarShadowColor(Color.TRANSPARENT);
        dataset.setValueTextSize(10);
        dataset.setValueTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf"));
        dataset.setHighlightEnabled(false);

        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.animateY(500);
        barChart.setDrawValueAboveBar(true);

        barChart.setDescription("");
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setHighlightPerTapEnabled(false);
        barChart.setPinchZoom(false);

        YAxis yl = barChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setEnabled(false);
        yl.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        yl.setTextSize(20);

        YAxis yr = barChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setEnabled(false);
        yr.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        yr.setTextSize(20);


        XAxis xl = barChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        yr.setTextSize(20);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setAxisLineWidth(3f);
        xl.setEnabled(true);
        xl.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }

    private PieChart createPieChart(List<RoomExpenses> roomExpensesList) {
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pie_expense_report);
        String username = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(NAME, null);

        boolean isGoogleFBLogin = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
        if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
                    (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
        }
        int rent = 0;
        int maid = 0;
        int electricity = 0;
        int misc = 0;
        for (RoomExpenses roomExpenses : roomExpensesList) {
            switch (Category.getCategory(roomExpenses.getExpenseCategory())) {
                case RENT:
                    rent = rent + roomExpenses.getAmount();
                    break;
                case MAID:
                    maid = maid + roomExpenses.getAmount();
                    break;
                case ELECTRICITY:
                    electricity = electricity + roomExpenses.getAmount();
                    break;
                case MISCELLANEOUS:
                    misc = misc + roomExpenses.getAmount();
                    break;
            }
        }

        int spent = rent + maid + electricity + misc;
        ArrayList<Entry> entries = new ArrayList<Entry>();
        List<String> labels = new ArrayList<String>();

        if (spent > 0) {
            entries.add(new Entry(rent, 0));
            entries.add(new Entry(maid, 1));
            entries.add(new Entry(electricity, 2));
            entries.add(new Entry(misc, 3));
            labels = Arrays.asList(Category.getCategories());
        } else {
            entries.add(new Entry(1, 0));
            labels.add("NO SPENDS");
        }

        float total = getTotal();
        if (total <= 0) {
            entries.add(new Entry(0, 0));
            labels.add("NO SPENDS");
        }

        TextView rentVal = (TextView) rootView.findViewById(R.id.rent_value);
        rentVal.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        rentVal.setText(String.valueOf(rent) + " " + Constants.RENT_SMALL);

        TextView maidVal = (TextView) rootView.findViewById(R.id.maid_value);
        maidVal.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        maidVal.setText(String.valueOf(maid) + " " + Constants.MAID_SMALL);

        TextView elecVal = (TextView) rootView.findViewById(R.id.electricity_value);
        elecVal.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        elecVal.setText(String.valueOf(electricity) + " " + Constants.ELECTRICITY_SMALL);

        TextView miscVal = (TextView) rootView.findViewById(R.id.misc_value);
        miscVal.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        miscVal.setText(String.valueOf(misc) + " " + Constants.MISC_SMALL);


        PieDataSet dataSet = new PieDataSet(entries,
                sharedPreferences.getString(ROOM_ALIAS, ""));
        if (spent != 0) {
            dataSet.setColors(ColorUtils.getCategoryColor(mContext));
        } else {
            dataSet.setColors(ColorUtils.getBlankColor(mContext));
        }
        dataSet.setDrawValues(false);
        PieData data = new PieData(labels, dataSet);
        pieChart.setData(data);
        pieChart.animateXY(1000, 1000);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(RoomiesHelper
                .generateCenterSpannableText(getActivity(), (int) getPercentageLeft(total, spent),
                        true));
        pieChart.setDescription("");
        pieChart.setClickable(true);
        pieChart.setNoDataText("");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextTypeface(Typeface.createFromAsset(mContext.getAssets(),
                "fonts/VarelaRound-Regular.ttf"));
        pieChart.getLegend().setEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setTouchEnabled(false);
        return pieChart;
    }

    private float getPercentageLeft(float total, float expense) {
        float percent = 0f;
        if (expense > 0) {
            percent = (expense / total) * 100;
        }
        return percent;
    }

    private float getTotal() {
        float rent = Float.valueOf(sharedPreferences.getString(PREF_RENT_MARGIN, "0"));
        float maid = Float.valueOf(sharedPreferences.getString(PREF_MAID_MARGIN, "0"));
        float electricity = Float.valueOf(sharedPreferences.getString(PREF_ELECTRICITY_MARGIN,
                "0"));
        float misc = Float.valueOf(sharedPreferences.getString(PREF_MISCELLANEOUS_MARGIN, "0"));
        return rent + maid + electricity + misc;
    }


    @Override
    public void update(RoomExpenses expenses) {
        roomExpensesList.add(expenses);
        createPieChart(roomExpensesList);
        createBarChart(roomExpensesList);
    }
}
