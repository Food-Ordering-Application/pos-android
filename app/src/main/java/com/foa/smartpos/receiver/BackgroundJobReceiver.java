package com.foa.smartpos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.session.BackgroundJobSession;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.LoggerHelper;

import java.util.List;

public class BackgroundJobReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("SYNC_ACTION")) {
            syncOrder(context);
        }
    }

    void syncOrder(Context context){
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            OrderDataSource orderDS = new OrderDataSource(db);
            List<Order> orderList = orderDS.getAllOrder(true);
            orderList.forEach(item -> {
                OrderService.syncOrder(item, success -> {
                    if (success){
                        orderDS.updateSyncAt(item.getId());
                        LoggerHelper.CheckAndLogInfo(context,item.getId().concat(" synced"));
                    }

                });
            });

            BackgroundJobSession.resetInstance(context,10);
        }catch (IllegalStateException e){

        }

    }
}
