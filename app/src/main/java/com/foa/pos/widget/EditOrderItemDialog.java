package com.foa.pos.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.adapter.ToppingListAdapter;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.Topping;

public class EditOrderItemDialog extends Dialog implements View.OnClickListener{
	private OrderItem orderItem;
	private Context context;
	private Button btnOk;
	private Button btnCancel;
	private Switch outOfProductSwitch;
	private boolean preIsOutOfSod;

	private OutOfProductListener listener;

	public EditOrderItemDialog(Context context) {
		super(context);
		this.context = context;
	}
	public EditOrderItemDialog(Context context, OrderItem orderItem) {
		super(context);
		this.context = context;
		this.orderItem = orderItem;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.edit_order_item_dialog);

		btnOk = findViewById(R.id.btnDoneTopping);
		btnCancel = findViewById(R.id.btnCancel);

		TextView t1 = findViewById(R.id.tvCartProductName);
		TextView t2 = findViewById(R.id.tvProductPrice);
		outOfProductSwitch = findViewById(R.id.OutOfProductSwitch);

		preIsOutOfSod = orderItem.isOutSlod();
		btnOk.setEnabled(false);
		outOfProductSwitch.setChecked(preIsOutOfSod);
		outOfProductSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			btnOk.setEnabled(isChecked != preIsOutOfSod);
		});

		btnOk.setEnabled(false);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
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
				listener.onFinish(outOfProductSwitch.isChecked());
			break;
		default:
			break;
		}
	}

	public void setOutOfProductListener(OutOfProductListener listener)
    {
    	this.listener = listener;
    }

    public interface OutOfProductListener {
        void onFinish(boolean result);
    }


}
