package com.phaseii.rxm.roomies.dao;

import com.phaseii.rxm.roomies.model.RoomiesModel;
import com.phaseii.rxm.roomies.util.ServiceParam;

import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 6/29/2015.
 */
public interface RoomiesDao {

    /**
     *
     * @param queryMap
     * @return
     */
    List<? extends RoomiesModel> getDetails(Map<ServiceParam, ?> queryMap);

    /**
     *
     * @param detailsMap
     * @return
     */
    int insertDetails(Map<ServiceParam, ? extends RoomiesModel> detailsMap);

    /**
     *
     * @param detailsMap
     * @param <T>
     * @return
     */
    <T> int deleteDetails(Map<ServiceParam, List<T>> detailsMap);

    /**
     *
     * @param detailsMap
     * @return
     */
    int updateDetails(Map<ServiceParam, ?> detailsMap);
}
