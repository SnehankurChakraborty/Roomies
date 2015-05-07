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
	public static final int GREEN_STATUS = Color.parseColor("#4CAF50");
	public static final int RED_STATUS = Color.parseColor("#F44336");
	public static final int AMBER_STATUS = Color.parseColor("#FF9800");
	public View rootView;

	public abstract View getFragmentView();

	public static final int[] ROOMIES_ALL_COLORS = {
			SEGMENT_ONE, SEGMENT_TWO, SEGMENT_THREE, SEGMENT_FOUR
	};

	public static final int[] ROOMIES_RAG_COLORS = {
			GREEN_STATUS, AMBER_STATUS
	};
	public static final int[] ROOMIES_RAG_REVERSE_COLORS = {
			RED_STATUS, AMBER_STATUS
	};


	public interface UpdatableFragment {
		public void update();
	}

}


