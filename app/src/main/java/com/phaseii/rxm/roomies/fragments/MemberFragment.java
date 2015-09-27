package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.view.MembersAdapter;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class MemberFragment extends RoomiesFragment {

    public static MemberFragment getInstance(){
        return new MemberFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_members, container, false);
        Context mContext = getActivity().getBaseContext();
        SharedPreferences msharedPref = mContext.getSharedPreferences(PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(
                R.id.room_members_view);
        RecyclerView.Adapter adapter = new MembersAdapter(getActivity().getBaseContext());
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
