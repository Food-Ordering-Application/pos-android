package com.foa.pos.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	SQLiteDatabase sqlDb;
	public DatabaseHelper(Context context) {
		super(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		sqlDb= db;
		db.execSQL(DbSchema.CREATE_TBL_MENU_GROUP);
		db.execSQL(DbSchema.CREATE_TBL_MENU_ITEM);
		db.execSQL(DbSchema.CREATE_TBL_ORDER);
		db.execSQL(DbSchema.CREATE_TBL_ORDER_ITEM);
		db.execSQL(DbSchema.TBL_ORDER_ITEM_TOPPING);
		db.execSQL(DbSchema.CREATE_TBL_TOPPING_GROUP);
		db.execSQL(DbSchema.CREATE_TBL_TOPPING_ITEM);
		db.execSQL(DbSchema.CREATE_TBL_MENU_ITEM_TOPPING);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DbSchema.DROP_TBL_MENU_GROUP);
		db.execSQL(DbSchema.DROP_TBL_MENU_ITEM);
		db.execSQL(DbSchema.DROP_TBL_ORDER);
		db.execSQL(DbSchema.DROP_TBL_ORDER_ITEM);
		db.execSQL(DbSchema.DROP_TBL_ORDER_ITEM_TOPPING);
		db.execSQL(DbSchema.DROP_TBL_TOPPING_GROUP);
		db.execSQL(DbSchema.DROP_TBL_TOPPING_ITEM);
		db.execSQL(DbSchema.DROP_TBL_MENU_ITEM_TOPPING);
	}

}
