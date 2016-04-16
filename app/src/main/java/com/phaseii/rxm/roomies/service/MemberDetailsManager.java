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

package com.phaseii.rxm.roomies.service;

import android.content.Context;

import com.phaseii.rxm.roomies.database.dao.RoomUserMapDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomiesDao;
import com.phaseii.rxm.roomies.database.model.MemberDetail;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomUserMap;
import com.phaseii.rxm.roomies.database.model.UserDetails;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class MemberDetailsManager {

    private Context mContext;
    private RoomiesDao roomiesDao;

    public MemberDetailsManager(Context mContext) {
        this.mContext = mContext;
    }

    public List<MemberDetail> getMemberDetails() {
        List<MemberDetail> memberList = new ArrayList<>();
        String roomId = mContext.getSharedPreferences(Constants.PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE).getString(Constants.PREF_ROOM_ID, "0");

        Map<ServiceParam, Object> paramMap = new HashMap<>();

        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        projectionParams.add(QueryParam.USER_ID);
        projectionParams.add(QueryParam.ROOM_ID);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.ROOM_ID);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(roomId);
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);

        roomiesDao = new RoomUserMapDaoImpl(mContext);
        List<RoomUserMap> users = (List<RoomUserMap>) roomiesDao.getDetails(paramMap);
        List<RoomExpenses> roomExpensesList = new RoomExpensesManager(mContext)
                .getRoomExpenses(roomId);
        for (RoomUserMap user : users) {
            MemberDetail member = new MemberDetail();
            UserDetails userDetails = new UserDetailsManager(mContext).getUserDetails(user
                    .getUserId()).get(0);
            member.setUsername(userDetails.getUsername());
            member.setUserStatus("Life is fun");

            float amount = 0f;
            for (RoomExpenses expenses : roomExpensesList) {
                if (expenses.getUserId() == user.getUserId()) {
                    amount = amount + expenses.getAmount();
                }
            }
            member.setTotalExpense(amount);
            if (roomExpensesList.size() > 0) {
                member.setRecentExpense(roomExpensesList.get(roomExpensesList.size() - 1));
            }
            memberList.add(member);
        }
        return memberList;
    }
}
