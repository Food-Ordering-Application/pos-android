package com.foa.pos.model;

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

	private String cashierId;
	private String customerAddressId;
	@SerializedName( "driverId" )
	private String driverId;
	@SerializedName( "restaurantId" )
	private String restaurantId;
	@SerializedName( "subTotal" )
	private long subTotal;
	@SerializedName( "itemDiscount" )
	private long itemDiscount;
	@SerializedName( "discount" )
	private long discount;
	@SerializedName( "shippingFee" )
	private long shippingFee;
	@SerializedName( "serviceFee" )
	private long serviceFee;
	@SerializedName( "grandTotal" )
	private long grandTotal;
	private int paymentMode;
	@SerializedName( "paymentType" )
	private int paymentType;
	private String note;
	private int status;
	@SerializedName( "deliveredAt" )
	private Date deliveredAt;
	@SerializedName( "createdAt" )
	private Date createdAt;
	@SerializedName( "updatedAt" )
	private Date updatedAt;
	@SerializedName( "orderItems" )
	private ArrayList<OrderItem> orderItems;
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

	public String getCustomerAddressId() {
		return customerAddressId;
	}

	public void setCustomerAddressId(String customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
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

	public long getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(long shippingFee) {
		this.shippingFee = shippingFee;
	}

	public float getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(long serviceFee) {
		this.serviceFee = serviceFee;
	}

	public long getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(long grandTotal) {
		this.grandTotal = grandTotal + this.grandTotal;
	}

	public int getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(int paymentMode) {
		this.paymentMode = paymentMode;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public ArrayList<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(ArrayList<OrderItem> orderItems) {
		this.orderItems = orderItems;
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
	}

}
