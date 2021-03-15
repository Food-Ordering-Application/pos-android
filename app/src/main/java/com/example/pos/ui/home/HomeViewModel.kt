package com.example.pos.ui.home

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Website
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pos.adapter.FunctionItem


class HomeViewModel : ViewModel() {

    private val _funcListData = MutableLiveData<MutableList<FunctionItem>>().apply {
        value = FunctionItem.createFunctionList()
    }
    val funcListData: LiveData<MutableList<FunctionItem>> = _funcListData
}