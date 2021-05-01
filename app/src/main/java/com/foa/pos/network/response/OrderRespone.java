package com.foa.pos.network.response;

import com.foa.pos.model.Order;
import com.google.gson.annotations.SerializedName;

public class OrderRespone {
    @SerializedName( "statusCode" )
    private int status;
    @SerializedName( "message" )
    private String message;
    @SerializedName( "data" )
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    static class Data{
        @SerializedName( "order" )
        Order order;

        public Data(Order order) {
            this.order = order;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }
    }
}

