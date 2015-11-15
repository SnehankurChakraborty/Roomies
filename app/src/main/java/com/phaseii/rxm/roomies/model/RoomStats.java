package com.phaseii.rxm.roomies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snehankur on 7/1/2015.
 */
public class RoomStats extends RoomiesModel implements Parcelable{
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

    public RoomStats(){
        statsId = 0;
        roomId = 0;
        monthYear = null;
        rentMargin = 0l;
        maidMargin = 0l;
        electricityMargin = 0l;
        miscellaneousMargin = 0l;
        rentSpent = 0f;
        maidSpent = 0f;
        electricitySpent = 0f;
        miscellaneousSpent = 0f;
    }

    protected RoomStats(Parcel in) {
        statsId = in.readInt();
        roomId = in.readInt();
        monthYear = in.readString();
        rentMargin = in.readLong();
        maidMargin = in.readLong();
        electricityMargin = in.readLong();
        miscellaneousMargin = in.readLong();
        rentSpent = in.readFloat();
        maidSpent = in.readFloat();
        electricitySpent = in.readFloat();
        miscellaneousSpent = in.readFloat();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statsId);
        dest.writeInt(roomId);
        dest.writeString(monthYear);
        dest.writeLong(rentMargin);
        dest.writeLong(maidMargin);
        dest.writeLong(electricityMargin);
        dest.writeLong(miscellaneousMargin);
        dest.writeFloat(rentSpent);
        dest.writeFloat(maidSpent);
        dest.writeFloat(electricitySpent);
        dest.writeFloat(miscellaneousSpent);
    }
}
