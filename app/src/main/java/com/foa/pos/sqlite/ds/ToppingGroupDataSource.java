package com.foa.pos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.pos.model.ToppingGroup;
import com.foa.pos.sqlite.DbSchema;

import java.util.ArrayList;

import static com.foa.pos.sqlite.DbSchema.COL_TOPPING_GROUP_ID;

public class ToppingGroupDataSource {

	private SQLiteDatabase db;
	public ToppingGroupDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate() {
		return db.delete(DbSchema.TBL_TOPPING_GROUP, null, null);
	}
	

	public ArrayList<ToppingGroup> getAll() {
		 
		ArrayList<ToppingGroup> items = new ArrayList<ToppingGroup>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_TOPPING_GROUP;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				ToppingGroup item = new ToppingGroup();
				item.setId(c.getString(c.getColumnIndex(COL_TOPPING_GROUP_ID)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_TOPPING_GROUP_NAME)));
				item.setActive(c.getInt(c.getColumnIndex(DbSchema.COL_TOPPING_GROUP_IS_ACTIVE))>0);
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(ToppingGroup item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_TOPPING_GROUP_ID, item.getId());
		values.put(DbSchema.COL_TOPPING_GROUP_NAME, item.getName());
		values.put(DbSchema.COL_TOPPING_GROUP_INDEX, item.getIndex());
		values.put(DbSchema.COL_TOPPING_GROUP_IS_ACTIVE, item.isActive()?1:0);
		return db.insert(DbSchema.TBL_TOPPING_GROUP, null, values);
	}
}
