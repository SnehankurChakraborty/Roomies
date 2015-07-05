package com.phaseii.rxm.roomies.model;

import java.util.Date;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class RoomExpenses extends RoomiesModel {

	private int expenseId;
	private int roomId;
	private int userId;
	private String expenseCategory;
	private String expenseSubcategory;
	private String description;
	private int quantity;
	private long amount;
	private Date expense_date;
	private String monthYear;

	public int getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getExpenseCategory() {
		return expenseCategory;
	}

	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	public String getExpenseSubcategory() {
		return expenseSubcategory;
	}

	public void setExpenseSubcategory(String expenseSubcategory) {
		this.expenseSubcategory = expenseSubcategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Date getExpense_date() {
		return expense_date;
	}

	public void setExpense_date(Date expense_date) {
		this.expense_date = expense_date;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
}
