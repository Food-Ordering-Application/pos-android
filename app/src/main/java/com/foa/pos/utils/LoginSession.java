package com.foa.pos.utils;

import com.foa.pos.network.response.LoginData;

public class LoginSession {
    private static LoginData staffLogin = null;

    public LoginSession() {
    }

    public static LoginData getInstance(){
        if (staffLogin == null){
            staffLogin  = new LoginData();
        }
        return staffLogin;
    }

    public void setStaffLogin(LoginData staffLogin){
        LoginSession.staffLogin = staffLogin;
    }
}
