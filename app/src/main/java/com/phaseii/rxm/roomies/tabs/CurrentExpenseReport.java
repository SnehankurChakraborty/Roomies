package com.phaseii.rxm.roomies.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dao.MiscServiceImpl;
import com.phaseii.rxm.roomies.dao.RoomServiceImpl;
import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.model.MiscExpense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.EMAIL_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_ELEC_PAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_MAID_PAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_RENT_PAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.NAME;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;

/**
 * Created by Snehankur on 4/3/2015.
 */
public class CurrentExpenseReport extends RoomiesFragment
        implements RoomiesFragment.UpdatableFragment {

    private SharedPreferences sharedPreferences;
    private ShapeDrawable dot;
    private Context mContext;

    public static CurrentExpenseReport getInstance() {
        return new CurrentExpenseReport();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_current_expense_report, container, false);
        mContext = getActivity().getBaseContext();
        View rentDot = rootView.findViewById(R.id.rent_mark);
        View maidDot = rootView.findViewById(R.id.maid_mark);
        View electricityDot = rootView.findViewById(R.id.electricity_mark);
        View miscDot = rootView.findViewById(R.id.misc_mark);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(ColorTemplate.JOYFUL_COLORS[0]);
        rentDot.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(ColorTemplate.JOYFUL_COLORS[1]);
        maidDot.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(ColorTemplate.JOYFUL_COLORS[2]);
        electricityDot.setBackgroundDrawable(dot);

        dot = new ShapeDrawable(new OvalShape());
        dot.getPaint().setColor(ColorTemplate.JOYFUL_COLORS[3]);
        miscDot.setBackgroundDrawable(dot);

        createPieChart(mContext);
        createBarChart();
        return rootView;
    }

    public void createBarChart() {
        MiscServiceImpl miscService = new MiscServiceImpl(mContext);
        BarChart barChart = (BarChart) rootView.findViewById(R.id.summary);
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        String username = getActivity().getSharedPreferences(ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);

        boolean isGoogleFBLogin = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
        if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
                    (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
        }

        List<MiscExpense> miscExpenses = miscService.getCurrentTotalMiscExpense(username);
        float grocery = 0f;
        float vegetables = 0f;
        float others = 0f;
        float bills = 0f;
        String types[] = mContext.getResources().getStringArray(R.array.subcategory);
        for (MiscExpense miscExpense : miscExpenses) {
            String type = miscExpense.getType();
            if (type.equals(types[0])) {
                bills = bills + miscExpense.getAmount();
            } else if (type.equals(types[1])) {
                grocery = grocery + miscExpense.getAmount();
            } else if (type.equals(types[2])) {
                vegetables = vegetables + miscExpense.getAmount();
            } else if (type.equals(types[3])) {
                others = others + miscExpense.getAmount();
            }
        }
        labels.addAll(Arrays.asList(types));
        entries.add(new BarEntry(bills, 0));
        entries.add(new BarEntry(grocery, 1));
        entries.add(new BarEntry(vegetables, 2));
        entries.add(new BarEntry(others, 3));
        BarDataSet dataset = new BarDataSet(entries, "Summary");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        dataset.setBarShadowColor(Color.TRANSPARENT);
        dataset.setValueTextSize(10);
        dataset.setValueTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));

        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.animateY(500);
        barChart.setDrawValueAboveBar(true);

        barChart.setDescription("");
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setEnabled(false);

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
        yr.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular" +
                ".ttf"));
        yr.setTextSize(20);


        XAxis xl = barChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular.ttf"));
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }

    private PieChart createPieChart(Context context) {
        PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_expense_report);
        RoomServiceImpl roomService = new RoomServiceImpl(getActivity());
        String username = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(NAME, null);

        boolean isGoogleFBLogin = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
        if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
                    (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
        }

        Cursor cursor = roomService.getRoomDetails(username);
        cursor.moveToFirst();
        float rent = cursor.getFloat(
                cursor.getColumnIndex(RoomiesContract.Room_Expenses.COLUMN_RENT));
        float maid = cursor.getFloat(cursor.getColumnIndex(RoomiesContract.Room_Expenses
                .COLUMN_MAID));
        float electricity = cursor.getFloat(cursor.getColumnIndex(RoomiesContract.Room_Expenses
                .COLUMN_ELECTRICITY));
        float misc = cursor.getFloat(cursor.getColumnIndex(RoomiesContract.Room_Expenses
                .COLUMN_MISCELLANEOUS));
        sharedPreferences = context.getSharedPreferences(
                ROOM_BUDGET_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        float spent = rent + maid + electricity + misc;
        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        if (rent > 0) {
            entries.add(new Entry(rent, 0));
            labels.add(RENT);
            mEditor.putBoolean(IS_RENT_PAID, true);
            mEditor.apply();
        }
        if (maid > 0) {
            entries.add(new Entry(maid, 1));
            labels.add(MAID);
            mEditor.putBoolean(IS_MAID_PAID, true);
            mEditor.apply();
        }
        if (electricity > 0) {
            entries.add(new Entry(electricity, 2));
            labels.add(ELECTRICITY);
            mEditor.putBoolean(IS_ELEC_PAID, true);
            mEditor.apply();
        }
        if (misc > 0) {
            entries.add(new Entry(misc, 3));
            labels.add(MISC);
        }
        float total = getTotal();
        if (total <= 0) {
            entries.add(new Entry(0, 0));
            labels.add("NO SPENDS");
        }

        TextView rentVal = (TextView) rootView.findViewById(R.id.rent_value);
        rentVal.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        rentVal.setText(String.valueOf(rent) + " Rent");

        TextView maidVal = (TextView) rootView.findViewById(R.id.maid_value);
        maidVal.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        maidVal.setText(String.valueOf(maid) + " Maid");

        TextView elecVal = (TextView) rootView.findViewById(R.id.electricity_value);
        elecVal.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        elecVal.setText(String.valueOf(electricity) + " Electricity");

        TextView miscVal = (TextView) rootView.findViewById(R.id.misc_value);
        miscVal.setTypeface(
                Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf"));
        miscVal.setText(String.valueOf(misc) + " Misc");

        sharedPreferences = context.getSharedPreferences(
                ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
        PieDataSet dataSet = new PieDataSet(entries,
                sharedPreferences.getString(ROOM_ALIAS, ""));
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setDrawValues(false);
        PieData data = new PieData(labels, dataSet);
        mChart.setData(data);
        mChart.animateXY(1000, 1000);
        mChart.setDrawCenterText(true);
        mChart.setCenterText(String.valueOf((int) getPercentageLeft(total, spent)) + "%");
        mChart.setDescription("");
        mChart.setClickable(true);
        mChart.setNoDataText("");
        mChart.setCenterTextColor(Color.BLACK);
        mChart.setCenterTextSize(30);
        mChart.setCenterTextTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/VarelaRound-Regular.ttf"));
        mChart.getLegend().setEnabled(false);
        mChart.setDrawSliceText(false);
        return mChart;
    }

    private float getPercentageLeft(float total, float expense) {
        float percent = 0f;
        if (expense > 0) {
            percent = (expense / total) * 100;
        }
        return percent;
    }

    private float getTotal() {
        float rent = Float.valueOf(sharedPreferences.getString(RENT_MARGIN, "0"));
        float maid = Float.valueOf(sharedPreferences.getString(MAID_MARGIN, "0"));
        float electricity = Float.valueOf(sharedPreferences.getString(ELECTRICITY_MARGIN, "0"));
        float misc = Float.valueOf(sharedPreferences.getString(MISC_MARGIN, "0"));
        return rent + maid + electricity + misc;
    }


    @Override
    public void update(String username) {
        createPieChart(getActivity().getBaseContext());
    }
}
