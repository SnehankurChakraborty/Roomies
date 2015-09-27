package com.phaseii.rxm.roomies.model;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class MemberDetail {
    private String username;
    private String userStatus;
    private RoomExpenses recentExpense;
    private float totalExpense;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public RoomExpenses getRecentExpense() {
        return recentExpense;
    }

    public void setRecentExpense(RoomExpenses recentExpense) {
        this.recentExpense = recentExpense;
    }

    public float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }
}
