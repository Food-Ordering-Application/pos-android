package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.model.MenuItem;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.sqlite.DbSchema;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderDataSource {
	private SQLiteDatabase db;
	public OrderDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_ORDER,null,null);
	}
	
	public Order get(String id) {
		 
		Order order = new Order();
		 
		String selectQuery = 	" SELECT  o.*,u."+DbSchema.COL_USER_NAME+"  FROM " + DbSchema.TBL_ORDER   + " o " +
								" LEFT JOIN " +  DbSchema.TBL_USER +  " u ON u." +  DbSchema.COL_USER_CODE + " = o." + DbSchema.COL_ORDER_USER_ID +
								" Where " +DbSchema.COL_ORDER_CODE + " = '"+id+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				order.setId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CODE)));
				order.setNote(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				order.setGrandTotal(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
				order.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				order.setRestaurantId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
				order.setCustomerId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_USER_ID)));
				
				try {  
				    order.setCreatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
				    order.setUpdatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
				    order.setSycnAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYCN_ON))));
				} catch (Exception e) {
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_PRODUCT_NAME+",c."+DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME+"  FROM " + DbSchema.TBL_PRODUCT_ORDER_DETAIL  + " o " +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT +  " p ON p." +  DbSchema.COL_PRODUCT_CODE + " = o." + DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT_CATEGORY +  " c ON c." +  DbSchema.COL_PRODUCT_CATEGORY_CODE + " = p." + DbSchema.COL_PRODUCT_CATEGORY_CODE +
											" WHERE " +DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " = '"+id+"'";
				Cursor cDetail = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderItem> details = new ArrayList<OrderItem>();
				if (cDetail.moveToFirst()) {
					do {
						
						OrderItem orderItem = new OrderItem();
						orderItem.setId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE)));
						orderItem.setMenuItemName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_NAME)));
						orderItem.setOrderId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE)));
						orderItem.setMenuItemId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE)));
						orderItem.setQuantity(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY)));
						orderItem.setDiscount(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT)));
						orderItem.setPrice(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE)));
						orderItem.setOutSlod(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_IS_OUT_SOLD))>0);
						details.add(orderItem);
					} while (cDetail.moveToNext());
				}
				
				 order.setOrderItems(details);
			
			} while (c.moveToNext());
		}
		return order;
	}
	
	
	public ArrayList<Order> getAll() {
		return getAll(null,null,null,null);
	}
	
	public ArrayList<Order> getAll(ArrayList<HashMap<String, String>> filter, String orderby, String limit, String offset) {
		 
		ArrayList<Order> items = new ArrayList<Order>();
		//Cursor c = db.rawQuery("DROP TABLE "+DbSchema.TBL_ORDER , null);
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_ORDER;

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			String x  = c.getColumnName(0);
			do {
				Order item = new Order();
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CODE)));
				item.setNote(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				item.setGrandTotal(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
				item.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_ORDER_DISCOUNT)));
				item.setRestaurantId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
				item.setCustomerId(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_USER_ID)));
				item.setSelected(false);
				
				try {  
				    item.setCreatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
				    item.setUpdatedAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
				    item.setSycnAt( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYCN_ON))));
				} catch (Exception e) {
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_PRODUCT_NAME+",c."+DbSchema.COL_PRODUCT_CATEGORY_NAME+" as "+ DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME
						+"  FROM " + DbSchema.TBL_PRODUCT_ORDER_DETAIL  + " o " +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT +  " p ON p." +  DbSchema.COL_PRODUCT_CODE + " = o." + DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT_CATEGORY +  " c ON c." +  DbSchema.COL_PRODUCT_CATEGORY_CODE + " = p." + DbSchema.COL_PRODUCT_CATEGORY_CODE +
											" WHERE " +DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " = '"+item.getId()+"'";
				Cursor cDetail = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderItem> details = new ArrayList<>();
				if (cDetail.moveToFirst()) {
					do {
						OrderItem orderItem = new OrderItem();
						orderItem.setId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE)));
						orderItem.setMenuItemName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_NAME)));
						orderItem.setOrderId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE)));
						orderItem.setMenuItemId(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE)));
						orderItem.setQuantity(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY)));
						orderItem.setDiscount(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT)));
						orderItem.setPrice(cDetail.getLong(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE)));
						details.add(orderItem);
					} while (cDetail.moveToNext());
				}
				
				item.setOrderItems(details);
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(Order item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_ORDER_CODE, item.getId());
		values.put(DbSchema.COL_ORDER_DESCRIPTION, item.getNote());
		values.put(DbSchema.COL_ORDER_AMOUNT, item.getGrandTotal());
		values.put(DbSchema.COL_ORDER_DISCOUNT,item.getDiscount());
		values.put(DbSchema.COL_ORDER_BRANCH_ID, item.getRestaurantId());
		values.put(DbSchema.COL_ORDER_USER_ID, item.getCustomerId());
		values.put(DbSchema.COL_ORDER_ORDERED_ON,  Helper.dateformat.format(item.getCreatedAt()));
		values.put(DbSchema.COL_ORDER_UPDATED_ON,  Helper.dateformat.format(item.getUpdatedAt()));
	//	values.put(DbSchema.COL_ORDER_SYCN_ON, Shared.dateformat.format(item.getSycnOn()));
		db.insert(DbSchema.TBL_ORDER, null, values);
		
		for (OrderItem detail : item.getOrderItems()) {
			ContentValues valuesDetails = new ContentValues();
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE, detail.getId());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE, detail.getOrderId());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE, detail.getMenuItemId());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE, detail.getPrice());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY,detail.getQuantity());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT, detail.getDiscount());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_IS_OUT_SOLD,false);
			db.insert(DbSchema.TBL_PRODUCT_ORDER_DETAIL, null, valuesDetails);
		}
		
		return 1;
	}

	public long updateOutSoldOrderItem(String orderItemId, boolean isOutOfSold)
	{
		ContentValues values = new ContentValues();

		values.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_IS_OUT_SOLD, isOutOfSold ? 1:0);

		return db.update(DbSchema.TBL_PRODUCT_ORDER_DETAIL, values, DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE+"= '"+orderItemId+"' ", null);
	}
	
	public int delete(String code)
	{
		db.delete(DbSchema.TBL_PRODUCT_ORDER_DETAIL, DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + "= '" + code + "'", null);
		db.delete(DbSchema.TBL_ORDER, DbSchema.COL_ORDER_CODE + "= '" + code + "'", null);
		return 1;
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_ORDER  +
						      " Where lower(" +DbSchema.COL_ORDER_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
