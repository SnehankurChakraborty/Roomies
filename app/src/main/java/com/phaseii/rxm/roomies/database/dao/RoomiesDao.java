package com.phaseii.rxm.roomies.database.dao;

import com.phaseii.rxm.roomies.database.model.RoomiesModel;
import com.phaseii.rxm.roomies.utils.ServiceParam;

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
    int deleteDetails(Map<ServiceParam, ?> detailsMap);

    /**
     *
     * @param detailsMap
     * @return
     */
    int updateDetails(Map<ServiceParam, ?> detailsMap);

    /**
     *
     * @param serviceParamMap
     */
    void prepareStatement(Map<ServiceParam, ?> serviceParamMap);
}
