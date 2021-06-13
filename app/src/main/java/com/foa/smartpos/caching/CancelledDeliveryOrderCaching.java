package com.foa.smartpos.caching;

import com.foa.smartpos.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CancelledDeliveryOrderCaching {
    private static List<Order> orderList = null;

    public CancelledDeliveryOrderCaching() {
    }

    public static void setCancelledDeliveryCatching(List<Order> orders){
        orderList = orders;
    }

    public static void addCancelledDeliveryCatching(Order order){
        if (orderList == null){
            orderList  = new ArrayList<>();
        }
        boolean isContain  = orderList.contains(order);
        if (!isContain) orderList.add(order);
    }

    public static List<Order> getCancelledOrderCatching(){
        return orderList;
    }

    public static void clearInstance(){
        orderList = null;
    }
}
