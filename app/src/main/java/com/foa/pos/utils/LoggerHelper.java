package com.foa.pos.utils;

import android.util.Log;


public class LoggerHelper {
    public static void CheckAndLogInfo(Object container,String text){
        Log.e(container.toString(),text);
    }

}
