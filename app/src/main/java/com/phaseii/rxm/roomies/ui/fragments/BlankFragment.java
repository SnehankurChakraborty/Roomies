package com.phaseii.rxm.roomies.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;

/**
 * A simple {@link Fragment} subclass. This fragment is shown when no rooms are added by the user.
 * Use the {@link BlankFragment#newInstance} factory method to create an instance of this fragment.
 */
public class BlankFragment extends RoomiesFragment {

    public static int HOME_FRAGMENT = 1;
    public static int ROOMMATE_FRAGMENT = 2;
    public static String PARENT_FRAGMENT = "parent_fragment";

    /**
     * Empty constructor for BlankFragment.
     */
    public BlankFragment() {

    }

    /**
     * @return new object of BlankFragment
     */
    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    /**
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        return rootView;
    }

    /**
     * @return
     */
    @Override
    public View getFragmentView() {
        return rootView;
    }
}
