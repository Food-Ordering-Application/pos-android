package com.foa.smartpos.network.response;

import com.foa.smartpos.model.Order;
import com.google.gson.annotations.SerializedName;

public class AutoConfirmData {

    @SerializedName("isAutoConfirm")
    boolean isAutoConfirm;

    public boolean isAutoConfirm() {
        return isAutoConfirm;
    }

    public void setAutoConfirm(boolean autoConfirm) {
        isAutoConfirm = autoConfirm;
    }
}

