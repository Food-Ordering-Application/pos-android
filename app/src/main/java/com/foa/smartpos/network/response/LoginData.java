package com.foa.smartpos.network.response;

import com.foa.smartpos.model.Staff;
import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName( "user" )
    Staff staff;
    @SerializedName("access_token")
    String accessToken;

    public LoginData() {
    }

    public LoginData(Staff staff, String accessToken) {
        this.staff = staff;
        this.accessToken = accessToken;
    }

    public void setStaffLogin(LoginData staffLogin) {
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

    public String getBearerAccessToken() {
        return "Bearer "+accessToken;
    }
}