package com.foa.smartpos.caching;

import com.foa.smartpos.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DetailDeliveryOrderCaching {
    private static List<Order> orderList = null;

    public DetailDeliveryOrderCaching() {
    }

    public static void addDeliveryCatching(Order order){
        if (orderList == null){
            orderList  = new ArrayList<>();
        }
        boolean isContain  = orderList.contains(order);
        if (!isContain) orderList.add(order);
    }

    public static Order getOrderCatching(Order order){
        if (orderList==null) return null;
        for (int i = 0; i < orderList.size(); i++) {
            if(orderList.get(i).getId().equals(order.getId())){
                return orderList.get(i);
            }
        }
        return null;
    }

    public static void clearInstance(){
        orderList = null;
    }
}
