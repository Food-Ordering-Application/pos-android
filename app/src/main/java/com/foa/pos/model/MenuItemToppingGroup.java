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

}
