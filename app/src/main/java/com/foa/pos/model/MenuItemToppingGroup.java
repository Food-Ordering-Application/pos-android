package com.foa.pos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuItemToppingGroup {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("index")
    private int index;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("toppingItems")
    private List<MenuItemTopping> toppingItems;

    public MenuItemToppingGroup(String id, String name, int index, boolean isActive, List<MenuItemTopping> toppingItems) {
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<MenuItemTopping> getToppingItems() {
        return toppingItems;
    }

    public void setToppingItems(List<MenuItemTopping> toppingItems) {
        this.toppingItems = toppingItems;
    }
}
