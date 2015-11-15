package com.phaseii.rxm.roomies.tabs;

import android.view.View;

import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.model.RoomExpenses;

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
