package com.phaseii.rxm.roomies.manager;

import android.content.Context;

import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.dao.UserDetailsDaoImpl;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.ServiceParam;

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
     *
     * @param mContext
     */
    public UserDetailsManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * get user details based on user name
     * @param username
     * @return
     */
    public List<UserDetails> getUserDetails(String username) {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> params = new ArrayList<QueryParam>();
        params.add(QueryParam.USERNAME);
        paramMap.put(ServiceParam.SELECTION, params);
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(username);
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);
        roomiesDao = new UserDetailsDaoImpl(mContext);
        List<UserDetails> userDetailsList = (List<UserDetails>) roomiesDao.getDetails(paramMap);
        return userDetailsList;
    }

    /**
     * * get user details based on user id
     * @param userId
     * @return
     */
    public List<UserDetails> getUserDetails(int userId) {
        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> params = new ArrayList<QueryParam>();
        params.add(QueryParam.ID);
        paramMap.put(ServiceParam.SELECTION, params);
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(String.valueOf(userId));
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);
        roomiesDao = new UserDetailsDaoImpl(mContext);
        List<UserDetails> userDetailsList = (List<UserDetails>) roomiesDao.getDetails(paramMap);
        return userDetailsList;
    }
}
