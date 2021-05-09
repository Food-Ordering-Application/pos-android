package com.foa.pos.model;

import com.google.gson.annotations.SerializedName;

public class MenuItemToppingCustom {
    @SerializedName("id")
    private String id;
    @SerializedName("customPrice")
    private long customPrice;

    public MenuItemToppingCustom(String id, long price) {
        this.id = id;
        this.customPrice = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(long customPrice) {
        this.customPrice = customPrice;
    }
}
