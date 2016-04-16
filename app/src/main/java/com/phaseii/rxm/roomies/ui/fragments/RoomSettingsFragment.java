/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
