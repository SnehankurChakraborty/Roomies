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
import android.content.SharedPreferences;

import com.phaseii.rxm.roomies.database.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.database.dao.RoomiesDao;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomiesModel;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.utils.Category;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.ServiceParam;
import com.phaseii.rxm.roomies.utils.SubCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ID;

/**
 * Created by Snehankur on 8/16/2015.
 */
public class RoomExpensesManager {

    private Context mContext;
    private SharedPreferences mSharedPref;
    private RoomiesDao roomiesDao;
    private Map<ServiceParam, RoomExpenses> roomExpensesMap;
    private RoomiesLogger log;

    /**
     *
     * @param mContext
     */
    public RoomExpensesManager(Context mContext) {
        this.mContext = mContext;
        this.mSharedPref = mContext.getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
        this.roomiesDao = new RoomExpensesDaoImpl(mContext);
        this.log = RoomiesLogger.getInstance();
    }

    /**
     *
     * @param category
     * @param subCategory
     * @param description
     * @param quantity
     * @param amount
     * @return
     */
    public boolean addRoomExpense(Category category, SubCategory subCategory, String
            description, String quantity, int amount, Date date) {

        String methodName = "addRoomExpense";
        log.createEntryLoggingMessage(RoomExpensesManager.class.getName(), methodName,
                category.toString() + " " + subCategory + " " + description + " " + quantity + " " +
                        amount);
        List<?> m = new ArrayList<RoomiesModel>();

        boolean isExpenseAdded = false;
        roomExpensesMap = new HashMap<>();

        RoomExpenses roomExpenses = new RoomExpenses();
        roomExpenses.setExpenseCategory(category.toString());
        if (null != subCategory) {
            roomExpenses.setExpenseSubcategory(subCategory.toString());
        }
        if (null != description) {
            roomExpenses.setDescription(description);
        }
        if (null != quantity) {
            roomExpenses.setQuantity(quantity);
        }
        roomExpenses.setAmount(amount);
        roomExpenses.setExpenseDate(date);
        roomExpenses.setMonthYear(DateUtils.getCurrentMonthYear());
        roomExpenses.setUserId(Integer.parseInt(mSharedPref.getString(PREF_USER_ID, "0")));
        roomExpenses.setRoomId(Integer.parseInt(mSharedPref.getString(PREF_ROOM_ID, "0")));

        roomExpensesMap.put(ServiceParam.MODEL, roomExpenses);
        int rowsInserted = roomiesDao.insertDetails(roomExpensesMap);
        if (rowsInserted > 0) {
            isExpenseAdded = true;
        }

        log.createExitLoggingMessage(RoomExpensesManager.class.getName(), methodName, "is Expense" +
                " Added :" + isExpenseAdded);

        return isExpenseAdded;
    }


    /**
     *
     * @param roomId
     * @return
     */
    public List<RoomExpenses> getRoomExpenses(String roomId) {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        List<QueryParam> projectionParams = new ArrayList<QueryParam>();
        List<String> selectionArgs = new ArrayList<String>();
        Map<QueryParam, String> queryParams = new HashMap<>();

        projectionParams.add(QueryParam.USER_ID);
        projectionParams.add(QueryParam.ROOM_ID);
        projectionParams.add(QueryParam.EXPENSE_CATEGORY);
        projectionParams.add(QueryParam.EXPENSE_SUBCATEGORY);
        projectionParams.add(QueryParam.EXPENSE_DATE);
        projectionParams.add(QueryParam.EXPENSE_QUANTITY);
        projectionParams.add(QueryParam.EXPENSE_AMOUNT);
        projectionParams.add(QueryParam.EXPENSE_DESCRIPTION);

        selectionParams.add(QueryParam.MONTH_YEAR);
        selectionArgs.add(DateUtils.getCurrentMonthYear());

        queryParams.put(QueryParam.ROOM_ID, roomId);

        paramMap.put(ServiceParam.PROJECTION, projectionParams);
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        paramMap.put(ServiceParam.SELECTION, selectionParams);
        paramMap.put(ServiceParam.QUERY_ARGS, queryParams);
        List<RoomExpenses> roomExpensesList = (List<RoomExpenses>) roomiesDao.getDetails(paramMap);
        return roomExpensesList;
    }

}
