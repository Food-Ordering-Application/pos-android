package com.foa.smartpos.notification;

import android.util.Log;

import com.foa.smartpos.service.OrderService;
import com.foa.smartpos.utils.DeliveryOrderQueue;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;

public class NotificationsMessagingService extends MessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        OrderService.getOrderById("5a087577-8679-4e6b-92a2-259fab9cc125", (success, data) -> {
				if (success){
                    DeliveryOrderQueue.getInstance().add(data);
				}
			});
    }
}