package com.foa.smartpos.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.foa.smartpos.R;
import com.foa.smartpos.adapter.OrderDetailListAdapter;
import com.foa.smartpos.api.OrderService;
import com.foa.smartpos.catching.DetailDeliveryOrderCatching;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.utils.Helper;

public class OrderDetailDialog extends Dialog {

	private Context context;
	private Button cancelButton;
	private TextView orderIdTextView;
	private TextView subTotalTextView;
	private TextView grandTotalTextView;
	private TextView shippingFee;
	private ListView orderItemsListView;
	private LinearLayout progressLoading;
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
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.CENTER);
		setContentView(R.layout.dialog_order_detail);
		init();
		loadData();

		progressLoading.setVisibility(View.VISIBLE);
		if (DetailDeliveryOrderCatching.getOrderCatching(order)==null){
			OrderService.getOrderById(order.getId(), (success, data) -> {
				DetailDeliveryOrderCatching.addDeliveryCatching(data);
				OrderDetailListAdapter detailAdapter = new OrderDetailListAdapter((Activity) context,data.getOrderItems());
				orderItemsListView.setAdapter(detailAdapter);
				order = data;
				loadData();
				progressLoading.setVisibility(View.GONE);
			});
		}
		progressLoading.setVisibility(View.GONE);
	}


	private void init(){
		progressLoading = findViewById(R.id.progressLoading);
		orderIdTextView = findViewById(R.id.orderId);
		subTotalTextView = findViewById(R.id.qtyOrderItemTextView);
		grandTotalTextView= findViewById(R.id.tvGrandTotal);
		orderItemsListView = findViewById(R.id.listOrderDetails);
	}

	private void loadData(){
		shippingFee = findViewById(R.id.shippingFee);
		orderIdTextView.setText(order.getId());
		subTotalTextView.setText(Helper.formatMoney(order.getSubTotal()));
		grandTotalTextView.setText(Helper.formatMoney(order.getGrandTotal()));
		if (order.getDelivery()!=null){
			shippingFee.setText(Helper.formatMoney(order.getDelivery().getShippingFee()));
		}else{
			shippingFee.setText(Helper.formatMoney(0));
		}
	}


}
