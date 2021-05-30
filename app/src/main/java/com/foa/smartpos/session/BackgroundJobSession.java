package com.foa.smartpos.session;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.foa.smartpos.receiver.BackgroundJobReceiver;
import com.foa.smartpos.utils.LoggerHelper;

public class BackgroundJobSession {

    private static AlarmManager alarmManager = null;
    private static  PendingIntent pendingIntent =null;

    public BackgroundJobSession(Context context) {
        alarmManager =  (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BackgroundJobReceiver.class);
        intent.setAction("SYNC_ACTION");
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void resetInstance(Context context, int minute){
        if (alarmManager==null){
            new BackgroundJobSession(context);
        }

       long alarmTimeAtUTC = System.currentTimeMillis() + minute * 1000*60*60;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, pendingIntent);
        LoggerHelper.CheckAndLogInfo(context,"background syncing reset time");
    }

    public static void clearInstance(){
        alarmManager.cancel(pendingIntent);
    }
}
