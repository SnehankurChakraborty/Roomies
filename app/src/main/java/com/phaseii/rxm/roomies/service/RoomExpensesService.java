package com.phaseii.rxm.roomies.service;

import android.content.Context;

import com.phaseii.rxm.roomies.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomExpenses;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 8/14/2015.
 */
public class RoomExpensesService {

    private Context mContext;
    private Map<ServiceParam, RoomExpenses> insertExpensesMap;
    private RoomiesDao dao;

    public RoomExpensesService(Context mContext) {
        this.mContext = mContext;
    }

    public void addNewExpense(RoomExpenses expenses) {

        insertExpensesMap = new HashMap<>();
        insertExpensesMap.put(ServiceParam.MODEL, expenses);

        dao = new RoomExpensesDaoImpl(mContext);
        int count = dao.insertDetails(insertExpensesMap);
    }
}
