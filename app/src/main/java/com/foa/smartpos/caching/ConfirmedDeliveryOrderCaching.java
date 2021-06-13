package com.foa.smartpos.caching;

import com.foa.smartpos.model.Order;

import java.util.ArrayList;
import java.util.List;

public class ConfirmedDeliveryOrderCaching {
    private static List<Order> orderList = null;

    public ConfirmedDeliveryOrderCaching() {
    }

    public static void setConfirmedDeliveryCatching(List<Order> orders){
        orderList = orders;
    }

    public static void addConfirmedDeliveryCatching(Order order){
        if (orderList == null){
            orderList  = new ArrayList<>();
        }
        boolean isContain  = orderList.contains(order);
        if (!isContain) orderList.add(order);
    }

    public static List<Order> getConfirmedOrderCatching(){
        return orderList;
    }
    public static void clearInstance(){
        orderList = null;
    }
}
