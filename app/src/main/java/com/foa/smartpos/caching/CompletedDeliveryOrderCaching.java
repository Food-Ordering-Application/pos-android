package com.foa.smartpos.caching;

import com.foa.smartpos.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CompletedDeliveryOrderCaching {
    private static List<Order> orderList = null;

    public CompletedDeliveryOrderCaching() {
    }

    public static void setCompletedDeliveryCatching(List<Order> orders){
       orderList = orders;
    }

    public static void addCompletedDeliveryCatching(Order order){
        if (orderList == null){
            orderList  = new ArrayList<>();
        }
        boolean isContain  = orderList.contains(order);
        if (!isContain) orderList.add(order);
    }

    public static List<Order> getCompletedOrderCatching(){
        return orderList;
    }

    public static void clearInstance(){
        orderList = null;
    }
}
