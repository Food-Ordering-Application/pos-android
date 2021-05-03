package com.foa.pos.network.response;

import com.foa.pos.model.Staff;
import com.google.gson.annotations.SerializedName;

public class StaffLogin{
    @SerializedName( "user" )
    Staff staff;
    @SerializedName("accessToken")
    String accessToken;

    public StaffLogin() {
    }

    public StaffLogin(Staff staff, String accessToken) {
        this.staff = staff;
        this.accessToken = accessToken;
    }

    public void setStaffLogin(StaffLogin staffLogin) {
        this.staff = staffLogin.staff;
        this.accessToken = staffLogin.accessToken;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}