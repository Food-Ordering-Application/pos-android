package com.foa.pos.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.adapter.ToppingListAdapter;
import com.foa.pos.entity.Order;
import com.foa.pos.entity.Product;
import com.foa.pos.entity.Topping;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.utils.Helper;
import com.zj.btsdk.BluetoothService;

public class PickToppingDialog extends Dialog implements View.OnClickListener{
	private Product product;
	private Context context;
	private Button btnOk;
	private Button btnCancel;
	private ListView toppingListView;

	private PickToppingListener listener;

	public PickToppingDialog(Context context) {
		super(context);
		this.context = context;
	}
	public PickToppingDialog(Context context, Product product) {
		super(context);
		this.context = context;
		this.product = product;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.pick_topping_dialog);
		
		btnOk = findViewById(R.id.btnDoneTopping);
		btnCancel = findViewById(R.id.btnCancel);

		TextView t1 = findViewById(R.id.tvCartProductName);
		TextView t2 = findViewById(R.id.tvProductPrice);
		toppingListView = findViewById(R.id.toppingListView);

		ToppingListAdapter adapter = new ToppingListAdapter((Activity) context, Topping.getSampleList());
		toppingListView.setAdapter(adapter);

		toppingListView.setOnItemClickListener((parent, view, position, id) -> {
			view.setSelected(true);
			btnOk.setEnabled(true);
		});

		btnOk.setEnabled(false);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		btnOk.setTypeface(Helper.OpenSansSemibold);
		btnCancel.setTypeface(Helper.OpenSansSemibold);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
		case R.id.btnDoneTopping:
			dismiss();
			if(listener != null)
				listener.onFinish(1);
			break;	
		default:
			break;
		}
	}

	public void setPickToppingistener(PickToppingListener listener)
    {
    	this.listener = listener;
    }

    public interface PickToppingListener {
        void onFinish(int result);
    }
    

}
