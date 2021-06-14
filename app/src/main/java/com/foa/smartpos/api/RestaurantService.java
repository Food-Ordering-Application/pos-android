package com.foa.smartpos.api;

import com.foa.smartpos.model.IDataResultCallback;
import com.foa.smartpos.model.IResultCallback;
import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.MenuItemTopping;
import com.foa.smartpos.model.ToppingGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.model.enums.StockState;
import com.foa.smartpos.network.RetrofitClient;
import com.foa.smartpos.network.entity.UpdateStockStateBody;
import com.foa.smartpos.network.response.AutoConfirmData;
import com.foa.smartpos.network.response.MenuData;
import com.foa.smartpos.network.response.ResponseAdapter;
import com.foa.smartpos.network.response.RestaurantServiceData;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.utils.LoggerHelper;

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
                    }
                }else {
                    resultCallback.onSuccess(false, null);
                }


            }

            @Override
            public void onFailure(Call<ResponseAdapter<MenuData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(false, null);
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
                    }
                }else{
                    resultCallback.onSuccess(true, null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<RestaurantServiceData<MenuItem>>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(true, null);
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
                resultCallback.onSuccess(false, null);
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
                resultCallback.onSuccess(false, null);
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
                resultCallback.onSuccess(false, null);
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
                resultCallback.onSuccess(false, null);
            }
        });
    }

    public static void updateMenuItemStockState(String menuItemId, StockState stockState, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .updateMenuItem(menuItemId, new UpdateStockStateBody(stockState));
        responseCall.enqueue(new Callback<ResponseAdapter<String>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<String>>call, Response<ResponseAdapter<String>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<String> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true);
                    } else {
                        resultCallback.onSuccess(false);
                    }
                } else {
                    resultCallback.onSuccess(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<String>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(false);
            }
        });
    }

    public static void updateToppingItemStockState(String toppingItemId, StockState stockState, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .updateToppingItem(toppingItemId, new UpdateStockStateBody(stockState));
        responseCall.enqueue(new Callback<ResponseAdapter<String>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<String>>call, Response<ResponseAdapter<String>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<String> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true);
                    } else {
                        resultCallback.onSuccess(false);
                    }
                } else {
                    resultCallback.onSuccess(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<String>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(false);
            }
        });
    }

    public static void getAutoConfirm(IDataResultCallback<AutoConfirmData> resultCallback) {
        Call<ResponseAdapter<AutoConfirmData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getAutoConfirm();
        responseCall.enqueue(new Callback<ResponseAdapter<AutoConfirmData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<AutoConfirmData>>call, Response<ResponseAdapter<AutoConfirmData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<AutoConfirmData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<AutoConfirmData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(false,null);
            }
        });
    }

    public static void updateIsAutoConfirm(boolean isAutoConfirm, IDataResultCallback<AutoConfirmData> resultCallback) {
        String restaurantId = Helper.read(Constants.RESTAURANT_ID);
        Call<ResponseAdapter<AutoConfirmData>> responseCall = RetrofitClient.getInstance().getAppService()
                .updateAutoConfirm(isAutoConfirm,restaurantId);
        responseCall.enqueue(new Callback<ResponseAdapter<AutoConfirmData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<AutoConfirmData>>call, Response<ResponseAdapter<AutoConfirmData>> response) {
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<AutoConfirmData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true,res.getData());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<AutoConfirmData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(false,null);
            }
        });
    }
}
