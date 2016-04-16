package com.phaseii.rxm.roomies.database.model;

import java.util.List;

/**
 * Created by Snehankur on 11/8/2015.
 */
public class Room {
    private RoomDetails roomDetails;
    private RoomStats roomStats;
    private List<UserDetails> users;
    private List<RoomExpenses> roomExpenses;

    public RoomDetails getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(RoomDetails roomDetails) {
        this.roomDetails = roomDetails;
    }

    public RoomStats getRoomStats() {
        return roomStats;
    }

    public void setRoomStats(RoomStats roomStats) {
        this.roomStats = roomStats;
    }

    public List<UserDetails> getUsers() {
        return users;
    }

    public void setUsers(List<UserDetails> users) {
        this.users = users;
    }

    public List<RoomExpenses> getRoomExpenses() {
        return roomExpenses;
    }

    public void setRoomExpenses(List<RoomExpenses> roomExpenses) {
        this.roomExpenses = roomExpenses;
    }
}
