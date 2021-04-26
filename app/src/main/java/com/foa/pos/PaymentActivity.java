package com.foa.pos;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.foa.pos.adapter.CartListAdapter;
import com.foa.pos.model.Order;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ListView;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity implements NumPadFragment.OnNumpadClick {

    private String orderId;
    private OrderDataSource DS;
    private Order currentOrder;
    private CartListAdapter cartAdapter;
    private ListView cartList;
    private TextView txtAmount;
    private TextView txtGrantTotal;
    private TextView tvTotalPay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                orderId= null;
            } else {
                orderId= extras.getString(Constants.PUT_ORDER_ID);
            }
        } else {
            orderId= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        DatabaseManager.initializeInstance(new DatabaseHelper(this));
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        DS = new OrderDataSource(db);
        currentOrder = DS.getOrderById(orderId);
        txtGrantTotal.setText(String.valueOf(currentOrder.getAmount()));
        txtAmount.setText(String.valueOf(currentOrder.getAmount()));
        tvTotalPay.setText(String.valueOf(currentOrder.getAmount()));

        cartAdapter = new CartListAdapter(this);
        cartAdapter.setIsPayment(true);
        cartAdapter.set(currentOrder.getOrderItems());
        cartList.setAdapter(cartAdapter);


    }

    private void init(){
        txtAmount = findViewById(R.id.tvAmount);
        txtGrantTotal = findViewById(R.id.tvGrandTotal);
        cartList  = findViewById(R.id.listOrderDetails);
        tvTotalPay = findViewById(R.id.tvTotalPay);
    }

    boolean isFirstClick =true;

    @SuppressLint("SetTextI18n")
    @Override
    public void onReceiveData(String data) {
        String totalPay = tvTotalPay.getText().toString();
        switch (data){
            case "B":
                tvTotalPay.setText( totalPay.substring(0,totalPay.length()-2)
                );
                break;
            case "C":
                tvTotalPay.setText(String.valueOf(0));
                break;
            default:
                if(isFirstClick){
                    tvTotalPay.setText(data);
                    isFirstClick = false;
                }else{
                    tvTotalPay.setText( tvTotalPay.getText()+data);
                }


        }
    }
}