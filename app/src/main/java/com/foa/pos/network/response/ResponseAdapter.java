package com.foa.pos.network.response;

import com.google.gson.annotations.SerializedName;

public class ResponseAdapter<T> {
        @SerializedName( "statusCode" )
        private int status;
        @SerializedName( "message" )
        private String message;
        @SerializedName( "data" )
        private T data;

    public ResponseAdapter(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
