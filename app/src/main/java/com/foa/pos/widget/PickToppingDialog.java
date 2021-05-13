package com.foa.pos.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.foa.pos.R;
import com.foa.pos.model.IResultCallback;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.MenuItemTopping;
import com.foa.pos.model.MenuItemToppingGroup;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.network.response.ToppingGroupData;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoginSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickToppingDialog extends Dialog implements View.OnClickListener{
	private MenuItem menuItem;
	private Context context;
	private Button btnOk;
	private Button btnCancel;
	private ListView toppingListView;
	private LinearLayout toppingsGroupContainer;
	private ProgressBar progressBar;
	private List<MenuItemToppingGroup> menuItemToppingGroup;

	private PickToppingListener listener;
	LoginData loginData;

	public PickToppingDialog(Context context) {
		super(context);
		this.context = context;
	}
	public PickToppingDialog(Context context, MenuItem menuItem) {
		super(context);
		this.context = context;
		this.menuItem = menuItem;
		this.menuItemToppingGroup = new ArrayList<>();
	}

	public void setMenuItemToppingGroup(List<MenuItemToppingGroup> menuItemToppingGroup) {
		this.menuItemToppingGroup = menuItemToppingGroup;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.pick_topping_dialog);
		loginData = LoginSession.getInstance();

		btnOk = findViewById(R.id.btnDoneTopping);
		btnCancel = findViewById(R.id.btnCancel);

		TextView nameTextView = findViewById(R.id.tvProductName);
		TextView priceTextView = findViewById(R.id.tvProductPrice);
		progressBar  = findViewById(R.id.progressBar);
		nameTextView.setText(menuItem.getName());
		priceTextView.setText(Helper.formatMoney(menuItem.getPrice()));
		toppingsGroupContainer  = findViewById(R.id.toppingsGroupContainer);


		//toppingsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> btnOk.setEnabled(true));

		btnOk.setEnabled(true);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	private void addRadioButtons(List<MenuItemToppingGroup> menuItemToppingGroup) {
		if (menuItemToppingGroup.size()==0) {

			return;
		}
		toppingsGroupContainer.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < menuItemToppingGroup.size(); i++) {
			MenuItemToppingGroup currentMenuItemToppingGroup = menuItemToppingGroup.get(i);
			LinearLayout groupContainer = new LinearLayout(context);
			groupContainer.setId(View.generateViewId());
			groupContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			groupContainer.setOrientation(LinearLayout.VERTICAL);

			toppingsGroupContainer.addView(groupContainer);

			TextView groupTitle = new TextView(context);
			groupTitle.setId(View.generateViewId());
			LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0,10,0,5);
			groupTitle.setLayoutParams(params);
			groupTitle.setBackgroundResource(R.color.line_gray);
			groupTitle.setText(currentMenuItemToppingGroup.getName());
			groupTitle.setPadding(5,5,5,5);

			RadioGroup toppingItemsRadioGroup = new RadioGroup(context);
			toppingItemsRadioGroup.setId(View.generateViewId());
			toppingItemsRadioGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			toppingItemsRadioGroup.setOrientation(LinearLayout.VERTICAL);

			groupContainer.addView(groupTitle);
			groupContainer.addView(toppingItemsRadioGroup);

			for (int j = 0; j < currentMenuItemToppingGroup.getToppingItems().size(); j++) {
				RadioButton rdbtn = new RadioButton(context);
				MenuItemTopping currentMenuItemTopping = currentMenuItemToppingGroup.getToppingItems().get(j);
				rdbtn.setId(View.generateViewId());
				rdbtn.setText(currentMenuItemTopping.getName() + "  -  "+ currentMenuItemTopping.getPrice() +"đ");
				rdbtn.setPadding(20, 10, 20, 10);
				rdbtn.setGravity(Gravity.CENTER);
				toppingItemsRadioGroup.addView(rdbtn);
			}
			toppingsGroupContainer.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
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
