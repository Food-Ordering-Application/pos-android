package com.foa.smartpos.utils;

import com.foa.smartpos.model.enums.OrderStatus;

public class EnumHelper {

    public static String getOrderStatusString(OrderStatus orderStatus){
        switch (orderStatus){
            case ORDERED:
                return "Chờ xác nhận";
            case COMPLETED:
                return "Hoàn thành";
            case CONFIRMED:
                return"Đã xác nhận";
            case CANCELLED:
                return"Huỷ";
            default:
                return "";
        }
    }
}
