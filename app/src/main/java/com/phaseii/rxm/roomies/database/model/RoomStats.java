package com.phaseii.rxm.roomies.database.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snehankur on 7/1/2015.
 */
public class RoomStats extends RoomiesModel implements Parcelable {
    public static final Creator<RoomStats> CREATOR = new Creator<RoomStats>() {
        @Override
        public RoomStats createFromParcel(Parcel in) {
            return new RoomStats(in);
        }

        @Override
        public RoomStats[] newArray(int size) {
            return new RoomStats[size];
        }
    };
    private int statsId;
    private int roomId;
    private String monthYear;
    private int rentMargin;
    private int maidMargin;
    private int electricityMargin;
    private int miscellaneousMargin;
    private int rentSpent;
    private int maidSpent;
    private int electricitySpent;
    private int miscellaneousSpent;

    public RoomStats() {
        statsId = 0;
        roomId = 0;
        monthYear = null;
        rentMargin = 0;
        maidMargin = 0;
        electricityMargin = 0;
        miscellaneousMargin = 0;
        rentSpent = 0;
        maidSpent = 0;
        electricitySpent = 0;
        miscellaneousSpent = 0;
    }

    protected RoomStats(Parcel in) {
        statsId = in.readInt();
        roomId = in.readInt();
        monthYear = in.readString();
        rentMargin = in.readInt();
        maidMargin = in.readInt();
        electricityMargin = in.readInt();
        miscellaneousMargin = in.readInt();
        rentSpent = in.readInt();
        maidSpent = in.readInt();
        electricitySpent = in.readInt();
        miscellaneousSpent = in.readInt();
    }

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

    public int getRentMargin() {
        return rentMargin;
    }

    public void setRentMargin(int rentMargin) {
        this.rentMargin = rentMargin;
    }

    public int getMaidMargin() {
        return maidMargin;
    }

    public void setMaidMargin(int maidMargin) {
        this.maidMargin = maidMargin;
    }

    public int getElectricityMargin() {
        return electricityMargin;
    }

    public void setElectricityMargin(int electricityMargin) {
        this.electricityMargin = electricityMargin;
    }

    public int getMiscellaneousMargin() {
        return miscellaneousMargin;
    }

    public void setMiscellaneousMargin(int miscellaneousMargin) {
        this.miscellaneousMargin = miscellaneousMargin;
    }

    public int getRentSpent() {
        return rentSpent;
    }

    public void setRentSpent(int rentSpent) {
        this.rentSpent = rentSpent;
    }

    public int getMaidSpent() {
        return maidSpent;
    }

    public void setMaidSpent(int maidSpent) {
        this.maidSpent = maidSpent;
    }

    public int getElectricitySpent() {
        return electricitySpent;
    }

    public void setElectricitySpent(int electricitySpent) {
        this.electricitySpent = electricitySpent;
    }

    public int getMiscellaneousSpent() {
        return miscellaneousSpent;
    }

    public void setMiscellaneousSpent(int miscellaneousSpent) {
        this.miscellaneousSpent = miscellaneousSpent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statsId);
        dest.writeInt(roomId);
        dest.writeString(monthYear);
        dest.writeInt(rentMargin);
        dest.writeInt(maidMargin);
        dest.writeInt(electricityMargin);
        dest.writeInt(miscellaneousMargin);
        dest.writeInt(rentSpent);
        dest.writeInt(maidSpent);
        dest.writeInt(electricitySpent);
        dest.writeInt(miscellaneousSpent);
    }
}
