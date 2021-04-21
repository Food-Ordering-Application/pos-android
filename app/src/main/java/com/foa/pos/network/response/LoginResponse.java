package com.foa.pos.network.response;

import com.foa.pos.model.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName( "statusCode" )
    private int status;
    @SerializedName( "data" )
    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName( "user" )
        User user;
        @SerializedName("accessToken")
        String accessToken;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}