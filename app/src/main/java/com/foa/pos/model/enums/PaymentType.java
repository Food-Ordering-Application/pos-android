package com.foa.pos.model.enums;

import com.google.gson.annotations.SerializedName;

public enum PaymentType {
    @SerializedName("COD")
    COD,
    @SerializedName("PAYPAL")
    PAYPAL
}