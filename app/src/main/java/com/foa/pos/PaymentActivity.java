package com.foa.pos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.foa.pos.adapter.CartListAdapter;
import com.foa.pos.model.Order;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.OrderSession;
import com.foa.pos.widget.CustomConfirm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import okio.Timeout;

public class PaymentActivity extends AppCompatActivity implements NumPadFragment.OnNumpadClick {

    private Context context;
    private String orderId;
    private OrderDataSource DS;
    private Order currentOrder;
    private CartListAdapter cartAdapter;
    private ListView cartList;
    private TextView txtAmount;
    private TextView txtGrantTotal;
    private TextView tvTotalPay;
    private Button paymentCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        init();

        currentOrder= OrderSession.getInstance();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);


        txtGrantTotal.setText(String.valueOf(currentOrder.getSubTotal()));
        txtAmount.setText(String.valueOf(currentOrder.getSubTotal()));
        tvTotalPay.setText(String.valueOf(currentOrder.getSubTotal()));

        cartAdapter = new CartListAdapter(this);
        cartAdapter.setIsPayment(true);
        cartAdapter.set(currentOrder.getOrderItems());
        cartList.setAdapter(cartAdapter);

        paymentCash.setOnClickListener(paymentOnClick);

    }

    private void init(){
        txtAmount = findViewById(R.id.tvAmount);
        txtGrantTotal = findViewById(R.id.tvGrandTotal);
        cartList  = findViewById(R.id.listOrderDetails);
        tvTotalPay = findViewById(R.id.tvTotalPay);
        paymentCash = findViewById(R.id.paymentCash);
    }

    boolean isFirstClick =true;

    @SuppressLint("SetTextI18n")
    @Override
    public void onReceiveData(String data) {
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

    private OnClickListener paymentOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

            CustomConfirm con = new CustomConfirm(context, currentOrder);
            con.setConfirmListener(result -> {
                Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                OrderSession.clearInstance();
                new CountDownTimer(2000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                    }
                };
            });
            con.show();
        }
    };
}