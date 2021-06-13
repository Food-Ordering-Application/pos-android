package com.foa.smartpos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuGroup {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("index")
    private int index;
    @SerializedName("menuItems")
    List<MenuItem> menuItems;

    public MenuGroup() {
    }

    public MenuGroup(String id, String name, int index, List<MenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.index = index;
        this.menuItems = menuItems;
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

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
