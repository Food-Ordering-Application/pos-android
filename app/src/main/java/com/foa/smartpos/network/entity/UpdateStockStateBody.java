package com.foa.smartpos.network.entity;

import com.foa.smartpos.model.enums.StockState;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UpdateStockStateBody {
    @SerializedName("state")
    private StockState state;

    public UpdateStockStateBody(StockState state) {
        this.state = state;
    }
}
