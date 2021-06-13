package com.foa.smartpos.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class VoidOrderBody {
    @SerializedName("orderItemIds")
    private List<String> orderItemId;
    @SerializedName("cashierNote")
    private String cashierNote;

    public VoidOrderBody(List<String> orderItemId, String cashierNote) {
        this.orderItemId = orderItemId;
        this.cashierNote = cashierNote;
    }
}
