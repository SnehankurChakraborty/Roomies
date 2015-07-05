package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class RoomDetails extends RoomiesModel {

	private int roomId;
	private String roomAlias;
	private int noOfPersons;


	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getRoomAlias() {
		return roomAlias;
	}

	public void setRoomAlias(String roomAlias) {
		this.roomAlias = roomAlias;
	}

	public int getNoOfPersons() {
		return noOfPersons;
	}

	public void setNoOfPersons(int noOfPersons) {
		this.noOfPersons = noOfPersons;
	}

}
