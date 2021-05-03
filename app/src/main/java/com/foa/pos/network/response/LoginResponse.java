package com.foa.pos.network.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName( "statusCode" )
    private int status;
    @SerializedName( "message" )
    private String message;
    @SerializedName( "data" )
    private StaffLogin data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StaffLogin getData() {
        return data;
    }

    public void setData(StaffLogin data) {
        this.data = data;
    }
}





