package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class RoomBudget {
	private static String month;
	private static float rent;
	private static float rent_margin;
	private static float maid;
	private static float maid_margin;
	private static float electricity;
	private static float electricity_margin;
	private static float miscellaneous;
	private static float miscellaneous_margin;
	private static float total;

	public static String getMonth() {
		return month;
	}

	public static void setMonth(String month) {
		RoomBudget.month = month;
	}

	public static float getRent() {
		return rent;
	}

	public static void setRent(float rent) {
		RoomBudget.rent = rent;
	}

	public static float getRent_margin() {
		return rent_margin;
	}

	public static void setRent_margin(float rent_margin) {
		RoomBudget.rent_margin = rent_margin;
	}

	public static float getMaid() {
		return maid;
	}

	public static void setMaid(float maid) {
		RoomBudget.maid = maid;
	}

	public static float getMaid_margin() {
		return maid_margin;
	}

	public static void setMaid_margin(float maid_margin) {
		RoomBudget.maid_margin = maid_margin;
	}

	public static float getElectricity() {
		return electricity;
	}

	public static void setElectricity(float electricity) {
		RoomBudget.electricity = electricity;
	}

	public static float getElectricity_margin() {
		return electricity_margin;
	}

	public static void setElectricity_margin(float electricity_margin) {
		RoomBudget.electricity_margin = electricity_margin;
	}

	public static float getMiscellaneous() {
		return miscellaneous;
	}

	public static void setMiscellaneous(float miscellaneous) {
		RoomBudget.miscellaneous = miscellaneous;
	}

	public static float getMiscellaneous_margin() {
		return miscellaneous_margin;
	}

	public static void setMiscellaneous_margin(float miscellaneous_margin) {
		RoomBudget.miscellaneous_margin = miscellaneous_margin;
	}

	public static float getTotal() {
		return total;
	}

	public static void setTotal(float total) {
		RoomBudget.total = total;
	}
}