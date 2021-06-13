package com.foa.smartpos.session;


public class NotificationOrderIdSession {
    private static String currentOrderId = null;

    public NotificationOrderIdSession() {
    }

    public static String getInstance(){
        return currentOrderId;
    }

    public static void setInstance(String currentOrder){
        NotificationOrderIdSession.currentOrderId = currentOrder;
    }

    public static void clearInstance(){
        NotificationOrderIdSession.currentOrderId = null;
    }
}
