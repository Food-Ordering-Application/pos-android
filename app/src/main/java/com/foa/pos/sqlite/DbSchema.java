package com.foa.pos.sqlite;

public interface DbSchema {
	
	String DB_NAME = "com_foa_androidpos.db";
	int DB_VERSION = 1;
	
	String TBL_MENU_GROUP = "menu_group";
	String COL_MENU_GROUP_ID = "menu_group_id";
	String COL_MENU_GROUP_NAME = "name";
	
	String CREATE_TBL_MENU_GROUP = "CREATE TABLE "
			+ TBL_MENU_GROUP
			+ "(" 
				+ COL_MENU_GROUP_ID + " TEXT PRIMARY KEY,"
				+ COL_MENU_GROUP_NAME + " TEXT"
			+ ");";
	
	
	String TBL_MENU_ITEM = "menu_item";
	String COL_MENU_ITEM_ID = "menu_item_id";
	String COL_MENU_ITEM_GROUP_ID = "menu_group_id";
	String COL_MENU_ITEM_GROUP_NAME = "group_name";
	String COL_MENU_ITEM_NAME = "name";
	String COL_MENU_ITEM_DESCRIPTION = "description";
	String COL_MENU_ITEM_PRICE = "price";
	String COL_MENU_ITEM_DISCOUNT = "discount";
	String COL_MENU_ITEM_SYCN_ON = "sycn_on";
	String COL_MENU_ITEM_CREATED_BY = "created_by";
	String COL_MENU_ITEM_UPDATED_BY = "updated_by";
	String COL_MENU_ITEM_STATUS = "status";
	String COL_MENU_ITEM_IMAGE = "image";
	String COL_MENU_ITEM_IS_ACTIVE = "is_active";
	String COL_MENU_ITEM_INDEX = "item_index";

	String CREATE_TBL_MENU_ITEM = "CREATE TABLE "
			+ TBL_MENU_ITEM
			+ "(" 
				+ COL_MENU_ITEM_ID + " TEXT PRIMARY KEY,"
				+ COL_MENU_ITEM_GROUP_ID + " TEXT,"
				+ COL_MENU_ITEM_NAME + " TEXT,"
				+ COL_MENU_ITEM_DESCRIPTION + " TEXT,"
				+ COL_MENU_ITEM_PRICE + " INTEGER,"
				+ COL_MENU_ITEM_DISCOUNT + " INTEGER,"
				+ COL_MENU_ITEM_SYCN_ON + " DATETIME,"
				+ COL_MENU_ITEM_CREATED_BY + " TEXT,"
				+ COL_MENU_ITEM_UPDATED_BY + " TEXT,"
				+ COL_MENU_ITEM_STATUS + " TEXT,"
				+ COL_MENU_ITEM_IMAGE + " TEXT,"
				+ COL_MENU_ITEM_IS_ACTIVE + " INTEGER,"
				+ COL_MENU_ITEM_INDEX + " REAL"
			+ ");";
	
	String TBL_ORDER = "order_pos";
	String COL_ORDER = "order_id";
	String COL_ORDER_ORDERED_ON = "ordered_on";
	String COL_ORDER_UPDATED_ON = "updated_on";
	String COL_ORDER_SYCN_ON = "sycn_on";
	String COL_ORDER_DESCRIPTION = "description";
	String COL_ORDER_TAX = "tax";
	String COL_ORDER_DISCOUNT = "discount";
	String COL_ORDER_AMOUNT = "amount";
	String COL_ORDER_CASHIER_ID = "cashier_id";
	String COL_ORDER_BRANCH_ID = "branch_id";
	String COL_ORDER_STATUS = "status";
	String COL_SUM_QTY = "sum_quantity";
	
	String CREATE_TBL_ORDER = "CREATE TABLE "
			+ TBL_ORDER
			+ "(" 
				+ COL_ORDER + " TEXT PRIMARY KEY,"
				+ COL_ORDER_ORDERED_ON + " DATETIME,"
				+ COL_ORDER_UPDATED_ON + " DATETIME,"
				+ COL_ORDER_SYCN_ON + " DATETIME,"
				+ COL_ORDER_DESCRIPTION + " TEXT,"
				+ COL_ORDER_TAX + " INTEGER,"
				+ COL_ORDER_DISCOUNT + " INTEGER,"
				+ COL_ORDER_AMOUNT + " INTEGER,"
				+ COL_ORDER_CASHIER_ID + " TEXT,"
				+ COL_ORDER_STATUS + " INTEGER,"
				+ COL_ORDER_BRANCH_ID + " TEXT"
			+ ");";
	
	String TBL_ORDER_ITEM = "order_item";
	String COL_ORDER_ITEM_ID = "order_item_id";
	String COL_ORDER_ITEM_MENU_ITEM_ID = "menu_item_id";
	String COL_ORDER_ITEM_ORDER_ID = "order_id";
	String COL_ORDER_ITEM_PRICE = "price";
	String COL_ORDER_ITEM_DISCOUNT = "discount";
	String COL_ORDER_ITEM_QTY = "qty";
	String COL_ORDER_ITEM_STATE = "state";
	
	String CREATE_TBL_ORDER_ITEM = "CREATE TABLE "
			+ TBL_ORDER_ITEM
			+ "(" 
				+ COL_ORDER_ITEM_ID + " TEXT PRIMARY KEY,"
				+ COL_ORDER_ITEM_MENU_ITEM_ID + " TEXT,"
				+ COL_ORDER_ITEM_ORDER_ID + " TEXT,"
				+ COL_ORDER_ITEM_PRICE + " INTEGER,"
				+ COL_ORDER_ITEM_QTY + " INTEGER,"
				+ COL_ORDER_ITEM_DISCOUNT + " INTEGER,"
				+ COL_ORDER_ITEM_STATE + " INTEGER"
			+ ");";

	String TBL_TOPPING_ITEM = "topping_item";
	String COL_TOPPING_ITEM_ID = "topping_item_id";
	String COL_TOPPING_ITEM_NAME = "name";
	String COL_TOPPING_ITEM_MENU_ID = "menu_id";
	String COL_TOPPING_ITEM_GROUP_ID = "topping_group_id";
	String COL_TOPPING_ITEM_PRICE = "price";
	String COL_TOPPING_ITEM_DESC = "description";
	String COL_TOPPING_ITEM_MAX_QTY = "max_quantity";
	String COL_TOPPING_ITEM_IS_ACTIVE = "is_active";
	String COL_TOPPING_ITEM_INDEX = "topping_index";

	String CREATE_TBL_TOPPING_ITEM = "CREATE TABLE "
			+ TBL_TOPPING_ITEM
			+ "("
			+ COL_TOPPING_ITEM_ID + " TEXT PRIMARY KEY,"
			+ COL_TOPPING_ITEM_MENU_ID + " TEXT,"
			+ COL_TOPPING_ITEM_NAME + " TEXT,"
			+ COL_TOPPING_ITEM_GROUP_ID + " TEXT,"
			+ COL_TOPPING_ITEM_DESC + " TEXT,"
			+ COL_TOPPING_ITEM_PRICE + " INTEGER,"
			+ COL_TOPPING_ITEM_MAX_QTY + " INTEGER,"
			+ COL_TOPPING_ITEM_INDEX + " REAL,"
			+ COL_TOPPING_ITEM_IS_ACTIVE + " INTEGER"
			+ ");";

	String TBL_TOPPING_GROUP = "topping_group";
	String COL_TOPPING_GROUP_ID = "topping_group_id";
	String COL_TOPPING_GROUP_MENU_ID = "menu_id";
	String COL_TOPPING_GROUP_NAME = "name";
	String COL_TOPPING_GROUP_IS_ACTIVE = "is_active";
	String COL_TOPPING_GROUP_INDEX = "group_index";

	String CREATE_TBL_TOPPING_GROUP = "CREATE TABLE "
			+ TBL_TOPPING_GROUP
			+ "("
			+ COL_TOPPING_GROUP_ID + " TEXT PRIMARY KEY,"
			+ COL_TOPPING_GROUP_MENU_ID + " TEXT,"
			+ COL_TOPPING_GROUP_NAME + " TEXT,"
			+ COL_TOPPING_GROUP_INDEX + " REAL,"
			+ COL_TOPPING_GROUP_IS_ACTIVE + " INTEGER"
			+ ");";

	String TBL_MENU_ITEM_TOPPING = "menu_item_topping";
	String COL_MENU_ITEM_TOPPING_ID = "id";
	String COL_MENU_ITEM_TOPPING_MENU_ID = "menu_id";
	String COL_MENU_ITEM_TOPPING_MENU_ITEM_ID = "menu_item_id";
	String COL_MENU_ITEM_TOPPING_TOPPING_ITEM_ID = "topping_item_id";
	String COL_MENU_ITEM_TOPPING_PIRCE = "price";

	String CREATE_TBL_MENU_ITEM_TOPPING = "CREATE TABLE "
			+ TBL_MENU_ITEM_TOPPING
			+ "("
			+ COL_MENU_ITEM_TOPPING_ID + " TEXT PRIMARY KEY,"
			+ COL_MENU_ITEM_TOPPING_MENU_ID + " TEXT,"
			+ COL_MENU_ITEM_TOPPING_MENU_ITEM_ID + " TEXT,"
			+ COL_MENU_ITEM_TOPPING_TOPPING_ITEM_ID + " TEXT,"
			+ COL_MENU_ITEM_TOPPING_PIRCE + " INTEGER"
			+ ");";

	String DROP_TBL_MENU_GROUP = "DROP TABLE IF EXISTS "+ TBL_MENU_GROUP;
	String DROP_TBL_MENU_ITEM = "DROP TABLE IF EXISTS "+ TBL_MENU_ITEM;
	String DROP_TBL_ORDER = "DROP TABLE IF EXISTS "+ TBL_ORDER;
	String DROP_TBL_ORDER_ITEM = "DROP TABLE IF EXISTS "+ TBL_ORDER_ITEM;
	String DROP_TBL_TOPPING_ITEM = "DROP TABLE IF EXISTS "+ TBL_TOPPING_ITEM;
	String DROP_TBL_TOPPING_GROUP = "DROP TABLE IF EXISTS "+ TBL_TOPPING_GROUP;
	String DROP_TBL_MENU_ITEM_TOPPING = "DROP TABLE IF EXISTS "+ TBL_MENU_ITEM_TOPPING;
}
