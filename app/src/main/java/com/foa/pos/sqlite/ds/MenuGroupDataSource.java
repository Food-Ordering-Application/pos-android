package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.model.MenuGroup;
import com.foa.pos.sqlite.DbSchema;

import java.util.ArrayList;

import static com.foa.pos.sqlite.DbSchema.COL_MENU_GROUP_ID;

public class MenuGroupDataSource {
	
	private SQLiteDatabase db;
	public MenuGroupDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_MENU_GROUP,null,null);
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
	
	public long insert(MenuGroup item)
	{
		ContentValues values = new ContentValues();
		values.put(COL_MENU_GROUP_ID, item.getId());
		values.put(DbSchema.COL_MENU_GROUP_NAME, item.getName());
		
		return db.insert(DbSchema.TBL_MENU_GROUP, null, values);
	}
	
	public long update(MenuGroup item, String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(COL_MENU_GROUP_ID, item.getId());
		values.put(DbSchema.COL_MENU_GROUP_NAME, item.getName());
		
		return db.update(DbSchema.TBL_MENU_GROUP, values, COL_MENU_GROUP_ID +"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_MENU_GROUP, COL_MENU_GROUP_ID + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_MENU_GROUP +
						      " Where lower(" + COL_MENU_GROUP_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekName(String name) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_MENU_GROUP +
						      " Where lower(" + DbSchema.COL_MENU_GROUP_NAME + ") = '"+name.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}


	
	public boolean cekAvailable(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_MENU_ITEM +
						      " Where lower(" + DbSchema.COL_MENU_ITEM_GROUP_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
