package com.phaseii.rxm.roomies.tabs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.util.RoomiesConstants;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.EMAIL_ID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_INFO_FILE_KEY;

/**
 * Created by Snehankur on 5/24/2015.
 */
public class TrendsTab extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {

    private final TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams
            .WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

    private Context mContext;

    private ArrayList<BarDataSet> dataSets;
    private float rentSavings;
    private float maidSavings;
    private float electricitySavings;
    private float miscSavings;
    private TableLayout rentTable;
    private TableLayout maidTable;
    private TableLayout electricityTable;
    private TableLayout miscTable;
    private int rowColor;
    private int altRowColor;
    private int currentMonth;
    private int currentYear;
    private Button previous;
    private Button next;
    private static List<RoomStats> roomStatsList = new ArrayList<>();

    public static TrendsTab getInstance(List<RoomStats> roomStatses) {
        if (null != roomStatses) {
            roomStatsList.clear();
            roomStatsList = roomStatses;
        }
        return new TrendsTab();
    }

    @Override
    public View getFragmentView() {
        return rootView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.last_months_tab, container, false);
        mContext = getActivity().getBaseContext();
        rowColor = getResources().getColor(R.color.primary_light);
        altRowColor = getResources().getColor(R.color.accent_light);
        rootView = inflater.inflate(R.layout.last_months_tab, container, false);
        previous = (Button) rootView.findViewById(R.id.previous);
        next = (Button) rootView.findViewById(R.id.next);
        rentTable = (TableLayout) rootView.findViewById(R.id.rent_table);
        maidTable = (TableLayout) rootView.findViewById(R.id.maid_table);
        electricityTable = (TableLayout) rootView.findViewById(R.id.electricity_table);
        miscTable = (TableLayout) rootView.findViewById(R.id.misc_table);

        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        createBarChart(currentMonth);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for (int i = 0; i < 12; i++) {
                    if (currentMonth - i == 1) {
                        currentMonth = i;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    currentMonth = 11;
                }
                createBarChart(currentMonth);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for (int i = 0; i < 12; i++) {
                    if (currentMonth - i == -1) {
                        flag = true;
                        currentMonth = i;
                        break;
                    }
                }
                if (!flag) {
                    currentMonth = 0;
                }
                createBarChart(currentMonth);
            }
        });
        return rootView;
    }

    public void createBarChart(int currentMonth) {
        BarChart barChart = (BarChart) rootView.findViewById(R.id.savings);
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> rentEntries = new ArrayList<>();
        ArrayList<BarEntry> maidEntries = new ArrayList<>();
        ArrayList<BarEntry> elecEntries = new ArrayList<>();
        ArrayList<BarEntry> miscEntries = new ArrayList<>();
        dataSets = new ArrayList<>();

        String username = getActivity().getSharedPreferences(ROOM_INFO_FILE_KEY,
                Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);

        boolean isGoogleFBLogin = getActivity().getSharedPreferences
                (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
        if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
                    (ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
        }

        List<String> months = new ArrayList<>();
        months.add(new DateFormatSymbols().getMonths()[currentMonth] + String.valueOf(
                currentYear));
        if ((currentMonth - 1) >= 0) {
            months.add(new DateFormatSymbols().getMonths()[currentMonth - 1] + String.valueOf(
                    currentYear));
        } else {
            months.add(new DateFormatSymbols().getMonths()[11] + String.valueOf(currentYear - 1));
        }
        if ((currentMonth - 2) >= 0) {
            months.add(new DateFormatSymbols().getMonths()[currentMonth - 2] + String.valueOf(
                    currentYear));
        } else {
            months.add(new DateFormatSymbols().getMonths()[11] + String.valueOf(
                    currentYear - 1));
        }


        int index = 0;

        if (roomStatsList.size() < 3) {
            previous.setEnabled(false);
        } else {
            previous.setEnabled(true);
        }

        if (currentMonth == Calendar.getInstance().get(Calendar.MONTH)) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }

        rentTable.removeAllViews();
        maidTable.removeAllViews();
        electricityTable.removeAllViews();
        miscTable.removeAllViews();

        for (RoomStats roomStats : roomStatsList) {
            rentSavings = roomStats.getRentMargin() - roomStats.getRentSpent();
            maidSavings = roomStats.getMaidMargin() - roomStats.getMaidSpent();
            electricitySavings = roomStats.getElectricityMargin() - roomStats.getElectricitySpent();
            miscSavings = roomStats.getMiscellaneousMargin() - roomStats.getMiscellaneousSpent();
            labels.add(roomStats.getMonthYear());
            rentEntries.add(new BarEntry(rentSavings, index));
            maidEntries.add(new BarEntry(maidSavings, index));
            elecEntries.add(new BarEntry(electricitySavings, index));
            miscEntries.add(new BarEntry(miscSavings, index));
            index++;

            TableRow rentRow = new TableRow(mContext);
            TextView rentMonth = new TextView(mContext);
            rentMonth.setText(roomStats.getMonthYear());

            rentMonth.setLayoutParams(params);
            rentMonth.setTextColor(Color.BLACK);
            TextView rentValue = new TextView(mContext);
            rentValue.setText(String.valueOf(rentSavings));
            rentValue.setLayoutParams(params);
            rentValue.setTextColor(Color.BLACK);
            rentMonth.setGravity(Gravity.CENTER);
            rentValue.setGravity(Gravity.CENTER);
            rentRow.addView(rentMonth);
            rentRow.addView(rentValue);
            if (index % 2 == 0) {
                rentRow.setBackgroundColor(altRowColor);
            } else {
                rentRow.setBackgroundColor(rowColor);
            }
            rentTable.addView(rentRow);

            TableRow maidRow = new TableRow(mContext);
            TextView maidMonth = new TextView(mContext);
            maidMonth.setText(roomStats.getMonthYear());
            maidMonth.setLayoutParams(params);
            maidMonth.setTextColor(Color.BLACK);
            TextView maidValue = new TextView(mContext);
            maidValue.setText(String.valueOf(maidSavings));
            maidValue.setLayoutParams(params);
            maidValue.setTextColor(Color.BLACK);
            maidMonth.setGravity(Gravity.CENTER);
            maidValue.setGravity(Gravity.CENTER);
            maidRow.addView(maidMonth);
            maidRow.addView(maidValue);
            if (index % 2 == 0) {
                maidRow.setBackgroundColor(altRowColor);
            } else {
                maidRow.setBackgroundColor(rowColor);
            }
            maidTable.addView(maidRow);

            TableRow electricityRow = new TableRow(mContext);
            TextView electricityMonth = new TextView(mContext);
            electricityMonth.setText(roomStats.getMonthYear());
            electricityMonth.setLayoutParams(params);
            electricityMonth.setTextColor(Color.BLACK);
            TextView electricityValue = new TextView(mContext);
            electricityValue.setText(String.valueOf(electricitySavings));
            electricityValue.setLayoutParams(params);
            electricityValue.setTextColor(Color.BLACK);
            electricityMonth.setGravity(Gravity.CENTER);
            electricityValue.setGravity(Gravity.CENTER);
            electricityRow.addView(electricityMonth);
            electricityRow.addView(electricityValue);
            if (index % 2 == 0) {
                electricityRow.setBackgroundColor(altRowColor);
            } else {
                electricityRow.setBackgroundColor(rowColor);
            }
            electricityTable.addView(electricityRow);

            TableRow miscRow = new TableRow(mContext);
            TextView miscMonth = new TextView(mContext);
            miscMonth.setText(roomStats.getMonthYear());
            miscMonth.setLayoutParams(params);
            miscMonth.setTextColor(Color.BLACK);
            TextView miscValue = new TextView(mContext);
            miscValue.setText(String.valueOf(miscSavings));
            miscValue.setLayoutParams(params);
            miscValue.setTextColor(Color.BLACK);
            miscMonth.setGravity(Gravity.CENTER);
            miscValue.setGravity(Gravity.CENTER);
            miscRow.addView(miscMonth);
            if (index % 2 == 0) {
                miscRow.setBackgroundColor(altRowColor);
            } else {
                miscRow.setBackgroundColor(rowColor);
            }
            miscRow.addView(miscValue);

            miscTable.addView(miscRow);
        }
        final BarDataSet rentDataSet = new BarDataSet(rentEntries, "Rent");
        rentDataSet.setBarShadowColor(Color.TRANSPARENT);
        rentDataSet.setColor(ColorTemplate.JOYFUL_COLORS[0]);
        dataSets.add(rentDataSet);
        final BarDataSet maidDataSet = new BarDataSet(maidEntries, "Maid");
        maidDataSet.setBarShadowColor(Color.TRANSPARENT);
        maidDataSet.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        dataSets.add(maidDataSet);
        final BarDataSet elecDataSet = new BarDataSet(elecEntries, "Elec");
        elecDataSet.setBarShadowColor(Color.TRANSPARENT);
        elecDataSet.setColor(ColorTemplate.JOYFUL_COLORS[2]);
        dataSets.add(elecDataSet);
        final BarDataSet miscDataSet = new BarDataSet(miscEntries, "Misc");
        miscDataSet.setBarShadowColor(Color.TRANSPARENT);
        miscDataSet.setColor(ColorTemplate.JOYFUL_COLORS[3]);
        dataSets.add(miscDataSet);

        BarData data = new BarData(labels, dataSets);
        barChart.setData(data);
        barChart.animateY(500);
        barChart.setDrawValueAboveBar(true);
        barChart.setDescription("");
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(true);
        barChart.setScaleMinima(1f, 1f);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setHighlightPerTapEnabled(false);

        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        YAxis yl = barChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setSpaceTop(10f);
        yl.setStartAtZero(false);
        yl.setShowOnlyMinMax(true);
        yl.setValueFormatter(new LargeValueFormatter());

        barChart.getAxisRight().setEnabled(false);

        XAxis xl = barChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
    }


    private void setTextColor(TextView field, TextView fieldVal, float value) {
        int green = getResources().getColor(R.color.green);
        int red = getResources().getColor(R.color.red);
        if (value > 0) {
            field.setTextColor(green);
            fieldVal.setTextColor(green);
        } else if (value < 0) {
            field.setTextColor(red);
            fieldVal.setTextColor(red);
        } else {
            field.setTextColor(Color.rgb(252, 180, 71));
            fieldVal.setTextColor(Color.rgb(252, 180, 71));
        }
    }


    @Override
    public void update(RoomExpenses expenses) {

    }
}
