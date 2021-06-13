package com.foa.smartpos.model;

import com.foa.smartpos.model.enums.IssueType;
import com.google.gson.annotations.SerializedName;

public class Delivery {
    @SerializedName("id")
    private String id;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("customerPhoneNumber")
    private String customerPhoneNumber;
    @SerializedName("customerAddress")
    private String customerAddress;
    @SerializedName("customerGeom")
    private Geom customerGeom;
    @SerializedName("driverId")
    private String driverId;
    @SerializedName("restaurantAddress")
    private String restaurantAddress;
    @SerializedName("restaurantName")
    private String restaurantName;
    @SerializedName("restaurantPhoneNumber")
    private String restaurantPhoneNumber;
    @SerializedName("restaurantGeom")
    private Geom geom;
    @SerializedName("distance")
    private float distance;
    @SerializedName("shippingFee")
    private long shippingFee;
    @SerializedName("status")
    private String status;
    @SerializedName("issueNote")
    private String issueNote;
    @SerializedName("issueType")
    private IssueType issueType;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("deliveredAt")
    private String deliveredAt;
    @SerializedName("orderItem")
    private String orderItem;
    @SerializedName("expectedDeliveryTime")
    private String expectedDeliveryTime;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Geom getCustomerGeom() {
        return customerGeom;
    }

    public void setCustomerGeom(Geom customerGeom) {
        this.customerGeom = customerGeom;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPhoneNumber() {
        return restaurantPhoneNumber;
    }

    public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
        this.restaurantPhoneNumber = restaurantPhoneNumber;
    }

    public Geom getGeom() {
        return geom;
    }

    public void setGeom(Geom geom) {
        this.geom = geom;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(long shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public String getIssueNote() {
        return issueNote;
    }

    public void setIssueNote(String issueNote) {
        this.issueNote = issueNote;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(String expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }
}
