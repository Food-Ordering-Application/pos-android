package com.foa.smartpos.model;

import com.google.gson.annotations.SerializedName;

public class MenuItemTopping {
    @SerializedName("id")
    private String id;
    @SerializedName("menuId")
    private String menuId;
    @SerializedName("menuItemId")
    private String menuItemId;
    @SerializedName("toppingItemId")
    private String toppingItemId;
    @SerializedName("customPrice")
    private long customPrice;

    public MenuItemTopping(String id, String menuId, String menuItemId, String toppingItemId, long customPrice) {
        this.id = id;
        this.menuId = menuId;
        this.menuItemId = menuItemId;
        this.toppingItemId = toppingItemId;
        this.customPrice = customPrice;
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

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getToppingItemId() {
        return toppingItemId;
    }

    public void setToppingItemId(String toppingItemId) {
        this.toppingItemId = toppingItemId;
    }

    public long getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(long customPrice) {
        this.customPrice = customPrice;
    }
}
