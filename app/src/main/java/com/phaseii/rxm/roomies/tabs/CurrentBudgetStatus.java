package com.phaseii.rxm.roomies.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.service.RoomService;
import com.phaseii.rxm.roomies.service.RoomServiceImpl;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.EMAIL_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;


public class CurrentBudgetStatus extends RoomiesFragment
		implements RoomiesFragment.UpdatableFragment {

	private OnFragmentInteractionListener mListener;

	public static CurrentBudgetStatus getInstance() {
		return new CurrentBudgetStatus();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_current_budget_status, container, false);
		rootView.setTag("BUDGET");
		showBarGraph(getActivity().getBaseContext());
		TextView month = (TextView) rootView.findViewById(R.id.month);
		month.setText(new DateFormatSymbols().getMonths()[Calendar.getInstance().get(
				Calendar.MONTH)] + " " +
				String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		return rootView;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}


	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public View getFragmentView() {
		return rootView;
	}

	@Override
	public void update() {
		showBarGraph(getActivity().getBaseContext());
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	private PieChart showBarGraph(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(ROOM_BUDGET_FILE_KEY,
				context.MODE_PRIVATE);
		float rent = Float.valueOf(sharedPreferences.getString(RENT_MARGIN, "0"));
		float maid = Float.valueOf(sharedPreferences.getString(MAID_MARGIN, "0"));
		float electricity = Float.valueOf(sharedPreferences.getString(ELECTRICITY_MARGIN, "0"));
		float misc = Float.valueOf(sharedPreferences.getString(MISC_MARGIN, "0"));
		ArrayList<Entry> entries = new ArrayList<Entry>();
		ArrayList<String> labels = new ArrayList<String>();
		float total = rent + maid + electricity + misc;
		float spent = getSpentDetails();
		entries.add(new Entry(total - spent, 0));
		labels.add("Remaining");
		entries.add(new Entry(spent, 1));
		labels.add("Spent");
		PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS, null));
		PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_current_budget);
		String status = getPercentageLeft(total, spent);
		if (Float.valueOf(status) < 0) {
			dataSet.setColors(ROOMIES_RAG_REVERSE_COLORS);
		} else {
			dataSet.setColors(ROOMIES_RAG_COLORS);
		}
		dataSet.setValueTextColor(Color.WHITE);
		PieData data = new PieData(labels, dataSet);
		mChart.setData(data);
		mChart.animateXY(1000, 1000);
		mChart.setDrawCenterText(true);

		mChart.setCenterText(status + " % Availaible");
		mChart.setDescription("");
		mChart.setClickable(true);
		mChart.getLegend().setEnabled(false);
		return mChart;
	}

	private String getPercentageLeft(float total, float spent) {
		float percent = 100f;
		if (spent > 0) {
			percent = 100 - ((spent / total) * 100);
		}
		return String.valueOf(percent);
	}

	private float getSpentDetails() {
		RoomService roomService = new RoomServiceImpl(getActivity().getBaseContext());
		String username = getActivity().getSharedPreferences(ROOM_INFO_FILE_KEY,
				Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);
		
		boolean isGoogleFBLogin = getActivity().getSharedPreferences
				(ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
		if (isGoogleFBLogin) {
			username = getActivity().getSharedPreferences
					(ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
		}

		return roomService.getTotalSpent(username);
	}
}
