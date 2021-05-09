package com.foa.pos.network.entity;

import com.foa.pos.model.MenuItemTopping;
import com.foa.pos.model.OrderItemTopping;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SendOrderItem {
    @SerializedName("menuItemId")
    private String menuItemId;
    @SerializedName("price")
    private long price;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("orderItemToppings")
    private List<OrderItemTopping> orderItemToppings;

    public SendOrderItem(String menuItemId, long price, int quantity, List<OrderItemTopping> orderItemToppings) {
        this.menuItemId = menuItemId;
        this.price = price;
        this.quantity = quantity;
        this.orderItemToppings = orderItemToppings;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
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
