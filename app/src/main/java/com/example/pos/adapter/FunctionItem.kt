package com.example.pos.adapter

import com.example.pos.R


class FunctionItem(val id:Int,val name:String, val icon:Int) {

    companion object {
        fun createFunctionList(): MutableList<FunctionItem> {
            val funcList: MutableList<FunctionItem> = mutableListOf()
            funcList.add(FunctionItem(1,"Thực đơn", R.drawable.ic_list_menu))
            funcList.add(FunctionItem(2,"Đơn hàng", R.drawable.ic_list_order))
            funcList.add(FunctionItem(3,"Quản lý bàn", R.drawable.ic_list_menu))
            funcList.add(FunctionItem(4,"Thanh toán", R.drawable.ic_list_order))
            funcList.add(FunctionItem(5,"Nhân viên", R.drawable.ic_list_menu))
//            funcList.add(FunctionItem(1,"Thực đơn", R.drawable.ic_list_menu))
//            funcList.add(FunctionItem(2,"Đơn hàng", R.drawable.ic_list_order))
//            funcList.add(FunctionItem(3,"Quản lý bàn", R.drawable.ic_list_menu))
//            funcList.add(FunctionItem(4,"Thanh toán", R.drawable.ic_list_order))
//            funcList.add(FunctionItem(5,"Nhân viên", R.drawable.ic_list_menu))

            return funcList
        }
    }
}