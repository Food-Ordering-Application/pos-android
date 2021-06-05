package com.foa.smartpos.model;

import com.foa.smartpos.model.enums.PaymentMethod;
import com.foa.smartpos.model.enums.PaymentStatus;
import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("method")
    private PaymentMethod method;
    @SerializedName("status")
    private PaymentStatus status;
    @SerializedName("amount")
    private long amount;

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
