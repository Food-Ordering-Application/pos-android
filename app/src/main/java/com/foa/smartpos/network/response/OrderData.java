package com.foa.smartpos.network.response;

import com.foa.smartpos.model.Order;
import com.google.gson.annotations.SerializedName;

public class OrderData {

    @SerializedName("order")
    Order order;

    public OrderData(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

