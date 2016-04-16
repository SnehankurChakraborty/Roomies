package com.phaseii.rxm.roomies.service;

import android.content.Context;

import com.phaseii.rxm.roomies.database.dao.RoomiesDao;
import com.phaseii.rxm.roomies.database.dao.UserDetailsDaoImpl;
import com.phaseii.rxm.roomies.database.model.UserDetails;
import com.phaseii.rxm.roomies.utils.QueryParam;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;
import com.phaseii.rxm.roomies.utils.ServiceParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class UserDetailsManager {
    private Context mContext;
    private RoomiesDao roomiesDao;

    /**
     * @param mContext
     */
    public UserDetailsManager(Context mContext) {
        this.mContext = mContext;
        this.roomiesDao = new UserDetailsDaoImpl(mContext);
    }

    /**
     * get user details based on user name
     */
    public List<UserDetails> getUserDetails(String username) {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> params = new ArrayList<QueryParam>();
        params.add(QueryParam.USERNAME);
        paramMap.put(ServiceParam.SELECTION, params);
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(username);
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        List<UserDetails> userDetailsList = (List<UserDetails>) roomiesDao.getDetails(paramMap);
        return userDetailsList;
    }

    /**
     * * get user details based on user id
     */
    public List<UserDetails> getUserDetails(int userId) {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> params = new ArrayList<QueryParam>();
        params.add(QueryParam.ID);
        paramMap.put(ServiceParam.SELECTION, params);
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(String.valueOf(userId));
        paramMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        List<UserDetails> userDetailsList = (List<UserDetails>) roomiesDao.getDetails(paramMap);
        return userDetailsList;
    }

    public int storeUserDetails(UserDetails userDetails) {
        Map<ServiceParam, UserDetails> userMap = new HashMap<>();
        userMap.put(ServiceParam.MODEL, userDetails);
        roomiesDao = new UserDetailsDaoImpl(mContext);
        int userId = roomiesDao.insertDetails(userMap);
        return userId;
    }

    public int updateUserDetails(UserDetails userDetails, String username) {
        Map<ServiceParam, Object> userMap = new HashMap<>();
        List<QueryParam> selections = new ArrayList<>();
        List<String> selectionArgs = new ArrayList<>();
        selections.add(QueryParam.USERNAME);
        selectionArgs.add(username);
        userMap.put(ServiceParam.MODEL, userDetails);
        userMap.put(ServiceParam.SELECTION, selections);
        userMap.put(ServiceParam.SELECTION_ARGS, selectionArgs);
        roomiesDao = new UserDetailsDaoImpl(mContext);
        int updated = roomiesDao.updateDetails(userMap);
        if (updated > 0) {
            RoomiesHelper.cacheDBtoPreferences(mContext, null, userDetails, null, null, false);
        }
        return updated;
    }
}
