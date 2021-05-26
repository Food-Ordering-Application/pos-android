package com.foa.smartpos.network.entity;

import com.google.gson.annotations.SerializedName;

public class UpdateQuantityBody {
    @SerializedName("orderItemId")
    private String orderItemId;
    @SerializedName("quantity")
    private int quantity;

    public UpdateQuantityBody(String orderItemId, int quantity) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
