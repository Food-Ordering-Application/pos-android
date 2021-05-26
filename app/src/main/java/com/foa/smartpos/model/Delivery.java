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
}
