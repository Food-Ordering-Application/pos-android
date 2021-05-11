package com.foa.pos.network.response;

import com.foa.pos.model.Menu;
import com.foa.pos.model.MenuGroup;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuData {
    @SerializedName("menu")
    private Menu menu;
    @SerializedName("menuGroups")
    private List<MenuGroup> menuGroups;

    public MenuData(Menu menu, List<MenuGroup> menuGroups) {
        this.menu = menu;
        this.menuGroups = menuGroups;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<MenuGroup> getMenuGroups() {
        return menuGroups;
    }

    public void setMenuGroups(List<MenuGroup> menuGroups) {
        this.menuGroups = menuGroups;
    }
}
