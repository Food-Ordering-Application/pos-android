package com.foa.pos.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.OrderItemTopping;
import com.foa.pos.dialog.LoadingDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class Helper
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public static SimpleDateFormat dateTimeformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public static DecimalFormat decimalformat = new DecimalFormat("#.###");
	public static void initialize(Context base)
	{
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		decimalformat.setDecimalFormatSymbols(otherSymbols);
		
		instance = new ContextWrapper(base);
		pref = instance.getSharedPreferences("com.foa.pos", Context.MODE_PRIVATE);
	}
	public static void write(String key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.apply();
	}
	
	public static String read(String key)
	{
		return Helper.read(key, null);
	}
	
	public static String read(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}
	
	public static void clear()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.apply();
	}
	
	public static Context getContext()
	{
		return instance.getBaseContext();
	}
	
	public static String getOrderID()
	{
		return UUID.randomUUID().toString();
	}
	
	public static String getOrderItemID(int i)
	{
		return UUID.randomUUID().toString();
	}

	public static String getOrderToppingId(int i)
	{
		return UUID.randomUUID().toString();
	}

	public static int getDisplayWidth()
	{
		 WindowManager wm = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
		 Display display = wm.getDefaultDisplay();
		 final int version = android.os.Build.VERSION.SDK_INT;
		 
		 if (version >= 13)
		 {
		     Point size = new Point();
		     display.getSize(size);
		     return size.x;
		 }
		 else
		 {
		     
		     return  display.getWidth();
		 }
	}

	public static String formatMoney(long monney){
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
		return currencyVN.format(monney)+ " "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL);
	}


	public static Date plusMinutes(Date date, int minute) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
	public static Date plusDays(Date date, int day) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static void enableSplitLayout(LinearLayout leftLayout, RelativeLayout rightLayout, GridView theGridView) {
		final int width = Helper.getDisplayWidth();
		ViewGroup.LayoutParams param = leftLayout.getLayoutParams();
		param.width = (width / 3)*2;
		leftLayout.setLayoutParams(param);
		ViewGroup.LayoutParams param2 = rightLayout.getLayoutParams();
		param2.width = (width / 3);
		rightLayout.setLayoutParams(param2);
		rightLayout.setVisibility(View.VISIBLE);
		theGridView.setNumColumns(3);
	}

	public static void disableSplitLayout(LinearLayout leftLayout, RelativeLayout rightLayout, GridView theGridView) {
		ViewGroup.LayoutParams param = leftLayout.getLayoutParams();
		param.width = ViewGroup.LayoutParams.MATCH_PARENT;
		leftLayout.setLayoutParams(param);
		rightLayout.setVisibility(View.GONE);
		theGridView.setNumColumns(5);
	}

	public static boolean checkHasSelectedItem(List<Order> orders,Order item){
		for (int i = 0; i < orders.size(); i++) {
			if(item.getId()!= orders.get(i).getId() && orders.get(i).isSelected()){
				orders.get(i).setSelected(false);
				return true; //has item selected
			}
		}
		return false;// no item selected;
	}

	public static void showFailNotification(Context context,LoadingDialog loading,LinearLayout wrapper,String message){
		loading.dismiss();
		YoYo.with(Techniques.Shake).duration(700).playOn(wrapper);
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static OrderItem createOrderItem(MenuItem product, int pos, String orderId){
		OrderItem orderItem = new OrderItem();
		orderItem.setId(Helper.getOrderItemID(pos));
		orderItem.setMenuItemId(product.getId());
		orderItem.setMenuItemName(product.getName());
		orderItem.setOrderId(orderId);
		orderItem.setQuantity(1);
		orderItem.setPrice(product.getPrice());
		//orderItem.setOrderItemToppings(new ArrayList<>());
		return orderItem;
	}
	public static OrderItem createSendOrderItem(MenuItem product, List<OrderItemTopping> orderItemToppingList){
		OrderItem orderItem = new OrderItem();
		orderItem.setMenuItemId(product.getId());
		orderItem.setMenuItemName(product.getName());
		orderItem.setQuantity(1);
		orderItem.setPrice(product.getPrice());
		orderItem.setOrderItemToppings(orderItemToppingList);
		return orderItem;
	}
}

