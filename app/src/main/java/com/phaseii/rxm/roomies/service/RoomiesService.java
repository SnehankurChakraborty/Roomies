package com.phaseii.rxm.roomies.service;

import android.database.Cursor;

/**
 * Created by Snehankur on 4/7/2015.
 */
public interface RoomiesService {
	void insertRoomDetails(String rent, String maid, String electricity, String username,
	                       String roomAlias);

	void updateRoomExpenses(String rent, String maid, String electricity, String username);

	Cursor getRoomDetails();

	Cursor getRoomDetailsWithMargin(String username);

	float getTotalSpent();
}
