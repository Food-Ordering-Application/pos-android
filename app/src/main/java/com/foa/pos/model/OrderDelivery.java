package com.foa.pos.model;

import com.foa.pos.utils.Helper;

import java.util.Date;

public class OrderDelivery {
    String OrderId;
    Staff driver;
    DeliveryStatus status;
    String address;
    Date acceptanceDeadline;

    public OrderDelivery(String orderId, Staff driver, DeliveryStatus status, String address, Date acceptanceDeadline) {
        OrderId = orderId;
        this.driver = driver;
        this.status = status;
        this.address = address;
        this.acceptanceDeadline = acceptanceDeadline;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public Staff getDriver() {
        return driver;
    }

    public void setDriver(Staff driver) {
        this.driver = driver;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getAcceptanceDeadline() {
        return acceptanceDeadline;
    }

    public void setAcceptanceDeadline(Date acceptanceDeadline) {
        this.acceptanceDeadline = acceptanceDeadline;
    }

}
