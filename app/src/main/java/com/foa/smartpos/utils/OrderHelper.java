package com.foa.smartpos.utils;

import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.OrderItemTopping;
import com.foa.smartpos.session.OrderSession;

import java.util.ArrayList;

public class OrderHelper {
    public static Order updateOrderStatistic(Order order){
        long orderSubTotal=0;
        for (OrderItem orderItem :order.getOrderItems()) {
            orderSubTotal+=orderItem.getSubTotal();
        }
        order.setSubTotal(orderSubTotal);
        order.setGrandTotal(orderSubTotal);
        return order;
    }

    public static OrderItem updateOrderItemStatistic(OrderItem orderItem){
        long toppingSubTotal = 0;
        for (OrderItemTopping orderItemTopping:orderItem.getOrderItemToppings()) {
            toppingSubTotal+= orderItemTopping.getPrice() + orderItemTopping.getQuantity();
        }

        orderItem.setSubTotal((toppingSubTotal+orderItem.getPrice())* orderItem.getQuantity());
        return orderItem;
    }

    public static void removeOrderItemByPosition(int pos){
        Order order = OrderSession.getInstance();
        order.getOrderItems().remove(pos);
        order = updateOrderStatistic(order);
        OrderSession.setInstance(order);
    }

    public static void restoreOrderItemByPosition(OrderItem item,int pos){
        Order order = OrderSession.getInstance();
        order.getOrderItems().add(pos,item);
        order = updateOrderStatistic(order);
        OrderSession.setInstance(order);
    }

    public static void removeAllOrderItems(){
        Order order = OrderSession.getInstance();
        order.setOrderItems(new ArrayList<>());
        order = updateOrderStatistic(order);
        OrderSession.setInstance(order);
    }
}
