package com.foa.pos.utils;

import com.foa.pos.network.response.StaffLogin;

public class LoginSession {
    private static  StaffLogin staffLogin;

    public LoginSession() {
    }

    public static StaffLogin getInstance(){
        if (staffLogin == null){
            return new StaffLogin();
        }
        return staffLogin;
    }

    public void setStaffLogin(StaffLogin staffLogin){
        this.setStaffLogin(staffLogin);
    }
}
