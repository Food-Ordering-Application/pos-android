package com.foa.pos.model;

import com.foa.pos.model.enums.StockState;
import com.google.gson.annotations.SerializedName;


public class OrderItemTopping {
    @SerializedName("id")
    private String id;
    @SerializedName("menuItemToppingId")
    private String menuItemToppingId;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private long price;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("state")
    private StockState state;

    public OrderItemTopping(String id, String menuItemToppingId, String name, long price, int quantity, StockState state) {
        this.id = id;
        this.menuItemToppingId = menuItemToppingId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuItemToppingId() {
        return menuItemToppingId;
    }

    public void setMenuItemToppingId(String menuItemToppingId) {
        this.menuItemToppingId = menuItemToppingId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StockState getState() {
        return state;
    }

    public void setState(StockState state) {
        this.state = state;
    }
}
