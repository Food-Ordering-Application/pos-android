package com.foa.smartpos.model.enums;

import com.google.gson.annotations.SerializedName;

public enum  IssueType {
    @SerializedName("ITEM_IS_OUT_OF_STOCK")
    ITEM_IS_OUT_OF_STOCK,
    @SerializedName("RESTAURANT_CLOSED")
    RESTAURANT_CLOSED,
    @SerializedName("CANNOT_ASSIGN_DRIVER")
    CANNOT_ASSIGN_DRIVER ,
    @SerializedName("CANNOT_BUY_ORDER")
    CANNOT_BUY_ORDER ,
}
