package com.foa.pos.widget;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.Order;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;

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
		
		btnOk = findViewById(R.id.btnOrder);
		btnCancel = findViewById(R.id.btnCancel);
		grandTotal = findViewById(R.id.tvGrandTotal);
		paymentTotal = findViewById(R.id.tvPaymentTottal);
		changeTotal = findViewById(R.id.tvChangeTottal);

		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		loading = new LoadingDialog(context);

		grandTotal.setText(String.valueOf(order.getAmount()));
		paymentTotal.setText(String.valueOf(order.getAmount()));
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
				DatabaseManager.initializeInstance(new DatabaseHelper(context));
				SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
				OrderDataSource DS = new OrderDataSource(db);
				
				DS.updateOrderStatus(order.getId(),1);

				
				result = "1";
				
				Thread.sleep(3000);
			} catch (InterruptedException e) {
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
    

}
