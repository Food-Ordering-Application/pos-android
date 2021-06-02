package com.foa.smartpos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.sqlite.DbSchema;

import java.util.ArrayList;
import java.util.List;

import static com.foa.smartpos.sqlite.DbSchema.COL_MENU_GROUP_ID;
import static com.foa.smartpos.sqlite.DbSchema.COL_MENU_ITEM_ID;
import static com.foa.smartpos.sqlite.DbSchema.COL_TOPPING_ITEM_ID;

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

	public List<ToppingItem> getAll(String keyword) {

		List<ToppingItem> items = new ArrayList<>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_TOPPING_ITEM;
		ArrayList<String> where = new ArrayList<String>();

			if(keyword != null && !keyword.equals(""))
				where.add(DbSchema.COL_MENU_ITEM_NAME + " like '%" + keyword + "%' ");

		if(where.size() != 0)
			selectQuery += " where " + TextUtils.join(" AND ",where);

		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				ToppingItem item = new ToppingItem();
				item.setId(c.getString(c.getColumnIndex(COL_TOPPING_ITEM_ID)));
				item.setName(c.getString(c.getColumnIndex(DbSchema.COL_MENU_GROUP_NAME)));
				item.setPrice(c.getLong(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_PRICE)));
				item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_DESC)));
				item.setMaxQuantity(c.getInt(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_MAX_QTY)));
				item.setToppingGroupId(c.getString(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_GROUP_ID)));
				item.setMenuId(c.getString(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_MENU_ID)));
				item.setActive(c.getInt(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_IS_ACTIVE))>0);
				item.setStockState(StockState.valueOf(c.getString(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_STOCK_STATE))));
				item.setIndex(c.getInt(c.getColumnIndex(DbSchema.COL_TOPPING_ITEM_INDEX)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}

	public long updateStockState(String toppingItemId, StockState stockState)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_MENU_ITEM_STOCK_STATE, stockState.toString());
		return db.update(DbSchema.TBL_TOPPING_ITEM, values, COL_TOPPING_ITEM_ID +"= '"+toppingItemId+"' ", null);
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
		values.put(DbSchema.COL_TOPPING_ITEM_STOCK_STATE, item.getStockState().toString());
		values.put(DbSchema.COL_TOPPING_ITEM_IS_ACTIVE, item.isActive()?1:0);

		return db.insert(DbSchema.TBL_TOPPING_ITEM, null, values);
	}


}
