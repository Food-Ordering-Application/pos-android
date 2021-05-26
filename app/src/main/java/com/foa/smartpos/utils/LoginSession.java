package com.foa.smartpos.utils;

import com.foa.smartpos.network.response.LoginData;

public class LoginSession {
    private static LoginData staffLogin = null;

    public LoginSession() {
    }

    public static LoginData getInstance(){
        return staffLogin;
    }

    public static void setInstance(LoginData staffLogin){
        LoginSession.staffLogin = staffLogin;
    }

    public static void clearInstance(){
        LoginSession.staffLogin = null;
    }
}
