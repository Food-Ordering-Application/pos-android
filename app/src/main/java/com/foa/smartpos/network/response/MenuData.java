package com.foa.smartpos.network.response;

import com.google.gson.annotations.SerializedName;

public class MenuData {
    @SerializedName("menuId")
    String menuId;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
