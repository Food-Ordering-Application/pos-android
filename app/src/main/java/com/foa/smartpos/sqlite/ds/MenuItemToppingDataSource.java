package com.foa.smartpos.sqlite.ds;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.MenuItemTopping;
import com.foa.smartpos.model.ToppingGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.sqlite.DbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.foa.smartpos.sqlite.DbSchema.COL_MENU_GROUP_ID;

public class MenuItemToppingDataSource {

	private SQLiteDatabase db;
	public MenuItemToppingDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_MENU_ITEM_TOPPING,null,null);
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
	
	public long insert(MenuItemTopping item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_MENU_ITEM_TOPPING_ID, item.getId());
		values.put(DbSchema.COL_MENU_ITEM_TOPPING_MENU_ID, item.getMenuId());
		values.put(DbSchema.COL_MENU_ITEM_TOPPING_MENU_ITEM_ID, item.getMenuItemId());
		values.put(DbSchema.COL_MENU_ITEM_TOPPING_TOPPING_ITEM_ID, item.getToppingItemId());
		values.put(DbSchema.COL_MENU_ITEM_TOPPING_PIRCE, item.getCustomPrice());

		return db.insert(DbSchema.TBL_MENU_ITEM_TOPPING, null, values);
	}


	public List<ToppingGroup> getToppingGroupByMenuId(String menuItemId){

		//GET TOPPING ITEM
		String stringItemQuery =  " SELECT  ti.* FROM " + DbSchema.TBL_MENU_ITEM_TOPPING + " mit " +
				" JOIN " +  DbSchema.TBL_TOPPING_ITEM +  " ti ON mit." +  DbSchema.COL_MENU_ITEM_TOPPING_TOPPING_ITEM_ID + " = ti." + DbSchema.COL_TOPPING_ITEM_ID +
				" WHERE mit." +DbSchema.COL_MENU_ITEM_TOPPING_MENU_ITEM_ID + " = '"+menuItemId+"'";
		Cursor toppingCursor = db.rawQuery(stringItemQuery,null);

		List<ToppingItem>  toppingItems = new ArrayList<>();
		if (toppingCursor.moveToFirst()){
			do {
				ToppingItem toppingItem = new ToppingItem();
				toppingItem.setId(toppingCursor.getString(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_ID)));
				toppingItem.setName(toppingCursor.getString(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_NAME)));
				toppingItem.setMenuId(toppingCursor.getString(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_MENU_ID)));
				toppingItem.setDescription(toppingCursor.getString(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_DESC)));
				toppingItem.setMaxQuantity(toppingCursor.getInt(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_MAX_QTY)));
				toppingItem.setPrice(toppingCursor.getLong(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_PRICE)));
				toppingItem.setActive(toppingCursor.getInt(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_IS_ACTIVE))==1);
				toppingItem.setToppingGroupId(toppingCursor.getString(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_GROUP_ID)));
				toppingItem.setIndex(toppingCursor.getFloat(toppingCursor.getColumnIndex(DbSchema.COL_TOPPING_ITEM_INDEX)));

				toppingItems.add(toppingItem);
			}while (toppingCursor.moveToNext());
		}

		//GET TOPPING GROUP
		String stringQuery =  " SELECT DISTINCT tg.* FROM " + DbSchema.TBL_MENU_ITEM_TOPPING + " mit " +
				" JOIN " +  DbSchema.TBL_TOPPING_ITEM +  " ti ON mit." +  DbSchema.COL_MENU_ITEM_TOPPING_TOPPING_ITEM_ID + " = ti." + DbSchema.COL_TOPPING_ITEM_ID +
				" JOIN " +  DbSchema.TBL_TOPPING_GROUP +  " tg ON ti." +  DbSchema.COL_TOPPING_ITEM_GROUP_ID + " = tg." + DbSchema.COL_TOPPING_GROUP_ID +
				" WHERE mit." +DbSchema.COL_MENU_ITEM_TOPPING_MENU_ITEM_ID + " = '"+menuItemId+"'";
		Cursor groupCursor = db.rawQuery(stringQuery,null);

		List<ToppingGroup>  toppingGroups = new ArrayList<>();

		if (groupCursor.moveToFirst()) {
			ToppingGroup toppingGroup = new ToppingGroup();
			do {
				toppingGroup.setId(groupCursor.getString(groupCursor.getColumnIndex(DbSchema.COL_TOPPING_GROUP_ID)));
				toppingGroup.setName(groupCursor.getString(groupCursor.getColumnIndex(DbSchema.COL_TOPPING_GROUP_NAME)));
				toppingGroup.setActive(groupCursor.getInt(groupCursor.getColumnIndex(DbSchema.COL_TOPPING_GROUP_IS_ACTIVE))==1);
				toppingGroup.setIndex(groupCursor.getFloat(groupCursor.getColumnIndex(DbSchema.COL_TOPPING_GROUP_INDEX)));
				toppingGroup.setToppingItems(toppingItems.stream().filter(toppingItem -> toppingItem.getToppingGroupId().equals(toppingGroup.getId())).collect(Collectors.toList()));
				toppingGroups.add(toppingGroup);
			} while (groupCursor.moveToNext());
		}
		return toppingGroups;
	}
}
