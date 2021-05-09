package com.foa.pos.network.response;

import com.foa.pos.model.Order;
import com.google.gson.annotations.SerializedName;

public class OrderData {

    @SerializedName("order")
    Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

