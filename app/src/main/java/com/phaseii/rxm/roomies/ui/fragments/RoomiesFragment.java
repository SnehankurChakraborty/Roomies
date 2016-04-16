package com.phaseii.rxm.roomies.ui.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.phaseii.rxm.roomies.database.model.RoomExpenses;

/**
 * Created by Snehankur on 3/18/2015.
 */
public abstract class RoomiesFragment extends Fragment {

    public static final int SEGMENT_ONE = Color.parseColor("#3cbf97");
    public static final int SEGMENT_TWO = Color.parseColor("#23dfa5");
    public static final int SEGMENT_THREE = Color.parseColor("#2eb094");
    public static final int SEGMENT_FOUR = Color.parseColor("#15816a");
    public static final int GREEN_STATUS = Color.parseColor("#F48FB1");
    public static final int RED_STATUS = Color.parseColor("#F44336");
    public static final int AMBER_STATUS = Color.parseColor("#FF9800");
    public static final int GRAY_STATUS = Color.parseColor("#BDBDBD");
    public static final int[] ROOMIES_ALL_COLORS = {
            SEGMENT_ONE, SEGMENT_TWO, SEGMENT_THREE, SEGMENT_FOUR
    };
    public static final int[] ROOMIES_RAG_COLORS = {
            GRAY_STATUS, GREEN_STATUS
    };
    public static final int[] ROOMIES_RAG_REVERSE_COLORS = {
            GREEN_STATUS, RED_STATUS
    };
    public View rootView;
    private Bundle parceableBundle;

    public abstract View getFragmentView();

    public void setTypeface(Typeface typeface) {

    }

    public Bundle getParceableBundle() {
        return parceableBundle;
    }

    public void setParceableBundle(Bundle parceableBundle) {
        this.parceableBundle = parceableBundle;
    }

    public interface UpdatableFragment {
        void update(RoomExpenses roomExpense);
    }
}