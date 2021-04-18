package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.entity.User;
import com.foa.pos.sqlite.DbSchema;
import com.foa.pos.utils.Helper;

import java.util.ArrayList;
import java.util.Date;

public class UserDataSource {
	private SQLiteDatabase db;
	public UserDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_USER,null,null);
	}
	
	public User get(String code) {
		 
		User item = new User();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_USER  +
						       " Where " +DbSchema.COL_USER_CODE + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_USER_CODE)));
				item.setUserName(c.getString(c.getColumnIndex(DbSchema.COL_USER_NAME)));
				item.setCashierID(c.getString(c.getColumnIndex(DbSchema.COL_USER_CASHIER_ID)));
				item.setLevel(c.getString(c.getColumnIndex(DbSchema.COL_USER_LEVEL)));
				
				try {  
				    item.setLastLogin( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_USER_LAST_LOGIN))));
				} catch (Exception e) {
				}
			
				
				
			} while (c.moveToNext());
		}
		return item;
	}
	
	
	public ArrayList<User> getAll() {
		 
		ArrayList<User> items = new ArrayList<User>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_USER ;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				User item = new User();
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_USER_CODE)));
				item.setUserName(c.getString(c.getColumnIndex(DbSchema.COL_USER_NAME)));
				item.setCashierID(c.getString(c.getColumnIndex(DbSchema.COL_USER_CASHIER_ID)));
				item.setLevel(c.getString(c.getColumnIndex(DbSchema.COL_USER_LEVEL)));
				
				try {  
				    item.setLastLogin( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_USER_LAST_LOGIN))));
				} catch (Exception e) {
				}
				
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(User item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_USER_CODE, item.getId());
		values.put(DbSchema.COL_USER_NAME, item.getUserName());
		values.put(DbSchema.COL_USER_CASHIER_ID, item.getCashierID());
		values.put(DbSchema.COL_USER_PASSWORD, Helper.getMD5(item.getPassword()));
		values.put(DbSchema.COL_USER_LEVEL, item.getLevel());
		
		return db.insert(DbSchema.TBL_USER, null, values);
	}
	
	public long update(User item, String lastCode)
	{
		ContentValues values = new ContentValues();
		if(item.getId() != null)
			values.put(DbSchema.COL_USER_CODE, item.getId());
		
		if(item.getUserName() != null)
			values.put(DbSchema.COL_USER_NAME, item.getUserName());
		
		if(item.getCashierID() != null)
			values.put(DbSchema.COL_USER_CASHIER_ID, item.getCashierID());
		
		if(item.getPassword() != null)
			values.put(DbSchema.COL_USER_PASSWORD, Helper.getMD5(item.getPassword()));
		
		if(item.getLevel() != null)
			values.put(DbSchema.COL_USER_LEVEL, item.getLevel());
		
		return db.update(DbSchema.TBL_USER, values, DbSchema.COL_USER_CODE+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_USER, DbSchema.COL_USER_CODE + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_USER  +
						      " Where lower(" +DbSchema.COL_USER_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public User auth(String name, String password) {
		 
		User item = new User();
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_USER  +
						      " Where lower(" +DbSchema.COL_USER_NAME + ") = '"+name.toLowerCase()+"' and lower(" +DbSchema.COL_USER_PASSWORD + ") = '"+ Helper.getMD5(password) +"' ";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c.moveToFirst()) {
			do {
			
				item.setId(c.getString(c.getColumnIndex(DbSchema.COL_USER_CODE)));
				item.setUserName(c.getString(c.getColumnIndex(DbSchema.COL_USER_NAME)));
				item.setCashierID(c.getString(c.getColumnIndex(DbSchema.COL_USER_CASHIER_ID)));
				item.setLevel(c.getString(c.getColumnIndex(DbSchema.COL_USER_LEVEL)));
				
				try {  
				    item.setLastLogin( Helper.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_USER_LAST_LOGIN))));
				} catch (Exception e) {
				}
			
			} while (c.moveToNext());
		}
		
		if(item.getId() != null)
		{
			ContentValues values = new ContentValues();
			values.put(DbSchema.COL_USER_LAST_LOGIN, Helper.dateformat.format(new Date()));
			 db.update(DbSchema.TBL_USER, values, DbSchema.COL_USER_CODE+"= '"+item.getId()+"' ", null);
		}
		
		
		return item;
	}
	
	public boolean cekName(String name) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_USER  +
						      " Where lower(" +DbSchema.COL_USER_NAME + ") = '"+name.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekAvailable(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_ORDER  +
						      " Where lower(" +DbSchema.COL_ORDER_USER_ID + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
}
