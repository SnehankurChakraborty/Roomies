package com.phaseii.rxm.roomies.dao;

import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomiesModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 6/29/2015.
 */
public interface RoomiesDao {

    List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap);

    int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap);

    <T> int deleteDetails(Map<ServiceParam, List<T>> detailsMap);

    int updateDetails(Map<ServiceParam, ?> detailsMap);
}
