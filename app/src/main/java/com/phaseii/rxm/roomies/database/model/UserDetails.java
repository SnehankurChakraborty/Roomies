package com.phaseii.rxm.roomies.database.model;

/**
 * Created by Snehankur on 6/29/2015.
 */
public class UserDetails extends RoomiesModel {

    private int userId;
    private String username;
    private String password;
    private String userAlias;
    private String phone;
    private String location;
    private String aboutMe;
    private String senderId;

    public UserDetails() {
        userId = -1;
        username = null;
        password = null;
        userAlias = null;
        senderId = null;
        phone = null;
        location = null;
        aboutMe = null;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
