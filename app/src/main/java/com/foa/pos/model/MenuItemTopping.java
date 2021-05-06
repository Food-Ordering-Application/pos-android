package com.foa.pos.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MenuItemTopping {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("maxQuantity")
    private int maxQuantity;
    @SerializedName("index")
    private int index;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("price")
    private long price;
    @SerializedName("menuItemToppings")
    private List<MenuItemToppingCustom> menuItemToppingCustomList;

    public MenuItemTopping(String id, String name, String description, int maxQuantity, int index, boolean isActive, long price, List<MenuItemToppingCustom> menuItemToppingCustomList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.index = index;
        this.isActive = isActive;
        this.price = price;
        this.menuItemToppingCustomList = menuItemToppingCustomList;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public List<MenuItemToppingCustom> getMenuItemToppingCustomList() {
        return menuItemToppingCustomList;
    }

    public void setMenuItemToppingCustomList(List<MenuItemToppingCustom> menuItemToppingCustomList) {
        this.menuItemToppingCustomList = menuItemToppingCustomList;
    }

    public static List<MenuItemTopping> getSampleList(){
        List<MenuItemTopping> toppingList = new ArrayList<>();
        List<MenuItemToppingCustom> customPriceList = new ArrayList<>();
        customPriceList.add(new MenuItemToppingCustom("123",6000));
        toppingList.add(new MenuItemTopping("123","Tran chau","",5,1,true,
                5000,customPriceList));
        toppingList.add(new MenuItemTopping("124","Hat dau","",5,1,true,
                7000,customPriceList));
        return toppingList;
    }
}
