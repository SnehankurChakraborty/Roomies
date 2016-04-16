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
