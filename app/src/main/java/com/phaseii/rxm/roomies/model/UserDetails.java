package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class UserDetails extends RoomiesModel {

	private int userId;
	private String username;
	private String password;
	private String userAlias;
	private String senderId;
	private int roomId;
	private boolean isSetupCompleted;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public boolean isSetupCompleted() {
		return isSetupCompleted;
	}

	public void setIsSetupCompleted(boolean isSetupCompleted) {
		this.isSetupCompleted = isSetupCompleted;
	}
}
