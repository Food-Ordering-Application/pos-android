package com.foa.pos.network.entity;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("restaurantId")
    private String restaurantId;

    @SerializedName("temp")
    private String temp;

    public LoginBody(String username, String password, String restaurantId) {
        this.username = username;
        this.password = password;
        this.restaurantId = restaurantId;
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

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
