package com.foa.pos.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.MenuItemTopping;

import java.util.List;

public class PickToppingDialog extends Dialog implements View.OnClickListener{
	private MenuItem product;
	private Context context;
	private Button btnOk;
	private Button btnCancel;
	private ListView toppingListView;
	private RadioGroup toppingsRadioGroup;

	private PickToppingListener listener;

	public PickToppingDialog(Context context) {
		super(context);
		this.context = context;
	}
	public PickToppingDialog(Context context, MenuItem product) {
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

		TextView nameTextView = findViewById(R.id.tvCartProductName);
		TextView priceTextView = findViewById(R.id.tvProductPrice);
		toppingsRadioGroup  = findViewById(R.id.toppingsRadioGroup);
		addRadioButtons(toppingsRadioGroup, MenuItemTopping.getSampleList());
		toppingsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> btnOk.setEnabled(true));

		btnOk.setEnabled(false);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	private void addRadioButtons(RadioGroup radioGroup, List<MenuItemTopping> toppingList) {
		radioGroup.setOrientation(LinearLayout.VERTICAL);

		for (int i = 0; i < toppingList.size(); i++) {
			RadioButton rdbtn = new RadioButton(context);
			MenuItemTopping topping = toppingList.get(i);
			rdbtn.setId(View.generateViewId());
			rdbtn.setText(topping.getName() + " ("+ topping.getPrice() +") ");
			rdbtn.setPadding(50, 30, 50, 30);
			rdbtn.setGravity(Gravity.CENTER);

			radioGroup.addView(rdbtn);
		}
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
