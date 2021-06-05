package com.foa.smartpos.model.enums;

import com.google.gson.annotations.SerializedName;

public enum PaymentStatus {
    @SerializedName("PENDING_USER_ACTION")
    PENDING_USER_ACTION ,
    @SerializedName("PROCESSING")
    PROCESSING ,
    @SerializedName("SUCCESS")
    SUCCESS ,
    @SerializedName("FAILURE")
    FAILURE,
    @SerializedName("REFUNDED")
    REFUNDED ,
    @SerializedName("PENDING_REFUNDED")
    PENDING_REFUNDED ,
    @SerializedName("CANCELLED")
    CANCELLED,
}
