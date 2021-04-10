package com.foa.pos.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order{
	private String orderID;
	private Date createdOn;
	private Date updatedOn;
	private Date sycnOn;
	private String description;
	private double tax;
	private double discount;
	private double amount;
	private String userID;
	private String branchID;
    private boolean status;
	private int hasAudit;
    private String tableID;

	private String userName;
	private String tableName;
	private String branchName;
	private boolean isSelected;

	private ArrayList<OrderDetails> orderDetails;
	public String getOrderID() {
		return orderID;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Date getSycnOn() {
		return sycnOn;
	}
	public void setSycnOn(Date sycnOn) {
		this.sycnOn = sycnOn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getBranchID() {
		return branchID;
	}
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	public ArrayList<OrderDetails> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(ArrayList<OrderDetails> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public boolean getStatus() {
		return true;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public int getHasAudit() {
		return hasAudit;
	}

	public void setHasAudit(int hasAudit) {
		this.hasAudit = hasAudit;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public int getSumQuantity(){
		int sumQty = 0;
		List<OrderDetails> orderDetails = getOrderDetails();
		for ( OrderDetails item:orderDetails) {
			sumQty+= item.getQty();
		}
		return sumQty;
	}

	public String getStringCreatedOn(){
		DateFormat dateFormat = new SimpleDateFormat("hh:mm");
		return  dateFormat.format(createdOn);
	}
}
