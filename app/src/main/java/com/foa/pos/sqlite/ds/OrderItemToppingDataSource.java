package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.model.MenuGroup;
import com.foa.pos.model.OrderItemTopping;
import com.foa.pos.model.enums.StockState;
import com.foa.pos.sqlite.DbSchema;

import java.util.ArrayList;
import java.util.List;

import static com.foa.pos.sqlite.DbSchema.COL_MENU_GROUP_ID;
import static com.foa.pos.sqlite.DbSchema.COL_ORDER_ITEM_TOPPING_ID;

public class OrderItemToppingDataSource {

	private SQLiteDatabase db;
	public OrderItemToppingDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_ORDER_ITEM_TOPPING,null,null);
	}
	
	public MenuGroup get(String code) {
		 
		MenuGroup item = new MenuGroup();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_MENU_GROUP +
						       " Where " + COL_MENU_GROUP_ID + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setId(c.getString(c.getColumnIndex(COL_MENU_GROUP_ID)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_MENU_GROUP_NAME)));
				
			} while (c.moveToNext());
		}
		return item;
	}
	

	public ArrayList<MenuGroup> getAll() {
		 
		ArrayList<MenuGroup> items = new ArrayList<MenuGroup>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_MENU_GROUP;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				MenuGroup item = new MenuGroup();
				item.setId(c.getString(c.getColumnIndex(COL_MENU_GROUP_ID)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_MENU_GROUP_NAME)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insertMany(List<OrderItemTopping> orderItemToppings)
	{
		long success = -1;
		for (OrderItemTopping item :orderItemToppings) {
			ContentValues values = new ContentValues();
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_ID, item.getId());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_TOPPING_ID, item.getMenuItemToppingId());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_ORDER_ITEM_ID, item.getId());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_PRICE, item.getPrice());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_QTY, item.getQuantity());
			values.put(DbSchema.COL_ORDER_ITEM_TOPPING_STATE,item.getState().toString());

			success =  db.insert(DbSchema.TBL_MENU_GROUP, null, values);
			if (success==-1) break;
		}
		return success;
	}
	
	public long update(MenuGroup item, String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(COL_MENU_GROUP_ID, item.getId());
		values.put(DbSchema.COL_MENU_GROUP_NAME, item.getName());
		
		return db.update(DbSchema.TBL_MENU_GROUP, values, COL_MENU_GROUP_ID +"= '"+lastCode+"' ", null);
	}

	public int delete(String orderItemToppingId)
	{
		return db.delete(DbSchema.TBL_ORDER_ITEM_TOPPING, COL_ORDER_ITEM_TOPPING_ID + "= '" + orderItemToppingId + "'", null);
	}

}
