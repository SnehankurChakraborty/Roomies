package com.phaseii.rxm.roomies.tabs;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.view.MembersAdapter;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class RoommateExpensesTab extends RoomiesFragment {

    private Context mContext;
    private Typeface typeface;

    public static RoommateExpensesTab getInstance() {
        return new RoommateExpensesTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_roommate_expenses, container, false);
        mContext = getActivity().getBaseContext();

        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular" +
                ".ttf");
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(
                R.id.room_members_view);
        RecyclerView.Adapter adapter = new MembersAdapter(getActivity().getBaseContext(), getChildFragmentManager());
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return rootView;

    }

    @Override
    public View getFragmentView() {
        return rootView;
    }
}
