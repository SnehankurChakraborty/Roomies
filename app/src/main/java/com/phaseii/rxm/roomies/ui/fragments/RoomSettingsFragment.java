package com.phaseii.rxm.roomies.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.service.RoomStatManager;
import com.phaseii.rxm.roomies.ui.customviews.RoomiesTextView;
import com.phaseii.rxm.roomies.utils.Constants;

import java.util.List;

/**
 * Created by Snehankur on 3/19/2016.
 */
public class RoomSettingsFragment extends RoomiesFragment {

    public static RoomSettingsFragment newInstance() {
        return new RoomSettingsFragment();
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_room_settings, container, false);
        Context mContext = getActivity().getBaseContext();
        RoomStatManager manager = new RoomStatManager(mContext);
        List<RoomStats> roomStatsList = manager.getAllRoomStats(
                mContext.getSharedPreferences(Constants.PREF_ROOMIES_KEY, Context.MODE_PRIVATE)
                        .getString(Constants.PREF_USER_ID, "-1"));
        ((RoomiesTextView) rootView.findViewById(R.id.stat_rent_budget))
                .setText(String.valueOf(roomStatsList.get(0).getRentMargin()));
        return rootView;
    }

    @Override public View getFragmentView() {
        return rootView;
    }
}
