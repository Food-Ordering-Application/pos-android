package com.foa.pos.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.foa.pos.fragment.NumPadFragment;
import com.foa.pos.R;
import com.foa.pos.model.IResultCallback;
import com.foa.pos.model.Order;
import com.foa.pos.model.enums.OrderStatus;
import com.foa.pos.model.enums.PaymentType;
import com.foa.pos.network.utils.NetworkStatus;
import com.foa.pos.network.utils.NetworkUtils;
import com.foa.pos.service.OrderService;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.LoggerHelper;
import com.foa.pos.utils.OrderSession;


public class PaymentDialog  extends DialogFragment implements View.OnClickListener{

    Context context;
    PaymentListener listener;
    boolean isFirstClick =true;
    private TextView tvTotalPay;
    private Button btnPay;
    private Button btnCancel;
    private Order currentOrder;
    private OrderDataSource orderDS;

    public PaymentDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        return new AlertDialog.Builder(context).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.payment_dialog, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalPay = view.findViewById(R.id.tvTotalPay);
        btnPay =  view.findViewById(R.id.btnPay);
        btnCancel =  view.findViewById(R.id.btnCancel);

        currentOrder= OrderSession.getInstance();
        tvTotalPay.setText(String.valueOf(currentOrder.getSubTotal()));

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        NumPadFragment numPadFragment = new NumPadFragment();
        ft.replace(R.id.fragment_numpad,numPadFragment,null);
        numPadFragment.setNumPadClickListener(data -> receiveData(data));
        ft.commit();



        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        orderDS = new OrderDataSource(db);

        btnPay.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fm =  getParentFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        NumPadFragment oldNumpadFragment = (NumPadFragment)fm.findFragmentById(R.id.fragment_numpad);
        if (oldNumpadFragment!=null){
            ft.remove(oldNumpadFragment);
            ft.commit();
        }

    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        setContentView(R.layout.payment_dialog);
//
//    }


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
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnCancel:
                listener.onFinish(false);
                dismiss();
                break;
            case R.id.btnPay:
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
                listener.onFinish(false);
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
