package com.foa.pos.model;

import com.foa.pos.model.enums.OrderStatus;
import com.foa.pos.model.enums.PaymentType;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order{
	@SerializedName( "id" )
	private String id;
	@SerializedName( "customerId" )
	private String customerId;
	@SerializedName( "cashierId" )
	private String cashierId;
	@SerializedName( "restaurantId" )
	private String restaurantId;
	@SerializedName( "subTotal" )
	private long subTotal;
	@SerializedName( "itemDiscount" )
	private long itemDiscount;
	@SerializedName( "discount" )
	private long discount;
	@SerializedName( "serviceFee" )
	private long serviceFee;
	@SerializedName( "grandTotal" )
	private long grandTotal;
	@SerializedName( "paymentType" )
	private PaymentType paymentType;
	private String note;
	@SerializedName( "status" )
	private OrderStatus status;
	@SerializedName( "deliveredAt" )
	private Date deliveredAt;
	@SerializedName( "createdAt" )
	private Date createdAt;
	@SerializedName( "updatedAt" )
	private Date updatedAt;
	@SerializedName( "orderItems" )
	private List<OrderItem> orderItems;
	// field for setting list view selector.
	private boolean isSelected;

	public Order() {
		this.orderItems = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public long getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(long subTotal) {
		this.subTotal = subTotal;
	}

	public long getItemDiscount() {
		return itemDiscount;
	}

	public void setItemDiscount(long itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	public long getDiscount() {
		return discount;
	}

	public void setDiscount(long discount) {
		this.discount = discount;
	}

	public long getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(long serviceFee) {
		this.serviceFee = serviceFee;
	}

	public long getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(long grandTotal) {
		this.grandTotal = grandTotal;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(Date deliveredAt) {
		this.deliveredAt = deliveredAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void addOrderItem(OrderItem orderItems) {
		this.orderItems.add(orderItems);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public int getSumQuantity(){
		int sumQty = 0;
		List<OrderItem> orderDetails = getOrderItems();
		for ( OrderItem item:orderDetails) {
			sumQty+= item.getQuantity();
		}
		return sumQty;
	}

	public String getStringCreatedAt(){
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss - dd/MM/yy");
		return  dateFormat.format(createdAt);
	}

	public void addOrderItemPrice(long orderItemPrice){
		this.subTotal += orderItemPrice;
		this.grandTotal = this.subTotal;
	}

}
