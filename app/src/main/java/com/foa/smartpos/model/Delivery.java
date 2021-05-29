package com.foa.smartpos.model;

import com.google.gson.annotations.SerializedName;

public class Delivery {
    @SerializedName("id")
    private String id;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("driverId")
    private String driverId;
    @SerializedName("customerAddress")
    private String customerAddress;
    @SerializedName("customerGeom")
    private Geom customerGeom;
    @SerializedName("restaurantAddress")
    private String restaurantAddress;
    @SerializedName("restaurantGeom")
    private Geom geom;
    @SerializedName("distance")
    private float distance;
    @SerializedName("shippingFee")
    private long shippingFee;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("deliveredAt")
    private String deliveredAt;

    public Delivery(String id, String customerId, String driverId, String customerAddress, Geom customerGeom, String restaurantAddress, Geom geom, float distance, long shippingFee, String createdAt, String updatedAt, String deliveredAt) {
        this.id = id;
        this.customerId = customerId;
        this.driverId = driverId;
        this.customerAddress = customerAddress;
        this.customerGeom = customerGeom;
        this.restaurantAddress = restaurantAddress;
        this.geom = geom;
        this.distance = distance;
        this.shippingFee = shippingFee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deliveredAt = deliveredAt;
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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
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
}
