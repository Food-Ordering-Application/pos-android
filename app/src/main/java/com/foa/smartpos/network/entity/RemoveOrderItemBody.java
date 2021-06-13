package com.foa.smartpos.network.entity;

import com.google.gson.annotations.SerializedName;


public class RemoveOrderItemBody {
    @SerializedName("orderItemId")
    private String orderItemId;

    public RemoveOrderItemBody(String orderItemId) {
        this.orderItemId = orderItemId;
    }
}
