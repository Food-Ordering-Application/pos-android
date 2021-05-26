package com.foa.smartpos.utils;

import com.foa.smartpos.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DeliveryOrderQueue {

    private static List<Order> orderList = null;

    public DeliveryOrderQueue() {
    }

    public static List<Order> getInstance(){
        if (orderList == null){
            orderList  = new ArrayList<>();
        }
        return orderList;
    }

    public static void setInstance(List<Order> currentOrder){
        DeliveryOrderQueue.orderList = currentOrder;
    }

    public static void addOrder(Order currentOrder){
        DeliveryOrderQueue.orderList.add(currentOrder);
    }

}
