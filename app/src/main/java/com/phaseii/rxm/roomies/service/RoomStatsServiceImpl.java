package com.phaseii.rxm.roomies.service;

import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.UpdateParam;
import com.phaseii.rxm.roomies.model.RoomStats;

import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 7/1/2015.
 */
public class RoomStatsServiceImpl implements RoomiesService {

	@Override
	public List<RoomStats> getDetails(Map<QueryParam, ?> paramMap) {
		return null;
	}

	@Override
	public int insertDetails(Map<UpdateParam, ?> detailsMap) {
		return 0;
	}

	@Override
	public int deleteDetails(Map<UpdateParam, ?> detailsMap) {
		return 0;
	}

	@Override
	public int updateDetails(Map<UpdateParam, ?> detailsMap) {
		return 0;
	}
}
