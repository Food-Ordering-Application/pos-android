package com.foa.smartpos.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foa.smartpos.fragment.NumPadFragment;
import com.foa.smartpos.R;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.model.enums.PaymentType;
import com.foa.smartpos.network.utils.NetworkStatus;
import com.foa.smartpos.network.utils.NetworkUtils;
import com.foa.smartpos.service.OrderService;
import com.foa.smartpos.sqlite.DatabaseManager;
import com.foa.smartpos.sqlite.ds.OrderDataSource;
import com.foa.smartpos.utils.LoggerHelper;
import com.foa.smartpos.utils.OrderSession;


public class PaymentDialog  extends DialogFragment implements View.OnClickListener{

    View view;
    Context context;
    PaymentListener listener;
    boolean isFirstClick =true;
    private TextView tvTotalPay;
    private TextView tvChange;
    private Button btnPay;
    private Button btnCancel;
    private Order currentOrder;
    private OrderDataSource orderDS;
    private long totalChange = 0;

    public PaymentDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        view = getActivity().getLayoutInflater().inflate(R.layout.payment_dialog, null, false);

        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.getWindow().setLayout(400,600);
        builder.setContentView(view);

        tvTotalPay = view.findViewById(R.id.tvTotalPay);
        tvChange = view.findViewById(R.id.tvChange);
        btnPay =  view.findViewById(R.id.btnPay);
        btnCancel =  view.findViewById(R.id.btnCancel);

        currentOrder= OrderSession.getInstance();
        tvTotalPay.setText(String.valueOf(currentOrder.getSubTotal()));

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        orderDS = new OrderDataSource(db);

        btnPay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return builder;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        NumPadFragment oldNumpadFragment = (NumPadFragment)fm.findFragmentById(R.id.fragment_numpad);
        if (oldNumpadFragment!=null){
            oldNumpadFragment.setNumPadClickListener(data -> receiveData(data));
        }else{
            NumPadFragment numPadFragment = new NumPadFragment();
            ft.replace(R.id.fragment_numpad,numPadFragment,null);
            numPadFragment.setNumPadClickListener(data -> receiveData(data));
            ft.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(800,1200);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fm =  getActivity().getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        NumPadFragment oldNumpadFragment = (NumPadFragment)fm.findFragmentById(R.id.fragment_numpad);
        if (oldNumpadFragment!=null){
            ft.remove(oldNumpadFragment);
            ft.commit();
        }

    }

    public void receiveData(String data) {
        String totalPay = tvTotalPay.getText().toString();
        String newTotalPay;
        switch (data){
            case "B":
                if (totalPay.length()>1){
                    newTotalPay =  totalPay.substring(0,totalPay.length()-1);
                }else{
                    newTotalPay = "0";
                }

                break;
            case "C":
                newTotalPay = "0";
                break;
            default:
                if(isFirstClick){
                    newTotalPay = data;
                    isFirstClick = false;
                }else{
                    if (totalPay.equals("0")){
                        newTotalPay = data;
                    }else
                        newTotalPay = tvTotalPay.getText()+data;
                }
        }
        tvTotalPay.setText(newTotalPay);
        totalChange = Long.parseLong(newTotalPay)-currentOrder.getGrandTotal();
        if (totalChange<0) btnPay.setEnabled(false);
        tvChange.setText(String.valueOf(totalChange));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnCancel:
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                listener.onFinish(false);
                dismiss();
                break;
            case R.id.btnPay:
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                if(listener != null){
                    Order order =OrderSession.getInstance();
                    order.setPaymentType(PaymentType.COD);
                    if(NetworkUtils.getConnectivityStatusString(context)== NetworkStatus.CONNETED){
                        OrderService.syncOrder(order, success -> {
                            if (success){
                                LoggerHelper.CheckAndLogInfo(PaymentDialog.this,"Order is synced");
                            }
                        });
                    }

                    orderDS.updateOrderStatus(order.getId(), OrderStatus.COMPLETED);
                    dismiss();
                    OrderSession.clearInstance();
                    listener.onFinish(true);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setPaymentListener(PaymentDialog.PaymentListener listener)
    {
        this.listener = listener;
    }

    public interface PaymentListener {
        void onFinish(boolean result);
    }

}
