package com.phaseii.rxm.roomies.database.model;

/**
 * Created by Snehankur on 8/22/2015.
 */
public class RoomUserStatData extends RoomiesModel {

    /**
     * User Details
     */
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
    private long rentSpent;
    private long maidSpent;
    private long electricitySpent;
    private long miscellaneousSpent;
    private long total;

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

    public long getRentSpent() {
        return rentSpent;
    }

    public void setRentSpent(long rentSpent) {
        this.rentSpent = rentSpent;
    }

    public long getMaidSpent() {
        return maidSpent;
    }

    public void setMaidSpent(long maidSpent) {
        this.maidSpent = maidSpent;
    }

    public long getElectricitySpent() {
        return electricitySpent;
    }

    public void setElectricitySpent(long electricitySpent) {
        this.electricitySpent = electricitySpent;
    }

    public long getMiscellaneousSpent() {
        return miscellaneousSpent;
    }

    public void setMiscellaneousSpent(long miscellaneousSpent) {
        this.miscellaneousSpent = miscellaneousSpent;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
