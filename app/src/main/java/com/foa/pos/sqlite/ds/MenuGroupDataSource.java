package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.model.MenuGroup;
import com.foa.pos.sqlite.DbSchema;

import java.util.ArrayList;

import static com.foa.pos.sqlite.DbSchema.COL_PRODUCT_CATEGORY_CODE;

public class MenuGroupDataSource {
	
	private SQLiteDatabase db;
	public MenuGroupDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_PRODUCT_CATEGORY,null,null);
	}
	
	public MenuGroup get(String code) {
		 
		MenuGroup item = new MenuGroup();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_PRODUCT_CATEGORY  +
						       " Where " + COL_PRODUCT_CATEGORY_CODE + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setId(c.getString(c.getColumnIndex(COL_PRODUCT_CATEGORY_CODE)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_NAME)));
				
			} while (c.moveToNext());
		}
		return item;
	}
	

	public ArrayList<MenuGroup> getAll() {
		 
		ArrayList<MenuGroup> items = new ArrayList<MenuGroup>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_PRODUCT_CATEGORY ;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				MenuGroup item = new MenuGroup();
				item.setId(c.getString(c.getColumnIndex(COL_PRODUCT_CATEGORY_CODE)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_NAME)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(MenuGroup item)
	{
		ContentValues values = new ContentValues();
		values.put(COL_PRODUCT_CATEGORY_CODE, item.getId());
		values.put(DbSchema.COL_PRODUCT_CATEGORY_NAME, item.getName());
		
		return db.insert(DbSchema.TBL_PRODUCT_CATEGORY, null, values);
	}
	
	public long update(MenuGroup item, String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(COL_PRODUCT_CATEGORY_CODE, item.getId());
		values.put(DbSchema.COL_PRODUCT_CATEGORY_NAME, item.getName());
		
		return db.update(DbSchema.TBL_PRODUCT_CATEGORY, values, COL_PRODUCT_CATEGORY_CODE+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_PRODUCT_CATEGORY, COL_PRODUCT_CATEGORY_CODE + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_PRODUCT_CATEGORY  +
						      " Where lower(" + COL_PRODUCT_CATEGORY_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekName(String name) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_PRODUCT_CATEGORY  +
						      " Where lower(" + DbSchema.COL_PRODUCT_CATEGORY_NAME + ") = '"+name.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}


	
	public boolean cekAvailable(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_MENU_ITEM +
						      " Where lower(" + DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
