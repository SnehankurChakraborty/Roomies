package com.phaseii.rxm.roomies.service;

import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.UpdateParam;
import com.phaseii.rxm.roomies.model.RoomiesModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 6/29/2015.
 */
public interface RoomiesService {

	public List<? extends RoomiesModel> getDetails(Map<QueryParam, ?> paramMap);

	public int insertDetails(Map<UpdateParam, ?> detailsMap);

	public int deleteDetails(Map<UpdateParam, ?> detailsMap);

	public int updateDetails(Map<UpdateParam, ?> detailsMap);
}