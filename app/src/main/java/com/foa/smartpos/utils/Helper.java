package com.foa.smartpos.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.smartpos.R;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.OrderItemTopping;
import com.foa.smartpos.dialog.LoadingDialog;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.network.response.LoginData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
	public static SimpleDateFormat dateSQLiteFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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

	public static void remove(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
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
	
	public static void clearAll()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.apply();
	}

	static public void setLoginData(LoginData loginData){
		Helper.write(Constants.CASHIER_ID,loginData.getStaff().getId());
		Helper.write(Constants.CASHIER_NAME,loginData.getStaff().getFullName());
		Helper.write(Constants.BEARER_ACCESS_TOKEN,loginData.getBearerAccessToken());
	}

	static public void clearLoginData(){
		Helper.remove(Constants.CASHIER_ID);
		Helper.remove(Constants.CASHIER_NAME);
		Helper.remove(Constants.BEARER_ACCESS_TOKEN);
	}
	
	public static Context getContext()
	{
		return instance.getBaseContext();
	}
	
	public static String getOrderID()
	{
		return UUID.randomUUID().toString();
	}
	
	public static String getOrderItemID()
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
		return currencyVN.format(monney);
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

	public static String downloadImageAndSave(String src, String name) {
		src = "https://picsum.photos/200/300";
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return saveImage(myBitmap,name);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getAppDir()
	{
		File f;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
			f = new File(instance.getExternalFilesDir(null).getAbsolutePath() + File.separator  + instance.getResources().getString(R.string.app_name));


		}else {
			f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator  + instance.getResources().getString(R.string.app_name));

		}
		if(!f.exists())
			f.mkdir();
		return f.getAbsolutePath();
	}

	public static float scaleFactor(int w)
	{
		return 512f / w;
	}

	static public String saveImage(Bitmap bitmapImage, String name)
	{
		String res = null;
		if(bitmapImage != null)
		{
			File dest = new File(Helper.getAppDir(), name);

			Bitmap bitmap = bitmapImage;
			FileOutputStream out;
			try {

				out = new FileOutputStream(dest);
				float scale = Helper.scaleFactor(bitmap.getWidth());
				bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*scale), (int) (bitmap.getHeight()*scale), false);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				res = dest.toString();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return res;

	}

	public static void enableSplitLayout(Context context,LinearLayout leftLayout, RelativeLayout rightLayout, RecyclerView theGridView) {
		final int width = Helper.getDisplayWidth();
		ViewGroup.LayoutParams param = leftLayout.getLayoutParams();
		param.width = (width / 3)*2;
		leftLayout.setLayoutParams(param);
		ViewGroup.LayoutParams param2 = rightLayout.getLayoutParams();
		param2.width = (width / 3);
		rightLayout.setLayoutParams(param2);
		rightLayout.setVisibility(View.VISIBLE);
		theGridView.setLayoutManager(new GridLayoutManager(context,3));
	}

	public static void disableSplitLayout(Context context,LinearLayout leftLayout, RelativeLayout rightLayout, RecyclerView theGridView) {
		ViewGroup.LayoutParams param = leftLayout.getLayoutParams();
		param.width = ViewGroup.LayoutParams.MATCH_PARENT;
		leftLayout.setLayoutParams(param);
		rightLayout.setVisibility(View.GONE);
		theGridView.setLayoutManager(new GridLayoutManager(context,4));
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

	public static OrderItem createOrderItem(MenuItem product,List<OrderItemTopping> orderItemToppings, String orderId){
		OrderItem orderItem = new OrderItem();
		orderItem.setId(Helper.getOrderItemID());
		orderItem.setMenuItemId(product.getId());
		orderItem.setMenuItemName(product.getName());
		orderItem.setStockState(StockState.IN_STOCK);
		orderItem.setOrderId(orderId);
		orderItem.setQuantity(1);
		orderItem.setPrice(product.getPrice());
		for (int i = 0; i < orderItemToppings.size() ; i++) {
			orderItemToppings.get(i).setId(Helper.getOrderToppingId(i));
		}
		orderItem.setOrderItemToppings(orderItemToppings);
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

