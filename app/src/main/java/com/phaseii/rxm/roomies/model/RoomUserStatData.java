package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 8/22/2015.
 */
public class RoomUserStatData extends RoomiesModel {

    /**
     * User Details
     */
    private int userId;
    private String username;
    private String userAlias;
    private String senderId;

    /**
     * Room Details
     */
    private int roomId;
    private String roomAlias;
    private int noOfMembers;

    /**
     * Room Stats
     */
    private String monthYear;
    private long rentMargin;
    private long maidMargin;
    private long electricityMargin;
    private long miscellaneousMargin;
    private long total;

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

    public String getRoomAlias() {
        return roomAlias;
    }

    public void setRoomAlias(String roomAlias) {
        this.roomAlias = roomAlias;
    }

    public int getNoOfMembers() {
        return noOfMembers;
    }

    public void setNoOfMembers(int noOfMembers) {
        this.noOfMembers = noOfMembers;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public long getRentMargin() {
        return rentMargin;
    }

    public void setRentMargin(long rentMargin) {
        this.rentMargin = rentMargin;
    }

    public long getMaidMargin() {
        return maidMargin;
    }

    public void setMaidMargin(long maidMargin) {
        this.maidMargin = maidMargin;
    }

    public long getElectricityMargin() {
        return electricityMargin;
    }

    public void setElectricityMargin(long electricityMargin) {
        this.electricityMargin = electricityMargin;
    }

    public long getMiscellaneousMargin() {
        return miscellaneousMargin;
    }

    public void setMiscellaneousMargin(long miscellaneousMargin) {
        this.miscellaneousMargin = miscellaneousMargin;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
