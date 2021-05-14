package com.foa.pos.model;

import com.foa.pos.model.enums.StockState;
import com.foa.pos.network.entity.SendOrderItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {
	@SerializedName( "id" )
	private String id;
	@SerializedName( "orderId" )
	private String orderId;
	@SerializedName( "menuItemId" )
	private String menuItemId;
	@SerializedName( "menuItemName" )
	private String menuItemName;
	@SerializedName( "quantity" )
	private int quantity;
	@SerializedName( "discount" )
	private long discount;
	@SerializedName( "price" )
	private long price;
	@SerializedName( "state" )
	private StockState stockState;
	@SerializedName( "orderItemToppings" )
	private List<OrderItemTopping> orderItemToppings;

	private String note;
	private long subTotal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(String menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getDiscount() {
		return discount;
	}

	public void setDiscount(long discount) {
		this.discount = discount;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public StockState getStockState() {
		return stockState;
	}

	public void setStockState(StockState stockState) {
		this.stockState = stockState;
	}

	public List<OrderItemTopping> getOrderItemToppings() {
		return orderItemToppings;
	}

	public void setOrderItemToppings(List<OrderItemTopping> orderItemToppings) {
		this.orderItemToppings = orderItemToppings;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	//view helper method

	public long getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(long subTotal) {
		this.subTotal = subTotal;
	}

	public SendOrderItem createSendOrderItem(){
		List<OrderItemTopping> orderItemTopping = new ArrayList<>();
		//orderItemTopping.add(new OrderItemTopping(null,"123-123",5000,1,StockState.IN_STOCK));
		return new SendOrderItem(menuItemId,price,menuItemName,quantity, orderItemTopping);
	}
}


