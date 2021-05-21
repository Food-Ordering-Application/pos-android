package com.foa.pos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foa.pos.fragment.NumPadFragment;
import com.foa.pos.R;
import com.foa.pos.model.Order;
import com.foa.pos.utils.OrderSession;


public class PaymentDialog  extends Dialog implements View.OnClickListener{

    Context context;
    PaymentListener listener;
    boolean isFirstClick =true;
    private TextView tvTotalPay;
    private Button btnPay;
    private Button btnCancel;
    private Order currentOrder;

    public PaymentDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.payment_dialog);
        tvTotalPay = findViewById(R.id.tvTotalPay);

        currentOrder= OrderSession.getInstance();
        tvTotalPay.setText(String.valueOf(currentOrder.getSubTotal()));

        NumPadFragment numPadFragment = new NumPadFragment();
        numPadFragment.setNumPadClickListener(data -> receiveData(data));
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
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnCancel:
                dismiss();
                listener.onFinish(false);
                break;
            case R.id.btnPay:
                dismiss();
                if(listener != null){
                    Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                    OrderSession.clearInstance();
                    dismiss();
                    listener.onFinish(true);
                }
                listener.onFinish(false);
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
