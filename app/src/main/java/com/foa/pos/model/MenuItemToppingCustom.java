package com.foa.pos.model;

public class MenuItemToppingCustom {
    private String id;
    private long price;

    public MenuItemToppingCustom(String id, long price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
