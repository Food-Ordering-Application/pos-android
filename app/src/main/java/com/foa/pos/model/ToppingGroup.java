package com.foa.pos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToppingGroup {
    @SerializedName("id")
    private String id;
    @SerializedName("menuId")
    private String menuId;
    @SerializedName("name")
    private String name;
    @SerializedName("index")
    private float index;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("toppingItems")
    private List<ToppingItem> toppingItems;

    public ToppingGroup() {
    }

    public ToppingGroup(String id, String name, int index, boolean isActive, List<ToppingItem> toppingItems) {
        this.id = id;
        this.name = name;
        this.index = index;
        this.isActive = isActive;
        this.toppingItems = toppingItems;
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

    public List<ToppingItem> getToppingItems() {
        return toppingItems;
    }

    public void setToppingItems(List<ToppingItem> toppingItems) {
        this.toppingItems = toppingItems;
    }
}
