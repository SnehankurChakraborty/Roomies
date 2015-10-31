package com.phaseii.rxm.roomies.manager;

import android.content.Context;

import com.phaseii.rxm.roomies.dao.RoomUserMapDao;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.model.MemberDetail;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomUserMap;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.ServiceParam;

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
        String roomId = mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE).getString(RoomiesConstants.PREF_ROOM_ID, "0");

        Map<ServiceParam, Object> paramMap = new HashMap<>();

        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        projectionParams.add(QueryParam.USER_ID);
        projectionParams.add(QueryParam.ROOMID);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.ROOMID);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(roomId);
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        roomiesDao = new RoomUserMapDao(mContext);
        List<RoomUserMap> users = (List<RoomUserMap>) roomiesDao.getDetails(paramMap);
        List<RoomExpenses> roomExpensesList = new RoomExpensesManager(mContext).getRoomExpenses();
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
