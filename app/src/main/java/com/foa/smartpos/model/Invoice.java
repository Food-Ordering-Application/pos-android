package com.foa.smartpos.model;

import com.foa.smartpos.model.enums.InvoiceStatus;
import com.google.gson.annotations.SerializedName;

public class Invoice {
    @SerializedName("status")
    private InvoiceStatus status;
    @SerializedName("payment")
    private Payment payment;
}
