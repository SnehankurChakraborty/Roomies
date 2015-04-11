package com.phaseii.rxm.roomies.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.service.RoomiesService;
import com.phaseii.rxm.roomies.service.RoomiesServiceImpl;

import java.util.ArrayList;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.SPENT;


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
/*
		if (rent > 0) {
			entries.add(new Entry(rent, 0));
			labels.add(RENT);
		}
		if (maid > 0) {
			entries.add(new Entry(maid, 1));
			labels.add(MAID);
		}
		if (electricity > 0) {
			entries.add(new Entry(electricity, 2));
			labels.add(ELECTRICITY);
		}
		if (misc > 0) {
			entries.add(new Entry(misc, 3));
			labels.add(MISC);
		}
		if (entries.size() == 0) {
			entries.add(new Entry(100f, 0));
			labels.add("EMPTY");
		}
*/
		PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS,
				null));
		PieChart mChart = (PieChart) rootView.findViewById(R.id.pie_current_budget);
		dataSet.setColors(ROOMIES_RAG_COLORS);
		PieData data = new PieData(labels, dataSet);
		mChart.setData(data);
		mChart.animateXY(1000, 1000);
		mChart.setDrawCenterText(true);
		mChart.setCenterText(getPercentageLeft(total, spent));
		mChart.setDescription("");
		mChart.setClickable(true);
		return mChart;
	}

	private String getPercentageLeft(float total, float spent) {
		float percent = 100f;
		if (spent > 0) {
			percent = 100 - ((spent / total) * 100);
		}
		return percent + "% Availaible";
	}

	private float getSpentDetails() {
		RoomiesService roomiesService = new RoomiesServiceImpl(getActivity().getBaseContext());
		return roomiesService.getTotalSpent();
	}
}
