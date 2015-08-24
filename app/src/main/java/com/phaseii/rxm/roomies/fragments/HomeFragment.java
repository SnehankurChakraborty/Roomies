package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.view.RoomiesHomePagerAdapter;
import com.phaseii.rxm.roomies.view.RoomiesSlidingTabLayout;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_RENT_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;


public class HomeFragment extends RoomiesFragment implements RoomiesFragment.UpdatableFragment {

    private ViewPager pager;
    private RoomiesSlidingTabLayout tabs;
    private RoomiesHomePagerAdapter adapter;
    private int numboftabs = 2;
    private String titles[] = {"Room Budget", "Room Expense"};
    private Typeface typeface;
    private TextView spentData;
    private TextView remainingData;
    private SharedPreferences sharedPreferences;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        spentData = (TextView) rootView.findViewById(R.id.spent_data);
        remainingData = (TextView) rootView.findViewById(R.id.remaining_data);
        ((TextView) rootView.findViewById(R.id.spent_label)).setTypeface(typeface);
        ((TextView) rootView.findViewById(R.id.remaining_label)).setTypeface(typeface);
        typeface = Typeface.createFromAsset(getActivity().getBaseContext().getAssets(),
                "fonts/VarelaRound-Regular.ttf");
        sharedPreferences = mContext.getSharedPreferences(PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE);

        showBarGraph(getActivity().getBaseContext());

        TextView month = (TextView) rootView.findViewById(R.id.month);
        month.setText(new DateFormatSymbols().getMonths()[Calendar.getInstance().get(
                Calendar.MONTH)] + " " +
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        month.setTypeface(typeface);
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

        entries.add(new Entry(total - spent, 0));
        remainingData.setText(String.valueOf(total - spent));
        remainingData.setTypeface(typeface);
        entries.add(new Entry(spent, 1));
        labels.add("Spent");
        spentData.setText(String.valueOf(spent));
        spentData.setTypeface(typeface);

        if (status < 0) {
            labels.add("Overflow");
            ((TextView) rootView.findViewById(R.id.remaining_label)).setText("Overflow");
        } else {
            labels.add("Remaining");
        }
        PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS, null));
        PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_current_budget);

        if (status < 0) {
            dataSet.setColors(ROOMIES_RAG_REVERSE_COLORS);
        } else {
            dataSet.setColors(ROOMIES_RAG_COLORS);
        }
        dataSet.setDrawValues(false);
        PieData data = new PieData(labels, dataSet);

        mChart.setData(data);
        mChart.animateXY(1000, 1000);
        mChart.setDrawCenterText(true);
        mChart.setCenterText(String.valueOf((int) status) + "%");
        mChart.setCenterTextColor(getResources().getColor(R.color.white));
        mChart.setCenterTextTypeface(typeface);
        mChart.setCenterTextColor(getResources().getColor(R.color.white));
        mChart.setHoleColor(getResources().getColor(R.color.card_dark));
        mChart.setCenterTextSize(25);
        mChart.setRotationEnabled(false);
        mChart.setDescription("");
        mChart.setClickable(true);
        mChart.setDrawSliceText(false);
        mChart.setHoleRadius(85);
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

        /*String roomId = sharedPreferences.getString(PREF_ROOM_ID, null);

        Map<ServiceParam, Object> queryMap = new HashMap<>();
        List<QueryParam> projection = new ArrayList<>();
        List<QueryParam> selection = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();

        projection.add(QueryParam.TOTAL);
        queryMap.put(ServiceParam.PROJECTION, projection);

        selection.add(QueryParam.ROOMID);
        queryMap.put(ServiceParam.SELECTION, selection);

        selectionArgs.add(roomId);
        queryMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        queryMap.put(ServiceParam.QUERYARGS, QueryParam.MONTH_YEAR);

        boolean isGoogleFBLogin = sharedPreferences.getBoolean(IS_GOOGLE_FB_LOGIN, false);*/
        /*if (isGoogleFBLogin) {
            username = getActivity().getSharedPreferences
					(ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
		}*/

        /*RoomiesDao service = new RoomStatsDaoImpl(mContext);
        List<RoomStats> roomStats = (List<RoomStats>) service.getDetails(queryMap);
        return roomStats.get(0).getTotal();*/
    }

    @Override
    public void update(String username) {
        showBarGraph(getActivity().getBaseContext());

    }
}
