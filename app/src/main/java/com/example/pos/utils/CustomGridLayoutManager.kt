package com.example.pos.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager


class CustomGridLayoutManager(context: Context?, span:Int) : GridLayoutManager(context,span) {
    private var isScrollEnabled = false
    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically()
    }
}