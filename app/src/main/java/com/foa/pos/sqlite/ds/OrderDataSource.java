package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.enums.StockState;
import com.foa.pos.sqlite.DbSchema;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDataSource {
	private SQLiteDatabase db;
	public OrderDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public Order getOrderById(String id) {
		 
		Order order = new Order();
		String selectQuery = 	" SELECT *  FROM " + DbSchema.TBL_ORDER + " Where " +DbSchema.COL_ORDER + " = '"+id+"'";
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				order.setId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER)));
				order.setNote(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				order.setSubTotal(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
				order.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				order.setRestaurantId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
				order.setCustomerId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CASHIER_ID)));
				
				try {  
				    order.setCreatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
				    order.setUpdatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
				} catch (Exception e) {
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_MENU_ITEM_NAME +",c."+DbSchema.COL_MENU_GROUP_NAME +"  FROM " + DbSchema.TBL_ORDER_ITEM + " o " +
											" LEFT JOIN " +  DbSchema.TBL_MENU_ITEM +  " p ON p." +  DbSchema.COL_MENU_ITEM_ID + " = o." + DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID +
											" LEFT JOIN " +  DbSchema.TBL_MENU_GROUP +  " c ON c." +  DbSchema.COL_MENU_GROUP_ID + " = p." + DbSchema.COL_MENU_GROUP_ID +
											" WHERE " +DbSchema.COL_ORDER_ITEM_ORDER_ID + " = '"+id+"'";
				Cursor cDetail = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderItem> details = new ArrayList<OrderItem>();
				if (cDetail.moveToFirst()) {
					do {
						
						OrderItem orderItem = new OrderItem();
						orderItem.setId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_ID)));
						orderItem.setMenuItemName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_MENU_ITEM_NAME)));
						orderItem.setOrderId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_ORDER_ID)));
						orderItem.setMenuItemId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID)));
						orderItem.setQuantity(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_QTY)));
						orderItem.setDiscount(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_DISCOUNT)));
						orderItem.setPrice(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_PRICE)));
						orderItem.setStockState(StockState.valueOf(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_STATE))));
						details.add(orderItem);
					} while (cDetail.moveToNext());
				}
				
				 order.setOrderItems(details);
			
			} while (c.moveToNext());
		}
		return order;
	}

	public ArrayList<Order> getAllOrder() {
		return getAllOrder(null,null,null,null, 1);
	}

	public ArrayList<Order> getAllOrder(ArrayList<HashMap<String, String>> filter, String orderby, String limit, String offset, int status) {
		 
		ArrayList<Order> items = new ArrayList<Order>();
		//Cursor c = db.rawQuery("DROP TABLE "+DbSchema.TBL_ORDER , null);
		String selectQuery = " SELECT *  FROM " + DbSchema.TBL_ORDER;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {

			do {
				if(c.getInt(c.getColumnIndex(DbSchema.COL_ORDER_STATUS))!=status){
					continue;
				}

				Order item = new Order();
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER)));
				item.setNote(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				item.setGrandTotal(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
				item.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_DISCOUNT)));
				item.setRestaurantId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
				item.setCustomerId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CASHIER_ID)));
				item.setSelected(false);
				
				try {  
				    item.setCreatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
				    item.setUpdatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
				} catch (Exception e) {
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_MENU_ITEM_NAME +",c."+DbSchema.COL_MENU_GROUP_NAME +" as "+ DbSchema.COL_MENU_ITEM_GROUP_NAME
						+"  FROM " + DbSchema.TBL_ORDER_ITEM + " o " +
											" LEFT JOIN " +  DbSchema.TBL_MENU_ITEM +  " p ON p." +  DbSchema.COL_MENU_ITEM_ID + " = o." + DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID +
											" LEFT JOIN " +  DbSchema.TBL_MENU_GROUP +  " c ON c." +  DbSchema.COL_MENU_GROUP_ID + " = p." + DbSchema.COL_MENU_GROUP_ID +
											" WHERE " +DbSchema.COL_ORDER_ITEM_ORDER_ID + " = '"+item.getId()+"'";
				Cursor cDetail = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderItem> details = new ArrayList<>();
				if (cDetail.moveToFirst()) {
					do {
						OrderItem orderItem = new OrderItem();
						orderItem.setId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_ID)));
						orderItem.setMenuItemName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_MENU_ITEM_NAME)));
						orderItem.setOrderId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_ORDER_ID)));
						orderItem.setMenuItemId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID)));
						orderItem.setQuantity(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_QTY)));
						orderItem.setDiscount(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_DISCOUNT)));
						orderItem.setPrice(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_PRICE)));
						orderItem.setStockState(StockState.valueOf(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_ORDER_ITEM_STATE))));
						details.add(orderItem);
					} while (cDetail.moveToNext());
				}
				
				item.setOrderItems(details);
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insertOrder(Order item){
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_ORDER, item.getId());
		values.put(DbSchema.COL_ORDER_DESCRIPTION, item.getNote());
		values.put(DbSchema.COL_ORDER_AMOUNT, item.getGrandTotal());
		values.put(DbSchema.COL_ORDER_DISCOUNT,item.getDiscount());
		values.put(DbSchema.COL_ORDER_BRANCH_ID, item.getRestaurantId());
		values.put(DbSchema.COL_ORDER_CASHIER_ID, item.getCustomerId());
		values.put(DbSchema.COL_ORDER_ORDERED_ON,  Helper.dateformat.format(item.getCreatedAt()));
		values.put(DbSchema.COL_ORDER_UPDATED_ON,  Helper.dateformat.format(item.getUpdatedAt()));
		db.insert(DbSchema.TBL_ORDER, null, values);
		
		for (OrderItem detail : item.getOrderItems()) {
			ContentValues valuesDetails = new ContentValues();
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_ID, detail.getId());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_ORDER_ID, detail.getOrderId());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID, detail.getMenuItemId());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_PRICE, detail.getPrice());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_QTY,detail.getQuantity());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_DISCOUNT, detail.getDiscount());
			valuesDetails.put(DbSchema.COL_ORDER_ITEM_STATE,false);
			db.insert(DbSchema.TBL_ORDER_ITEM, null, valuesDetails);
		}
		
		return 1;
	}

	public long updateOrderStatus(String orderId, int status)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_STATUS, status);

		return db.update(DbSchema.TBL_ORDER, values, DbSchema.COL_ORDER +"= '"+orderId+"' ", null);
	}

	public long deleteOrder(String orderId){
		return db.delete(DbSchema.TBL_ORDER, DbSchema.COL_ORDER +"= '"+orderId+"' ", null);
	}

	//------------------- ORDER ITEM -----------------//

	public long insertOrderItem(OrderItem item){
		ContentValues valuesDetails = new ContentValues();
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_ID, item.getId());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_ORDER_ID, item.getOrderId());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID, item.getMenuItemId());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_PRICE, item.getPrice());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_QTY,item.getQuantity());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_DISCOUNT, item.getDiscount());
		valuesDetails.put(DbSchema.COL_ORDER_ITEM_STATE,false);
		db.insert(DbSchema.TBL_ORDER_ITEM, null, valuesDetails);

		return 1;
	}

	public long updateOutSoldOrderItem(String orderItemId, boolean isOutOfSold)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_ITEM_STATE, isOutOfSold ? 1:0);

		return db.update(DbSchema.TBL_ORDER_ITEM, values, DbSchema.COL_ORDER_ITEM_ID +"= '"+orderItemId+"' ", null);
	}

	public int deleteOrderItem(String orderItemId)
	{
		return db.delete(DbSchema.TBL_ORDER_ITEM, DbSchema.COL_ORDER_ITEM_ID +"= '"+orderItemId+"' ", null);
	}

	public void updateOrderItemQuantity(String orderItemId, int quantity){
		ContentValues orderItemValues = new ContentValues();
		orderItemValues.put(DbSchema.COL_ORDER_ITEM_QTY, quantity);
		db.update(DbSchema.TBL_ORDER_ITEM, orderItemValues, DbSchema.COL_ORDER_ITEM_ID +"= '"+orderItemId+"' ", null);

	}

	public long updateSumaryOrderInfo(String orderItemId, long amount, long grandTotal)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_ORDER_AMOUNT, amount) ;

		return db.update(DbSchema.TBL_ORDER, values, DbSchema.COL_ORDER +"= '"+orderItemId+"' ", null);
	}
	
	public int delete(String code)
	{
		db.delete(DbSchema.TBL_ORDER_ITEM, DbSchema.COL_ORDER_ITEM_ORDER_ID + "= '" + code + "'", null);
		db.delete(DbSchema.TBL_ORDER, DbSchema.COL_ORDER + "= '" + code + "'", null);
		return 1;
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_ORDER  +
						      " Where lower(" +DbSchema.COL_ORDER + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
