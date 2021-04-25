package com.foa.pos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.pos.R;
import com.foa.pos.adapter.OrderDetailListAdapter;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.sqlite.DatabaseHelper;
import com.foa.pos.sqlite.DatabaseManager;
import com.foa.pos.sqlite.ds.OrderDataSource;
import com.foa.pos.sqlite.ds.ProductDataSource;
import com.foa.pos.widget.EditOrderItemDialog;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public final class Helper
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;
	public static String SERVER_URL = "http://10.0.2.2:8000";
	
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dateformatID = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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
		editor.commit();
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
		editor.commit();
	}
	
	public static void clear(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static Context getContext()
	{
		return instance.getBaseContext();
	}
	
	public static String getUserID()
	{
		return getMD5("U" + System.currentTimeMillis());
	}
	
	public static String getOrderID()
	{
		return getMD5("O" + System.currentTimeMillis());
	}
	
	public static String getOrderDetailID(int i)
	{
		if(i != 0)
			return getMD5("DO" + System.currentTimeMillis());
		else
			return getMD5("DO" + System.currentTimeMillis()+i);
	}
	public static String getCashierID()
	{
		return getMD5("K" + System.currentTimeMillis());
	}
	
	public static String getCategoryID()
	{
		return getMD5("C" + System.currentTimeMillis());
	}
	
	public static String getProductID()
	{
		return getMD5("P" + System.currentTimeMillis());
	}
	
	public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	 
	public static String getAppDir()
	{
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator  + instance.getResources().getString(R.string.app_name));
		if(!f.exists())
			f.mkdir();
		
		return f.getAbsolutePath();
	}
	
	public static float scaleFactor(int w)
    {
       return 512f / w;
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
		return decimalformat.format(monney)+ " "+Helper.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL);
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

	public static void loadOrderDetail(Order order, RelativeLayout detailLayout, Context context){
		((TextView)detailLayout.findViewById(R.id.tvTotal)).setText(String.valueOf(order.getGrandTotal()));
		((TextView)detailLayout.findViewById(R.id.tvTotalPay)).setText(String.valueOf(order.getGrandTotal()));
		((TextView)detailLayout.findViewById(R.id.tvOderId)).setText(String.valueOf(order.getId().substring(0,6)));
		((TextView)detailLayout.findViewById(R.id.tvReceiveMoney)).setText(String.valueOf(order.getGrandTotal()));
		if((TextView)detailLayout.findViewById(R.id.tvChange)!=null){
			((TextView)detailLayout.findViewById(R.id.tvChange)).setText(String.valueOf(0));
		}
		OrderDetailListAdapter adapter = new OrderDetailListAdapter((Activity) context);
		adapter.set(order.getOrderItems());
		adapter.notifyDataSetChanged();
		ListView detailsListView = detailLayout.findViewById(R.id.listOrderDetails);
		detailsListView.setAdapter(adapter);
		detailsListView.setOnItemClickListener((parent, view, position, id) -> {
			Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
			OrderItem item =(OrderItem) detailsListView.getItemAtPosition(position);
			EditOrderItemDialog dialog = new EditOrderItemDialog(context,item);
			dialog.setOutOfProductListener(result -> {
				if (result){
					DatabaseManager.initializeInstance(new DatabaseHelper(context));
					SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
					OrderDataSource DS = new OrderDataSource(db);
					DS.updateOutSoldOrderItem(item.getId(),result);
					((OrderDetailListAdapter) detailsListView.getAdapter())
							.updateIsOutSold(item.getId());

				}
			});
			dialog.show();
		});
	}

	public static void clearSelectedItem(List<Order> orders){
		for (int i = 0; i < orders.size(); i++) {
			if(orders.get(i).isSelected()){
				orders.get(i).setSelected(false);
				return;
			}
		}
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
}

