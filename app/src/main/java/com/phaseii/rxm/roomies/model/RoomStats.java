package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 7/1/2015.
 */
public class RoomStats extends RoomiesModel {
    private int statsId;
    private int roomId;
    private String monthYear;
    private long rentMargin;
    private long maidMargin;
    private long electricityMargin;
    private long miscellaneousMargin;
    private float rentSpent;
    private float maidSpent;
    private float electricitySpent;
    private float miscellaneousSpent;

    public int getStatsId() {
        return statsId;
    }

    public void setStatsId(int statsId) {
        this.statsId = statsId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public float getRentSpent() {
        return rentSpent;
    }

    public void setRentSpent(float rentSpent) {
        this.rentSpent = rentSpent;
    }

    public float getMaidSpent() {
        return maidSpent;
    }

    public void setMaidSpent(float maidSpent) {
        this.maidSpent = maidSpent;
    }

    public float getElectricitySpent() {
        return electricitySpent;
    }

    public void setElectricitySpent(float electricitySpent) {
        this.electricitySpent = electricitySpent;
    }

    public float getMiscellaneousSpent() {
        return miscellaneousSpent;
    }

    public void setMiscellaneousSpent(float miscellaneousSpent) {
        this.miscellaneousSpent = miscellaneousSpent;
    }
}
