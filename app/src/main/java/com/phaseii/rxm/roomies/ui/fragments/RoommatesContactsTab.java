package com.phaseii.rxm.roomies.ui.fragments;

import android.view.View;

import com.phaseii.rxm.roomies.database.model.RoomExpenses;

import java.util.List;

/**
 * Created by Snehankur on 10/27/2015.
 */
public class RoommatesContactsTab extends RoomiesFragment {
    private static List<RoomExpenses> roomExpensesList;

    public static RoomiesFragment getInstance(List<RoomExpenses> roomExpenses) {
        roomExpenses = roomExpensesList;
        return new RoommatesContactsTab();
    }

    @Override
    public View getFragmentView() {
        return null;
    }
}
