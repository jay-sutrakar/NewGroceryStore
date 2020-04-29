package com.example.grocerystore.util;

import android.app.Application;

public class UserApi extends Application {
    private String username;
    private String userContactNumber;
    private String userEmail;
    private UserAddress userAddress;
    private String userId;
    private static UserApi instance;

    public static UserApi getInstance() {
        if(instance == null )
            instance = new UserApi();
        return instance;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
