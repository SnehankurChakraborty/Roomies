package com.phaseii.rxm.roomies.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phaseii.rxm.roomies.R;

import java.util.ArrayList;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_EXPENSE_FILE_KEY;


public class CurrentRoomStatusFragment extends Fragment {


	private View mView;

	private OnFragmentInteractionListener mListener;

	public static CurrentRoomStatusFragment getInstance() {
		CurrentRoomStatusFragment currentRoomStatusFragment = new CurrentRoomStatusFragment();
		return currentRoomStatusFragment;
	}

	public CurrentRoomStatusFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mView = inflater.inflate(R.layout.fragment_current_room_status, container, false);
		showBarGraph(getActivity().getBaseContext());
		return mView;
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
		SharedPreferences sharedPreferences = context.getSharedPreferences(ROOM_EXPENSE_FILE_KEY,
				context.MODE_PRIVATE);
		float rent = Float.valueOf(sharedPreferences.getString(RENT, "0"));
		float maid = Float.valueOf(sharedPreferences.getString(MAID, "0"));
		float electricity = Float.valueOf(sharedPreferences.getString(ELECTRICITY, "0"));
		float misc = Float.valueOf(sharedPreferences.getString(MISC, "0"));
		ArrayList<Entry> entries = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<String>();

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
		PieDataSet dataSet = new PieDataSet(entries, sharedPreferences.getString(ROOM_ALIAS,
				null));
		PieChart mChart = (PieChart) mView.findViewById(R.id.pie);
		dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
		PieData data = new PieData(labels, dataSet);
		mChart.setUsePercentValues(true);
		mChart.setData(data);
		mChart.animateXY(1000, 1000);
		mChart.setDrawCenterText(true);
		mChart.setCenterText("Room Status");
		mChart.setDescription("");
		mChart.setClickable(true);
		return mChart;
	}


}
