package com.foa.smartpos.model;

import com.foa.smartpos.model.enums.StockState;
import com.google.gson.annotations.SerializedName;

public class ToppingItem {
    @SerializedName("id")
    private String id;
    @SerializedName("menuId")
    private String menuId;
    @SerializedName("toppingGroupId")
    private String toppingGroupId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("maxQuantity")
    private int maxQuantity;
    @SerializedName("index")
    private float index;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("price")
    private long price;
    @SerializedName("state")
    private StockState stockState;
    private int radioButtonId;

    public ToppingItem() {
    }

    public ToppingItem(String id, String menuId, String toppingGroupId, String name, String description, int maxQuantity, float index, boolean isActive, long price, int radioButtonId) {
        this.id = id;
        this.menuId = menuId;
        this.toppingGroupId = toppingGroupId;
        this.name = name;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.index = index;
        this.isActive = isActive;
        this.price = price;
        this.radioButtonId = radioButtonId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getToppingGroupId() {
        return toppingGroupId;
    }

    public void setToppingGroupId(String toppingGroupId) {
        this.toppingGroupId = toppingGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public float getIndex() {
        return index;
    }

    public void setIndex(float index) {
        this.index = index;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public StockState getStockState() {
        return stockState;
    }

    public void setStockState(StockState stockState) {
        this.stockState = stockState;
    }

    public int getRadioButtonId() {
        return radioButtonId;
    }

    public void setRadioButtonId(int radioButtonId) {
        this.radioButtonId = radioButtonId;
    }

    public OrderItemTopping createOrderItemTopping(){
        return new OrderItemTopping("",id,name,price,1, StockState.IN_STOCK);
    }
}
