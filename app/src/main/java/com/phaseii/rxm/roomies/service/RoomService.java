package com.phaseii.rxm.roomies.service;

import android.database.Cursor;

import com.phaseii.rxm.roomies.model.RoomBudget;

import java.util.List;

/**
 * Created by Snehankur on 4/7/2015.
 */
public interface RoomService {
	void insertRoomDetails(String rent, String maid, String electricity, String username,
	                       String roomAlias);

	void updateRoomExpenses(String rent, String maid, String electricity, String username);

	Cursor getRoomDetails(String username);

	boolean getRoomDetailsWithMargin(String username);

	List<RoomBudget> getAllMonthDetailsWithMargin(String username);

	float getTotalSpent(String username);

	boolean updateRoomMargins(String username, String column, String newVal);

	boolean isUserSaved(String username);

	void getSpecificMonthDetails(String username, String month);

	List<RoomBudget> getSpecificMonthRoomBudget(String username, List<String> months);
}
