package com.foa.pos.network.response;

import com.foa.pos.model.MenuItemToppingGroup;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToppingGroupData {
    @SerializedName("toppingGroups")
    private List<MenuItemToppingGroup> menuItemToppingGroup;

    public ToppingGroupData(List<MenuItemToppingGroup> menuItemToppingGroup) {
        this.menuItemToppingGroup = menuItemToppingGroup;
    }

    public List<MenuItemToppingGroup> getMenuItemToppingGroup() {
        return menuItemToppingGroup;
    }

    public void setMenuItemToppingGroup(List<MenuItemToppingGroup> menuItemToppingGroup) {
        this.menuItemToppingGroup = menuItemToppingGroup;
    }
}
