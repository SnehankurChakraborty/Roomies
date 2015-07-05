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
	private long total;

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

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
