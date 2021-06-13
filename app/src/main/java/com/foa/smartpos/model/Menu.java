package com.foa.smartpos.model;

import com.google.gson.annotations.SerializedName;

public class Menu {
    @SerializedName("menuId")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("index")
    private int index;

    public Menu(String id, String name, int index) {
        this.id = id;
        this.name = name;
        this.index = index;
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
}
