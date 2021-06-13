package com.foa.smartpos.network.entity;

import com.google.gson.annotations.SerializedName;

public class AddNewOrderItemBody {
    @SerializedName("sendItem")
    private SendOrderItem orderItem;

    public AddNewOrderItemBody(SendOrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
