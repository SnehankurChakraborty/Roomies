package com.phaseii.rxm.roomies.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.RoomiesContract;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.service.RoomService;
import com.phaseii.rxm.roomies.service.RoomServiceImpl;

import java.util.ArrayList;

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

	SharedPreferences sharedPreferences;

	public static CurrentExpenseReport getInstance() {
		return new CurrentExpenseReport();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_current_expense_report, container, false);
		showBarGraph(getActivity().getBaseContext());
		return rootView;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	private PieChart showBarGraph(Context context) {
		PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_expense_report);
		RoomService roomService = new RoomServiceImpl(getActivity());
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
		sharedPreferences = context.getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		PieDataSet dataSet = new PieDataSet(entries,
				sharedPreferences.getString(ROOM_ALIAS, ""));
		dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
		PieData data = new PieData(labels, dataSet);
		mChart.setData(data);
		mChart.animateXY(1000, 1000);
		mChart.setDrawCenterText(true);
		mChart.setCenterText(getPercentageLeft(total, spent));
		mChart.setDescription("");
		mChart.setClickable(true);
		mChart.setNoDataText("");
		mChart.getLegend().setEnabled(false);
		return mChart;
	}

	private String getPercentageLeft(float total, float expense) {
		float percent = 0f;
		if (expense > 0) {
			percent = (expense / total) * 100;
		}
		return percent + "% Spends";
	}

	private float getTotal() {
		float rent = Float.valueOf(sharedPreferences.getString(RENT_MARGIN, "0"));
		float maid = Float.valueOf(sharedPreferences.getString(MAID_MARGIN, "0"));
		float electricity = Float.valueOf(sharedPreferences.getString(ELECTRICITY_MARGIN, "0"));
		float misc = Float.valueOf(sharedPreferences.getString(MISC_MARGIN, "0"));
		return rent + maid + electricity + misc;
	}

	@Override
	public void update() {
		showBarGraph(getActivity().getBaseContext());
	}
}
