package com.foa.smartpos.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.foa.smartpos.dialog.NetworkWarningDialog;
import com.foa.smartpos.network.utils.NetworkStatus;
import com.foa.smartpos.network.utils.NetworkUtils;

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


                });
                networkWarningDialog.show();
                break;
            default:
               break;
        }

    }
}
