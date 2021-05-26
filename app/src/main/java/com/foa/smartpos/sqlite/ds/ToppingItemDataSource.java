package com.foa.smartpos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.sqlite.DbSchema;

import java.util.ArrayList;

import static com.foa.smartpos.sqlite.DbSchema.COL_MENU_GROUP_ID;

public class ToppingItemDataSource {

	private SQLiteDatabase db;
	public ToppingItemDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_TOPPING_ITEM,null,null);
	}

	public ArrayList<MenuGroup> getAll() {
		 
		ArrayList<MenuGroup> items = new ArrayList<MenuGroup>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_TOPPING_ITEM;
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
	
	public long insert(ToppingItem item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_TOPPING_ITEM_ID, item.getId());
		values.put(DbSchema.COL_TOPPING_ITEM_NAME, item.getName());
		values.put(DbSchema.COL_TOPPING_ITEM_GROUP_ID, item.getToppingGroupId());
		values.put(DbSchema.COL_TOPPING_ITEM_DESC, item.getDescription());
		values.put(DbSchema.COL_TOPPING_ITEM_MAX_QTY, item.getMaxQuantity());
		values.put(DbSchema.COL_TOPPING_ITEM_INDEX, item.getIndex());
		values.put(DbSchema.COL_TOPPING_ITEM_PRICE, item.getPrice());
		values.put(DbSchema.COL_TOPPING_ITEM_IS_ACTIVE, item.isActive()?1:0);

		return db.insert(DbSchema.TBL_TOPPING_ITEM, null, values);
	}


}
