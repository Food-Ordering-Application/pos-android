package com.foa.pos.service;

import com.foa.pos.model.IDataResultCallback;
import com.foa.pos.model.Menu;
import com.foa.pos.model.MenuGroup;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.MenuItemTopping;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.model.ToppingGroup;
import com.foa.pos.model.ToppingItem;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.entity.NewOrderBody;
import com.foa.pos.network.entity.SendOrderItem;
import com.foa.pos.network.response.MenuData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.network.response.RestaurantServiceData;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoggerHelper;
import com.foa.pos.utils.LoginSession;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantService {

    public void getMenuId(IDataResultCallback<MenuData> resultCallback) {
        Call<ResponseAdapter<MenuData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getMenuId();
        responseCall.enqueue(new Callback<ResponseAdapter<MenuData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<MenuData>> call, Response<ResponseAdapter<MenuData>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {

                    ResponseAdapter<MenuData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<MenuData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }
    public void getMenuItems(String menuId, IDataResultCallback<List<MenuItem>> resultCallback) {
        Call<ResponseAdapter<RestaurantServiceData<MenuItem>>> responseCall = RetrofitClient.getInstance().getAppService()
                .getMenuItems(menuId);
        responseCall.enqueue(new Callback<ResponseAdapter<RestaurantServiceData<MenuItem>>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<RestaurantServiceData<MenuItem>>> call, Response<ResponseAdapter<RestaurantServiceData<MenuItem>>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {

                    ResponseAdapter<RestaurantServiceData<MenuItem>> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getDataList());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<RestaurantServiceData<MenuItem>>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }

    public void getMenuGroups(String menuId, IDataResultCallback<List<MenuGroup>> resultCallback) {
        Call<ResponseAdapter<RestaurantServiceData<MenuGroup>>> responseCall = RetrofitClient.getInstance().getAppService()
                .getMenuGroups(menuId);
        responseCall.enqueue(new Callback<ResponseAdapter<RestaurantServiceData<MenuGroup>>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<RestaurantServiceData<MenuGroup>>> call, Response<ResponseAdapter<RestaurantServiceData<MenuGroup>>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {

                    ResponseAdapter<RestaurantServiceData<MenuGroup>> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getDataList());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<RestaurantServiceData<MenuGroup>>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }

    public void getToppingItems(String menuId, IDataResultCallback<List<ToppingItem>> resultCallback) {
        Call<ResponseAdapter<RestaurantServiceData<ToppingItem>>> responseCall = RetrofitClient.getInstance().getAppService()
                .getToppingItems(menuId);
        responseCall.enqueue(new Callback<ResponseAdapter<RestaurantServiceData<ToppingItem>>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<RestaurantServiceData<ToppingItem>>> call, Response<ResponseAdapter<RestaurantServiceData<ToppingItem>>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {

                    ResponseAdapter<RestaurantServiceData<ToppingItem>> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getDataList());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<RestaurantServiceData<ToppingItem>>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }

    public void getToppingGroups(String menuId, IDataResultCallback<List<ToppingGroup>> resultCallback) {
        Call<ResponseAdapter<RestaurantServiceData<ToppingGroup>>> responseCall = RetrofitClient.getInstance().getAppService()
                .getToppingGroups(menuId);
        responseCall.enqueue(new Callback<ResponseAdapter<RestaurantServiceData<ToppingGroup>>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<RestaurantServiceData<ToppingGroup>>> call, Response<ResponseAdapter<RestaurantServiceData<ToppingGroup>>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {

                    ResponseAdapter<RestaurantServiceData<ToppingGroup>> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getDataList());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<RestaurantServiceData<ToppingGroup>>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }

    public void getMenuItemTopping(String menuId, IDataResultCallback<List<MenuItemTopping>> resultCallback) {
        Call<ResponseAdapter<RestaurantServiceData<MenuItemTopping>>> responseCall = RetrofitClient.getInstance().getAppService()
                .getMenuItemsTopping(menuId);
        responseCall.enqueue(new Callback<ResponseAdapter<RestaurantServiceData<MenuItemTopping>>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<RestaurantServiceData<MenuItemTopping>>> call, Response<ResponseAdapter<RestaurantServiceData<MenuItemTopping>>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {

                    ResponseAdapter<RestaurantServiceData<MenuItemTopping>> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getDataList());
                    } else {
                        LoggerHelper.CheckAndLogInfo(this,res.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<RestaurantServiceData<MenuItemTopping>>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }
}
