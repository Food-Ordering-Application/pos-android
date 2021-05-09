package com.foa.pos.network.entity;

import com.google.gson.annotations.SerializedName;


public class NewOrderBody {
    @SerializedName("orderItem")
    private SendOrderItem orderItem;
    @SerializedName("restaurantId")
    private String restaurantId;
    @SerializedName("customerId")
    private String customerId;
    private @SerializedName("cashierId")
    String cashierId;

    public NewOrderBody(SendOrderItem orderItem, String restaurantId, String customerId, String cashierId) {
        this.orderItem = orderItem;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.cashierId = cashierId;
    }

    public SendOrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(SendOrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }
}
