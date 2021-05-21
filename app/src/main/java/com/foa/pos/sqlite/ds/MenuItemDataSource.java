package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.foa.pos.model.MenuItem;
import com.foa.pos.sqlite.DbSchema;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;

import static com.foa.pos.sqlite.DbSchema.COL_MENU_GROUP_ID;
import static com.foa.pos.sqlite.DbSchema.COL_MENU_GROUP_NAME;

public class MenuItemDataSource {
	private SQLiteDatabase db;
	public MenuItemDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_MENU_ITEM,null,null);
	}
	
	public MenuItem get(String code) {
		 
		MenuItem item = new MenuItem();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_MENU_ITEM +
						       " Where " + DbSchema.COL_MENU_ITEM_ID + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_ID)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_NAME)));
				item.setGroupId(c.getString(c.getColumnIndex(DbSchema.COL_MENU_GROUP_ID)));
				item.setCategoryName(c.getString(c.getColumnIndex(COL_MENU_GROUP_NAME)));
				item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_DESCRIPTION)));
				item.setPrice(c.getLong(c.getColumnIndex(DbSchema.COL_MENU_ITEM_PRICE)));
				item.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_MENU_ITEM_DISCOUNT)));
				item.setCreatedBy(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_CREATED_BY)));
				item.setUpdatedBy(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_UPDATED_BY)));
				item.setStatus(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_STATUS)));
				item.setImage(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_IMAGE)));
				try {
				    item.setSycnOn( Helper.dateFormat.parse(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_SYCN_ON))));
				} catch (Exception e) {
				}
			
			} while (c.moveToNext());
		}
		return item;
	}

	public ArrayList<MenuItem> getAll() {
		return getAll(true,null,null);
	}

	public ArrayList<MenuItem> getAll(String keyword, String categoryid) {
		return getAll(false,keyword,categoryid);
	}
	
	public ArrayList<MenuItem> getAll(boolean isAll, String keyword, String menuGroupId) {
		 
		ArrayList<MenuItem> items = new ArrayList<MenuItem>();

		ArrayList<String> where = new ArrayList<String>();


		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_MENU_ITEM;
		if(!isAll)
			//where.add(DbSchema.COL_MENU_ITEM_STATUS + " = 1 ");
		if(keyword != null && !keyword.equals(""))
			where.add(DbSchema.COL_MENU_ITEM_NAME + " like '%" + keyword + "%' ");
		if(menuGroupId != null && !menuGroupId.equals(""))
			where.add(DbSchema.COL_MENU_GROUP_ID + " = '" + menuGroupId + "' ");

		if(where.size() != 0)
			selectQuery += " where " + TextUtils.join(" AND ",where);

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				MenuItem item = new MenuItem();
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_ID)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_NAME)));
				item.setGroupId(c.getString(c.getColumnIndex(DbSchema.COL_MENU_GROUP_ID)));
				item.setCategoryName(c.getString(c.getColumnIndex(COL_MENU_GROUP_NAME)));
				item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_DESCRIPTION)));
				item.setPrice(c.getLong(c.getColumnIndex(DbSchema.COL_MENU_ITEM_PRICE)));
				item.setDiscount(c.getLong(c.getColumnIndex(DbSchema.COL_MENU_ITEM_DISCOUNT)));
				item.setCreatedBy(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_CREATED_BY)));
				item.setUpdatedBy(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_UPDATED_BY)));
				item.setStatus(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_STATUS)));
				item.setImage(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_IMAGE)));
				try {
				    item.setSycnOn( Helper.dateFormat.parse(c.getString(c.getColumnIndex(DbSchema.COL_MENU_ITEM_SYCN_ON))));
				} catch (Exception e) {
				}
				
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(MenuItem item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_MENU_ITEM_ID, item.getId());
		values.put(DbSchema.COL_MENU_ITEM_NAME, item.getName());
		values.put(DbSchema.COL_MENU_GROUP_ID, item.getGroupId());
		values.put(DbSchema.COL_MENU_ITEM_DESCRIPTION, item.getDescription());
		values.put(DbSchema.COL_MENU_ITEM_PRICE, item.getPrice());
		values.put(DbSchema.COL_MENU_ITEM_DISCOUNT,item.getDiscount());
		values.put(DbSchema.COL_MENU_ITEM_CREATED_BY, "Device");
		values.put(DbSchema.COL_MENU_ITEM_UPDATED_BY, "Device");
		values.put(DbSchema.COL_MENU_ITEM_STATUS, item.getStatus());
		values.put(DbSchema.COL_MENU_ITEM_IS_ACTIVE, item.isActive()?1:0);
		values.put(DbSchema.COL_MENU_ITEM_INDEX, item.getIndex());
		values.put(DbSchema.COL_MENU_ITEM_IMAGE, item.getImage());
		
		return db.insert(DbSchema.TBL_MENU_ITEM, null, values);
	}
	
	public long update(MenuItem item, String lastCode)
	{
		ContentValues values = new ContentValues();
		if(item.getId() != null)
			values.put(DbSchema.COL_MENU_ITEM_ID, item.getId());
		
		if(item.getName() != null)
			values.put(DbSchema.COL_MENU_ITEM_NAME, item.getName());
		
		if(item.getGroupId() != null)
			values.put(DbSchema.COL_MENU_GROUP_ID, item.getGroupId());
		
		if(item.getDescription() != null)
			values.put(DbSchema.COL_MENU_ITEM_DESCRIPTION, item.getDescription());
		
		if(item.getStatus() != null)
			values.put(DbSchema.COL_MENU_ITEM_STATUS, item.getStatus());
		
		if(item.getImage() != null)
			values.put(DbSchema.COL_MENU_ITEM_IMAGE, item.getImage());

		values.put(DbSchema.COL_MENU_ITEM_PRICE, item.getPrice());
		values.put(DbSchema.COL_MENU_ITEM_DISCOUNT, item.getDiscount());
		
		return db.update(DbSchema.TBL_MENU_ITEM, values, DbSchema.COL_MENU_ITEM_ID +"= '"+lastCode+"' ", null);
	}

	public String getIdByName(String name) {

		boolean has = false;
		String selectQuery = " SELECT * FROM " + DbSchema.TBL_MENU_GROUP +
				" Where lower(" + COL_MENU_GROUP_NAME + ") = '"+name.toLowerCase()+"'";

		Cursor c = db.rawQuery(selectQuery, null);
		int x = c.getCount();
		String y =name;
		if(c.getCount() ==1){
			c.moveToFirst();
			return c.getString(c.getColumnIndex(COL_MENU_GROUP_ID));

		}
		return "";
	}
	
	public int delete(String id)
	{
		return db.delete(DbSchema.TBL_MENU_ITEM, DbSchema.COL_MENU_ITEM_ID + "= '" + id + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_MENU_ITEM +
						      " Where lower(" + DbSchema.COL_MENU_ITEM_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekName(String name) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_MENU_ITEM +
						      " Where lower(" + DbSchema.COL_MENU_ITEM_NAME + ") = '"+name.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekAvailable(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_ORDER_ITEM +
						      " Where lower(" + DbSchema.COL_ORDER_ITEM_MENU_ITEM_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
