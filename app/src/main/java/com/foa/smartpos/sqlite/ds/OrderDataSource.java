package com.foa.smartpos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.model.OrderItem;
import com.foa.smartpos.model.OrderItemTopping;
import com.foa.smartpos.model.enums.OrderStatus;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.sqlite.DbSchema;
import com.foa.smartpos.utils.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class OrderDataSource {
	private SQLiteDatabase db;
	public OrderDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}

	public ArrayList<Order> getAllOrderWithDate(String startDate, String endDate) {
		return getAllOrder(null,null,null,null, OrderStatus.COMPLETED,startDate,endDate,null);
	}

	public ArrayList<Order> getAllOrderCurrentDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		String strStartDate = Helper.dateSQLiteFormat.format(calendar.getTime());
		calendar.add(Calendar.DATE,1);
		String strEndDate = Helper.dateSQLiteFormat.format(calendar.getTime());
		return getAllOrder(null,null,null,null, OrderStatus.COMPLETED,strStartDate,strEndDate,null);
	}

	public ArrayList<Order> getAllOrderNotSync(Boolean isNotSynced) {
		return getAllOrder(null,null,null,null, OrderStatus.COMPLETED,null,null,isNotSynced);
	}

	public ArrayList<Order> getAllOrder(ArrayList<HashMap<String, String>> filter, String orderby, String limit, String offset, OrderStatus status, String startDate, String endDate, Boolean isNotSynced) {
		ArrayList<String> where = new ArrayList<String>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_ORDER;
		if(isNotSynced != null && !isNotSynced.equals(""))
				where.add(DbSchema.COL_ORDER_SYNCED_AT +" IS NULL");
		if(startDate != null && !startDate.equals(""))
			where.add( DbSchema.COL_ORDER_CREATED_AT +" >= DATE('" + startDate + "')");
		if(endDate != null && !endDate.equals(""))
			where.add( DbSchema.COL_ORDER_CREATED_AT +" <= DATE('" + endDate + "')");
		if(where.size() != 0)
			selectQuery += " where " + TextUtils.join(" AND ",where);
		ArrayList<Order> orderList = new ArrayList<Order>();

 		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {

			do {
				if(!c.getString(c.getColumnIndex(DbSchema.COL_ORDER_STATUS)).equals(status.toString())){
					continue;
				}

				Order order = new Order();
				order.setId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ID)));
				order.setNote(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				order.setSubTotal(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_SUBTOTAL)));
				order.setGrandTotal(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_GRAND_TOTAL)));
				order.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_DISCOUNT)));
				order.setCashierId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CASHIER_ID)));
				order.setCashierName(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CASHIER_NAME)));
				order.setRestaurantId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_RESTAURANT_ID)));
				order.setStatus(OrderStatus.valueOf(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_STATUS))));
				order.setSelected(false);
				
				try {  
				    order.setCreatedAt( Helper.dateTimeformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CREATED_AT))));
				    order.setUpdatedAt( Helper.dateTimeformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_AT))));
					order.setSyncedAt(Helper.dateTimeformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYNCED_AT))));

				} catch (Exception e) {
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_MENU_ITEM_NAME
						+"  FROM " + DbSchema.TBL_ORDER_ITEM + " o " +
											" LEFT JOIN " +  DbSchema.TBL_MENU_ITEM +  " p ON p." +  DbSchema.COL_MENU_ITEM_ID + " = o." + DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID +
											" WHERE " +DbSchema.COL_ORDER_ITEM_ORDER_ID + " = '"+order.getId()+"'";
				Cursor cOrderItem = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderItem> orderItemList = new ArrayList<>();
				if (cOrderItem.moveToFirst()) {
					do {
						OrderItem orderItem = new OrderItem();
						orderItem.setId(cOrderItem.getString(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_ID)));
						orderItem.setMenuItemName(cOrderItem.getString(cOrderItem.getColumnIndex(DbSchema.COL_MENU_ITEM_NAME)));
						orderItem.setOrderId(cOrderItem.getString(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_ORDER_ID)));
						orderItem.setMenuItemId(cOrderItem.getString(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID)));
						orderItem.setQuantity(cOrderItem.getInt(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_QTY)));
						orderItem.setDiscount(cOrderItem.getLong(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_DISCOUNT)));
						orderItem.setSubTotal(cOrderItem.getLong(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_SUBTOTAL)));
						orderItem.setPrice(cOrderItem.getLong(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_PRICE)));
						orderItem.setStockState(StockState.valueOf(cOrderItem.getString(cOrderItem.getColumnIndex(DbSchema.COL_ORDER_ITEM_STATE))));
						orderItemList.add(orderItem);


						String selectQueryTopping = " SELECT  o.*,p." + DbSchema.COL_MENU_ITEM_NAME
								+ "  FROM " + DbSchema.TBL_ORDER_ITEM_TOPPING + " o " +
								" LEFT JOIN " + DbSchema.TBL_TOPPING_ITEM + " p ON p." + DbSchema.COL_TOPPING_ITEM_ID + " = o." + DbSchema.COL_ORDER_ITEM_TOPPING_TOPPING_ID +
								" WHERE " + DbSchema.COL_ORDER_ITEM_TOPPING_ORDER_ITEM_ID + " = '" + orderItem.getId() + "'";
						Cursor cTopping = db.rawQuery(selectQueryTopping, null);
						ArrayList<OrderItemTopping> orderItemToppingList = new ArrayList<>();
						if (cTopping.moveToFirst()) {
							do {
								OrderItemTopping orderItemTopping = new OrderItemTopping();
								orderItemTopping.setId(cTopping.getString(cTopping.getColumnIndex(DbSchema.COL_ORDER_ITEM_TOPPING_ID)));
								orderItemTopping.setName(cTopping.getString(cTopping.getColumnIndex(DbSchema.COL_MENU_ITEM_NAME)));
								orderItemTopping.setMenuItemToppingId(cTopping.getString(cTopping.getColumnIndex(DbSchema.COL_ORDER_ITEM_TOPPING_TOPPING_ID)));
								orderItemTopping.setOrderItemId(cTopping.getString(cTopping.getColumnIndex(DbSchema.COL_ORDER_ITEM_TOPPING_ORDER_ITEM_ID)));
								orderItemTopping.setQuantity(cTopping.getInt(cTopping.getColumnIndex(DbSchema.COL_ORDER_ITEM_TOPPING_QTY)));
								orderItemTopping.setPrice(cTopping.getLong(cTopping.getColumnIndex(DbSchema.COL_ORDER_ITEM_TOPPING_PRICE)));
								orderItemTopping.setState(StockState.valueOf(cTopping.getString(cTopping.getColumnIndex(DbSchema.COL_ORDER_ITEM_TOPPING_STATE))));
								orderItemToppingList.add(orderItemTopping);
							} while (cTopping.moveToNext()) ;
						}
						orderItem.setOrderItemToppings(orderItemToppingList);
						order.addOrderItem(orderItem);

					} while (cOrderItem.moveToNext());
				}
				order.setOrderItems(orderItemList);
				orderList.add(order);
			} while (c.moveToNext());
		}
	
		return orderList;
	}
	
	public long insertOrder(Order item){
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_ORDER_ID, item.getId());
		values.put(DbSchema.COL_ORDER_DESCRIPTION, item.getNote());
		values.put(DbSchema.COL_ORDER_SUBTOTAL, item.getSubTotal());
		values.put(DbSchema.COL_ORDER_GRAND_TOTAL, item.getSubTotal());
		values.put(DbSchema.COL_ORDER_DISCOUNT,item.getDiscount());
		values.put(DbSchema.COL_ORDER_CASHIER_ID, item.getCashierId());
		values.put(DbSchema.COL_ORDER_CASHIER_NAME, item.getCashierName());
		values.put(DbSchema.COL_ORDER_RESTAURANT_ID, item.getRestaurantId());
		values.put(DbSchema.COL_ORDER_STATUS, item.getStatus().toString());
		values.put(DbSchema.COL_ORDER_CREATED_AT, Helper.dateTimeformat.format(new Date()));
		values.put(DbSchema.COL_ORDER_UPDATED_AT, Helper.dateTimeformat.format(new Date()));
		long success = db.insert(DbSchema.TBL_ORDER, null, values);
		
		for (OrderItem orderItem : item.getOrderItems()) {
			ContentValues valuesDetails = new ContentValues();
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_ID, orderItem.getId());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_ORDER_ID, orderItem.getOrderId());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID, orderItem.getMenuItemId());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_PRICE, orderItem.getPrice());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_QTY,orderItem.getQuantity());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_DISCOUNT, orderItem.getDiscount());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_STATE,orderItem.getStockState().toString());
			long orderItemSucess = db.insert(DbSchema.TBL_ORDER_ITEM, null, valuesDetails);

		}
		
		return 1;
	}

	public void updateSyncAt(String orderId){
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_SYNCED_AT, Helper.dateTimeformat.format(new Date()));

		db.update(DbSchema.TBL_ORDER, values, DbSchema.COL_ORDER_ID +"= '"+orderId+"' ", null);
	}

	public long updateOrderStatus(String orderId, OrderStatus status)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_STATUS, status.toString());

		return db.update(DbSchema.TBL_ORDER, values, DbSchema.COL_ORDER_ID +"= '"+orderId+"' ", null);
	}

	public void deleteAllOrderItem(List<OrderItem> orderItems){
		 db.delete(DbSchema.TBL_ORDER_ITEM, DbSchema.COL_ORDER_ITEM_ORDER_ID +"= '"+orderItems.get(0).getOrderId()+"' ", null);
		 orderItems.forEach(item->{
			 db.delete(DbSchema.TBL_ORDER_ITEM_TOPPING, DbSchema.COL_ORDER_ITEM_TOPPING_ORDER_ITEM_ID +"= '"+item.getId()+"' ", null);
		 });
	}

	//------------------- ORDER ITEM -----------------//

	public long insertOrderItem(OrderItem orderItem){
		ContentValues valuesDetails = new ContentValues();
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_ID, orderItem.getId());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_ORDER_ID, orderItem.getOrderId());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID, orderItem.getMenuItemId());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_PRICE, orderItem.getPrice());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_QTY,orderItem.getQuantity());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_DISCOUNT, orderItem.getDiscount());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_SUBTOTAL, orderItem.getSubTotal());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_STATE,StockState.IN_STOCK.toString());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_CREATED_AT,Calendar.getInstance().getTime().toString());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_UPDATED_AT,Calendar.getInstance().getTime().toString());
		db.insert(DbSchema.TBL_ORDER_ITEM, null, valuesDetails);

		long success = -1;
		for (OrderItemTopping item :orderItem.getOrderItemToppings()) {
			ContentValues values = new ContentValues();
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_ID, item.getId());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_TOPPING_ID, item.getMenuItemToppingId());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_ORDER_ITEM_ID, orderItem.getId());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_PRICE, item.getPrice());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_QTY, item.getQuantity());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_STATE,item.getState().toString());

			success =  db.insert(DbSchema.TBL_ORDER_ITEM_TOPPING, null, values);
			if (success==-1) break;
		}
		return success;
	}

	public long updateStockStateOrderItem(String orderItemId, StockState stockState)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_ITEM_STATE, stockState.toString());

		long success =  db.update(DbSchema.TBL_ORDER_ITEM, values, DbSchema.COL_ORDER_ITEM_ID +"= '"+orderItemId+"' ", null);
		return success;
	}

	public void deleteOrderItem(String orderItemId, String orderId, long orderSubToal)
	{
		db.delete(DbSchema.TBL_ORDER_ITEM, DbSchema.COL_ORDER_ITEM_ID +"= '"+orderItemId+"' ", null);

		ContentValues orderValues = new ContentValues();
		orderValues.put(DbSchema.COL_ORDER_SUBTOTAL, orderSubToal);
		orderValues.put(DbSchema.COL_ORDER_GRAND_TOTAL, orderSubToal);
		db.update(DbSchema.TBL_ORDER, orderValues, DbSchema.COL_ORDER_ID +"= '"+orderId+"' ", null);
	}

	public void updateOrderItemQuantity(String orderItemId, int quantity, long orderItemSubTotal, String orderId, long orderSubToal){
		//Update order item qty and sub total
		ContentValues orderItemValues = new ContentValues();
		orderItemValues.put(DbSchema.COL_ORDER_ITEM_QTY, quantity);
		orderItemValues.put(DbSchema.COL_ORDER_ITEM_SUBTOTAL, orderItemSubTotal);
		long orderItemSuccess = db.update(DbSchema.TBL_ORDER_ITEM, orderItemValues, DbSchema.COL_ORDER_ITEM_ID +"= '"+orderItemId+"' ", null);

		//update order sub total and grand total
		ContentValues orderValues = new ContentValues();
		orderValues.put(DbSchema.COL_ORDER_SUBTOTAL, orderSubToal);
		orderValues.put(DbSchema.COL_ORDER_GRAND_TOTAL, orderSubToal);
		long orderSuccess = db.update(DbSchema.TBL_ORDER, orderValues, DbSchema.COL_ORDER_ID +"= '"+orderId+"' ", null);
	}

	public long updateSumaryOrderInfo(String orderId, long subTotal)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_SUBTOTAL, subTotal) ;
		values.put(DbSchema.COL_ORDER_GRAND_TOTAL, subTotal) ;

		return db.update(DbSchema.TBL_ORDER, values, DbSchema.COL_ORDER_ID +"= '"+orderId+"' ", null);
	}
	
	public int delete(String code)
	{
		db.delete(DbSchema.TBL_ORDER_ITEM, DbSchema.COL_ORDER_ITEM_ORDER_ID + "= '" + code + "'", null);
		db.delete(DbSchema.TBL_ORDER, DbSchema.COL_ORDER_ID + "= '" + code + "'", null);
		return 1;
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_ORDER  +
						      " Where lower(" +DbSchema.COL_ORDER_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
