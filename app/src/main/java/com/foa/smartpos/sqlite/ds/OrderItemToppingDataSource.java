package com.foa.smartpos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.sqlite.DbSchema;

import java.util.ArrayList;

import static com.foa.smartpos.sqlite.DbSchema.COL_MENU_GROUP_ID;
import static com.foa.smartpos.sqlite.DbSchema.COL_ORDER_ITEM_TOPPING_ID;

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
