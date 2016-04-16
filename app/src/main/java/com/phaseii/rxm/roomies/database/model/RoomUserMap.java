package com.phaseii.rxm.roomies.database.model;

/**
 * Created by Snehankur on 8/23/2015.
 */
public class RoomUserMap extends RoomiesModel {
    private int roomUserStatId;
    private int roomId;
    private int userId;

    public int getRoomUserStatId() {
        return roomUserStatId;
    }

    public void setRoomUserStatId(int roomUserStatId) {
        this.roomUserStatId = roomUserStatId;
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
}
