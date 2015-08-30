package com.phaseii.rxm.roomies.manager;


import android.content.Context;
import android.content.SharedPreferences;

import com.phaseii.rxm.roomies.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.helper.Category;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.helper.SubCategory;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.model.RoomExpenses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USER_ID;

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
}
