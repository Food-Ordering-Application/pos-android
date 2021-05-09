package com.foa.pos.model.enums;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {
    @SerializedName("DRAFT")
    DRAFT,
    @SerializedName("COMPLETED")
    COMPLETED
}