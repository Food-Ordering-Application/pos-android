package com.foa.pos.network.entity;

import com.google.gson.annotations.SerializedName;

public class MenuItemTopping {
    @SerializedName( "menuItemToppingId" )
    private String id;
    @SerializedName( "quantity" )
    private int quantity;
    @SerializedName( "price" )
    private long price;

    public MenuItemTopping(String id, int quantity, long price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
