package com.phaseii.rxm.roomies.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Snehankur on 3/18/2015.
 */
public abstract class RoomiesFragment extends Fragment {

	public static final int SEGMENT_ONE = Color.parseColor("#3cbf97");
	public static final int SEGMENT_TWO = Color.parseColor("#23dfa5");
	public static final int SEGMENT_THREE = Color.parseColor("#2eb094");
	public static final int SEGMENT_FOUR = Color.parseColor("#15816a");
	public static final int GREEN_STATUS = Color.parseColor("#16B624");
	public static final int RED_STATUS = Color.parseColor("#D5152C");
	public static final int AMBER_STATUS = Color.parseColor("#DF8921");
	public View rootView;

	public abstract View getFragmentView();

	public static final int[] ROOMIES_ALL_COLORS = {
			SEGMENT_ONE, SEGMENT_TWO, SEGMENT_THREE, SEGMENT_FOUR
	};

	public static final int[] ROOMIES_RAG_COLORS = {
			GREEN_STATUS, RED_STATUS, SEGMENT_THREE, AMBER_STATUS
	};

}


