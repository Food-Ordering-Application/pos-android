package com.foa.pos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.foa.pos.dialog.NetworkWarningDialog;
import com.foa.pos.network.utils.NetworkStatus;
import com.foa.pos.network.utils.NetworkUtils;
import com.foa.pos.utils.LoginSession;

public class NetworkReceiver extends BroadcastReceiver {
    NetworkWarningDialog networkWarningDialog;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;



        NetworkStatus status = NetworkUtils.getConnectivityStatusString(context);
        switch (status) {
            case CONNETED:
                Toast.makeText(context, "Đã kết nối lại internet", Toast.LENGTH_SHORT).show();
                break;
            case DISCONNECTED:
                networkWarningDialog = new NetworkWarningDialog(context);
                networkWarningDialog.setSaleModeChangeListener(result -> {
                    LoginSession.clearInstance();

                });
                networkWarningDialog.show();
                break;
            default:
               break;
        }

    }
}
