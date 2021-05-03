package com.foa.pos.model;

import com.foa.pos.model.enums.StockState;
import com.google.gson.annotations.SerializedName;

public class OrderItem {
	@SerializedName( "id" )
	private String id;
	private String orderId;
	private String menuItemId;
	private String menuItemName;
	@SerializedName( "quantity" )
	private int quantity;
	@SerializedName( "discount" )
	private long discount;
	private long price;
	private StockState stockState;
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
}
