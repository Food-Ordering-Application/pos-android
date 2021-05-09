package com.foa.pos.model.enums;

import com.google.gson.annotations.SerializedName;

public enum StockState {
    @SerializedName("OUT_OF_STOCK")
    OUT_OF_STOCK,
    @SerializedName("IN_STOCK")
    IN_STOCK
}
