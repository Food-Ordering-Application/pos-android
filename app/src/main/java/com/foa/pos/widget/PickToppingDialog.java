package com.foa.pos.widget;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.foa.pos.model.ToppingItem;
import com.foa.pos.model.ToppingGroup;
import com.foa.pos.model.OrderItemTopping;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.MenuItemToppingDataSource;
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
	private List<RadioGroup> menuItemToppingGroupRadioButton;

	private PickToppingListener listener;
	private LoginData loginData;
	private boolean enableOkButton;
	List<ToppingGroup> toppingGroup;
	List<OrderItemTopping> orderItemToppings;

	MenuItemToppingDataSource menuItemToppingDS;

	public PickToppingDialog(Context context, MenuItem menuItem) {
		super(context);
		this.context = context;
		this.menuItem = menuItem;
		this.menuItemToppingGroupRadioButton = new ArrayList<>();
		this.orderItemToppings = new ArrayList<>();
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
		btnOk.setEnabled(false);

		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
		menuItemToppingDS = new MenuItemToppingDataSource(db);

		addRadioButtons(menuItemToppingDS.getToppingGroupByMenuId(menuItem.getId()));
		progressBar.setVisibility(View.GONE);
		toppingsGroupContainer.setVisibility(View.VISIBLE);
	}

	private void checkEnableOkButton(){

		for (RadioGroup toppingGroup: menuItemToppingGroupRadioButton) {
			toppingGroup.setOnCheckedChangeListener((group, checkedId) -> {
				addToOrderItemToppingList(toppingGroup.getCheckedRadioButtonId());
				boolean isEnable=true;
				orderItemToppings = new ArrayList<>();
				for (RadioGroup item : menuItemToppingGroupRadioButton) {
					if(item.getCheckedRadioButtonId()== -1){
						isEnable = false;
					}else{
						addToOrderItemToppingList(item.getCheckedRadioButtonId());
					}
				}
				btnOk.setEnabled(isEnable);
			});
		}

	}

	private void addToOrderItemToppingList(int radioButtonId){
		for (ToppingGroup toppingGroup : toppingGroup) {
			for (ToppingItem toppingItem : toppingGroup.getToppingItems()) {
				if(toppingItem.getRadioButtonId()==radioButtonId) {
					orderItemToppings.add(toppingItem.createOrderItemTopping());
				}
			}
		}
	}

	private void addRadioButtons(List<ToppingGroup> toppingGroup) {
		if (toppingGroup.size()==0) {

			return;
		}
		toppingsGroupContainer.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < toppingGroup.size(); i++) {
			ToppingGroup currentToppingGroup = toppingGroup.get(i);
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
			groupTitle.setText(currentToppingGroup.getName());
			groupTitle.setPadding(5,5,5,5);

			RadioGroup toppingItemsRadioGroup = new RadioGroup(context);
			menuItemToppingGroupRadioButton.add(toppingItemsRadioGroup);
			toppingItemsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

			});
			toppingItemsRadioGroup.setId(View.generateViewId());
			toppingItemsRadioGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			toppingItemsRadioGroup.setOrientation(LinearLayout.VERTICAL);

			groupContainer.addView(groupTitle);
			groupContainer.addView(toppingItemsRadioGroup);

			for (int j = 0; j < currentToppingGroup.getToppingItems().size(); j++) {
				RadioButton rdbtn = new RadioButton(context);
				ToppingItem currentMenuItemTopping = currentToppingGroup.getToppingItems().get(j);
				rdbtn.setId(View.generateViewId());
				currentMenuItemTopping.setRadioButtonId(rdbtn.getId());
				rdbtn.setText(currentMenuItemTopping.getName() + "  -  "+ currentMenuItemTopping.getPrice() +"đ");
				rdbtn.setPadding(20, 10, 20, 10);
				rdbtn.setGravity(Gravity.CENTER);
				toppingItemsRadioGroup.addView(rdbtn);
			}

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
				listener.onFinish(orderItemToppings);
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
        void onFinish(List<OrderItemTopping> toppingGroups);
    }

//	private void getToppingsByMenuItemId(String menuItemId, IResultCallback resultCallback) {
//		Call<ResponseAdapter<ToppingGroupData>> responseCall = RetrofitClient.getInstance().getAppService()
//				.getToppingsByMenuItemId(menuItemId);
//		responseCall.enqueue(new Callback<ResponseAdapter<ToppingGroupData>>() {
//			@Override
//			public void onResponse(Call<ResponseAdapter<ToppingGroupData>> call, Response<ResponseAdapter<ToppingGroupData>> response) {
//				if (response.errorBody() != null) {
//					try {
//						Log.e("[PickToppingDialog][Api error]", response.errorBody().string());
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if (response.code() == Constants.STATUS_CODE_SUCCESS) {
//					ResponseAdapter<ToppingGroupData> res = response.body();
//					assert res != null;
//					if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
//						toppingGroup = res.getData().getMenuItemToppingGroup();
//						addRadioButtons(toppingGroup);
//						resultCallback.onSuccess(true);
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
//			public void onFailure(Call<ResponseAdapter<ToppingGroupData>> call, Throwable t) {
//				Log.e("Login Error", t.getMessage());
//			}
//		});
//	}
}
