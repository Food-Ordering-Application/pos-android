package com.foa.smartpos.network.entity;

import com.foa.smartpos.model.OrderItemTopping;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendOrderItem {
    @SerializedName("menuItemId")
    private String menuItemId;
    @SerializedName("price")
    private long price;
    @SerializedName("name")
    private String name;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("orderItemToppings")
    private List<OrderItemTopping> orderItemToppings;

    public SendOrderItem(String menuItemId, long price, String name, int quantity, List<OrderItemTopping> orderItemToppings) {
        this.menuItemId = menuItemId;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.orderItemToppings = orderItemToppings;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<OrderItemTopping> getOrderItemToppings() {
        return orderItemToppings;
    }

    public void setOrderItemToppings(List<OrderItemTopping> orderItemToppings) {
        this.orderItemToppings = orderItemToppings;
    }
}
