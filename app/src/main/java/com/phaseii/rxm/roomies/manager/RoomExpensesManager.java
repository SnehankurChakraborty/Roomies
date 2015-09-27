package com.phaseii.rxm.roomies.manager;


import android.content.Context;
import android.content.SharedPreferences;

import com.phaseii.rxm.roomies.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.util.Category;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.RoomiesHelper;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.util.SubCategory;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.model.RoomExpenses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_USER_ID;

/**
 * Created by Snehankur on 8/16/2015.
 */
public class RoomExpensesManager {

    private Context mContext;
    private SharedPreferences mSharedPref;
    private RoomiesDao roomiesDao;
    private Map<ServiceParam, RoomExpenses> roomExpensesMap;
    private RoomiesLogger log;

    public RoomExpensesManager(Context mContext) {
        this.mContext = mContext;
        this.mSharedPref = mContext.getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
        this.roomiesDao = new RoomExpensesDaoImpl(mContext);
        this.log = RoomiesLogger.getInstance();
    }

    public boolean addRoomExpense(Category category, SubCategory subCategory, String
            description, String quantity, float amount) {

        String methodName = "addRoomExpense";
        log.createEntryLoggingMessage(RoomExpensesManager.class.getName(), methodName,
                category.toString() + " " + subCategory + " " + description + " " + quantity + " " +
                        amount);

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
        roomExpenses.setExpenseDate(new Date());
        roomExpenses.setMonthYear(RoomiesHelper.getCurrentMonthYear());
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

    public List<RoomExpenses> getRoomExpenses() {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> projectionParams = new ArrayList<QueryParam>();

        projectionParams.add(QueryParam.USER_ID);
        projectionParams.add(QueryParam.EXPENSE_CATEGORY);
        projectionParams.add(QueryParam.EXPENSE_SUBCATEGORY);
        projectionParams.add(QueryParam.EXPENSE_DATE);
        projectionParams.add(QueryParam.EXPENSE_QUANTITY);
        projectionParams.add(QueryParam.EXPENSE_AMOUNT);
        projectionParams.add(QueryParam.EXPENSE_DESCRIPTION);
        paramMap.put(ServiceParam.PROJECTION, projectionParams);

        List<QueryParam> selectionParams = new ArrayList<QueryParam>();
        selectionParams.add(QueryParam.MONTH_YEAR);
        paramMap.put(ServiceParam.SELECTION, selectionParams);

        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(RoomiesHelper.getCurrentMonthYear());
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        paramMap.put(ServiceParam.QUERYARGS, QueryParam.ROOMID);
        List<RoomExpenses> roomExpensesList = (List<RoomExpenses>) roomiesDao.getDetails(paramMap);
        return roomExpensesList;
    }

}
