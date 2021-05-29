package com.foa.smartpos.model.enums;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {
    @SerializedName("DRAFT")
    DRAFT,
    @SerializedName("ORDERED")
    ORDERED,
    @SerializedName("COMPLETED")
    COMPLETED,
    @SerializedName("CONFIRMED")
    CONFIRMED,
    @SerializedName("CANCELED")
    CANCELED
}