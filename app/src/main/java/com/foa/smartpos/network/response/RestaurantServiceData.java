package com.foa.smartpos.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantServiceData<T> {
    @SerializedName("results")
    private List<T> dataList;

    public RestaurantServiceData(List<T> dataList) {
        this.dataList = dataList;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
