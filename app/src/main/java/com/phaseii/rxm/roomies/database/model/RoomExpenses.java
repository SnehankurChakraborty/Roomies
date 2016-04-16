package com.phaseii.rxm.roomies.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.phaseii.rxm.roomies.utils.DateUtils;

import java.util.Date;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class RoomExpenses extends RoomiesModel implements Parcelable {

    public static final Creator<RoomExpenses> CREATOR = new Creator<RoomExpenses>() {
        @Override
        public RoomExpenses createFromParcel(Parcel in) {
            return new RoomExpenses(in);
        }

        @Override
        public RoomExpenses[] newArray(int size) {
            return new RoomExpenses[size];
        }
    };
    private int expenseId;
    private int roomId;
    private int userId;
    private String expenseCategory;
    private String expenseSubcategory;
    private String description;
    private String quantity;
    private int amount;
    private Date expenseDate;
    private String monthYear;

    public RoomExpenses() {
        expenseId = 0;
        roomId = 0;
        userId = 0;
        expenseCategory = null;
        expenseSubcategory = null;
        description = null;
        quantity = null;
        amount = 0;
        monthYear = null;
        expenseDate = new Date();
    }

    protected RoomExpenses(Parcel in) {
        expenseId = in.readInt();
        roomId = in.readInt();
        userId = in.readInt();
        expenseCategory = in.readString();
        expenseSubcategory = in.readString();
        description = in.readString();
        quantity = in.readString();
        amount = in.readInt();
        monthYear = in.readString();
        expenseDate = DateUtils.stringToDateParser(in.readString());
    }

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(expenseId);
        dest.writeInt(roomId);
        dest.writeInt(userId);
        dest.writeString(expenseCategory);
        dest.writeString(expenseSubcategory);
        dest.writeString(description);
        dest.writeString(quantity);
        dest.writeInt(amount);
        dest.writeString(monthYear);
        dest.writeString(DateUtils.dateToStringFormatter(expenseDate));
    }
}
