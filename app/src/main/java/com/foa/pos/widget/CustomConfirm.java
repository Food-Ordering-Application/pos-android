package com.foa.pos.widget;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.foa.pos.R;
import com.foa.pos.model.Order;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.LoginSession;

public class CustomConfirm extends Dialog implements View.OnClickListener{
	private Order order;
	private Context context;
	private Button btnOk;
	private Button btnCancel;
	TextView grandTotal;
	TextView paymentTotal;
	TextView changeTotal;
	private ConfirmListener listener;
	private LoadingDialog loading;

	private LoginData loginData;

	public CustomConfirm(Context context) {
		super(context);
		this.context = context;
	}
	public CustomConfirm(Context context, Order order) {
		super(context);
		this.context = context;
		this.order = order;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.confirm_dialog);

		loginData = LoginSession.getInstance();
		
		btnOk = findViewById(R.id.btnOrder);
		btnCancel = findViewById(R.id.btnCancel);
		grandTotal = findViewById(R.id.tvGrandTotal);
		paymentTotal = findViewById(R.id.tvPaymentTottal);
		changeTotal = findViewById(R.id.tvChangeTottal);

		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		loading = new LoadingDialog(context);

		grandTotal.setText(String.valueOf(order.getSubTotal()));
		paymentTotal.setText(String.valueOf(order.getSubTotal()));
		changeTotal.setText(String.valueOf(0));

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
		case R.id.btnOrder:
			save();
			break;	
		default:
			break;
		}
	}
	
	private void save()
	{
		SaveAsync s = new SaveAsync();
		s.execute();
	}
	
	public class SaveAsync extends AsyncTask<Void, String, String>
	{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.show();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
		
			String result = "0";
			
			try {
				if (loginData!=null){

				}else{
					SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
					OrderDataSource DS = new OrderDataSource(db);
					DS.updateOrderStatus(order.getId(),1);
				}
				result = "1";

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			loading.dismiss();
			dismiss();
			if(listener != null)
				listener.onFinish(result);
			
		}
	}
	
	public void setConfirmListener(ConfirmListener listener)
    {
    	this.listener = listener;
    }

    public interface ConfirmListener {
        public void onFinish(String result);
    }

//	private void updateCompletedOrder(String orderId, IResultCallback resultCallback) {
//		Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
//				.updateCompletedOrder(orderId);
//		responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
//			@Override
//			public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {
//				if (response.errorBody() != null) {
//					try {
//						Log.e("[OrderFragment][Api error]", response.errorBody().string());
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (response.code() == Constants.STATUS_CODE_SUCCESS) {
//					resultCallback.onSuccess(true);
//					ResponseAdapter<OrderData> res = response.body();
//					assert res != null;
//					if (res.getStatus() == Constants.STATUS_CODE_CREATED) {
//						listener.onChange(res.getData().getOrder());
//					} else {
//						Log.e("[Order fragment]", "Create order fail");
//					}
//				} else {
//					resultCallback.onSuccess(false);
//					Log.e("[Order fragment]", "Create order fail");
//				}
//
//			}
//
//			@Override
//			public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
//				Log.e("Login Error", t.getMessage());
//			}
//		});
//	}

}
