package com.foa.pos.entity;

import com.foa.pos.utils.Helper;

import java.util.Date;

public class OrderDelivery {
    String OrderId;
    User driver;
    DeliveryStatus status;
    String address;
    Date acceptanceDeadline;

    public OrderDelivery(String orderId, User driver, DeliveryStatus status, String address, Date acceptanceDeadline) {
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

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
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

    public static OrderDelivery getDeliverySample(String orderId){
        return new OrderDelivery(orderId, User.getSampleUser(), DeliveryStatus.WAITING,"300 Lý Thường Kiệt, Q. Tân Bình, TPHCM", Helper.getMinuteChange(new Date(),5));
    }
}
