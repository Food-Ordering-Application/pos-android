package com.foa.smartpos.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.foa.smartpos.R;
import com.foa.smartpos.adapter.OrderDetailListAdapter;
import com.foa.smartpos.model.Order;

public class OrderDetailDialog extends Dialog {

	private Context context;
	private Button cancelButton;
	private TextView orderIdTextView;
	private TextView subTotalTextView;
	private TextView grandTotalTextView;
	private ListView orderItemsListView;
	private Order order;

	public OrderDetailDialog(Context context, Order order) {
		super(context);
		this.context = context;
		this.order = order;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_order_detail);
		init();
	}

	private void init(){
		orderIdTextView = findViewById(R.id.orderId);
		subTotalTextView = findViewById(R.id.tvGrandTotal);
		grandTotalTextView= findViewById(R.id.tvGrandTotal);
		orderItemsListView = findViewById(R.id.cartListView);
		OrderDetailListAdapter detailAdapter = new OrderDetailListAdapter((Activity) context,order.getOrderItems());
		orderItemsListView.setAdapter(detailAdapter);
		cancelButton = findViewById(R.id.btnCancel);
		cancelButton.setOnClickListener(view -> {
			dismiss();
		});
	}

}
