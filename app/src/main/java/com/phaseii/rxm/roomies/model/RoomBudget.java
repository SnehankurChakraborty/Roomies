package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class RoomBudget {
	private  String month;
	private  float rent;
	private  float rent_margin;
	private  float maid;
	private  float maid_margin;
	private  float electricity;
	private  float electricity_margin;
	private  float miscellaneous;
	private  float miscellaneous_margin;
	private  float total;

	public  String getMonth() {
		return month;
	}

	public  void setMonth(String month) {
		this.month = month;
	}

	public  float getRent() {
		return rent;
	}

	public  void setRent(float rent) {
		this.rent = rent;
	}

	public  float getRent_margin() {
		return rent_margin;
	}

	public  void setRent_margin(float rent_margin) {
		this.rent_margin = rent_margin;
	}

	public  float getMaid() {
		return maid;
	}

	public  void setMaid(float maid) {
		this.maid = maid;
	}

	public  float getMaid_margin() {
		return maid_margin;
	}

	public  void setMaid_margin(float maid_margin) {
		this.maid_margin = maid_margin;
	}

	public  float getElectricity() {
		return electricity;
	}

	public  void setElectricity(float electricity) {
		this.electricity = electricity;
	}

	public  float getElectricity_margin() {
		return electricity_margin;
	}

	public  void setElectricity_margin(float electricity_margin) {
		this.electricity_margin = electricity_margin;
	}

	public  float getMiscellaneous() {
		return miscellaneous;
	}

	public  void setMiscellaneous(float miscellaneous) {
		this.miscellaneous = miscellaneous;
	}

	public  float getMiscellaneous_margin() {
		return miscellaneous_margin;
	}

	public  void setMiscellaneous_margin(float miscellaneous_margin) {
		this.miscellaneous_margin = miscellaneous_margin;
	}

	public  float getTotal() {
		return total;
	}

	public  void setTotal(float total) {
		this.total = total;
	}
}