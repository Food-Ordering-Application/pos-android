package com.foa.smartpos.api;

import com.foa.smartpos.model.IDataResultCallback;
import com.foa.smartpos.model.IResultCallback;
import com.foa.smartpos.model.Order;
import com.foa.smartpos.network.RetrofitClient;
import com.foa.smartpos.network.entity.VoidOrderBody;
import com.foa.smartpos.network.response.OrderData;
import com.foa.smartpos.network.response.OrderListData;
import com.foa.smartpos.network.response.ResponseAdapter;
import com.foa.smartpos.utils.Constants;
import com.foa.smartpos.utils.Helper;
import com.foa.smartpos.utils.LoggerHelper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {

    public static void syncOrder(Order order, IResultCallback resultCallback) {
        Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
                .syncOrder(new OrderData(order));
        responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<OrderData> res = response.body();
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
            public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }

    public static void getOrderById(String orderId, IDataResultCallback<Order> resultCallback) {
        Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getOrderById(orderId);
        responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<OrderData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getOrder());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
            }
        });
    }

    public static void getAllOrder(String orderType, int pageNumber,IDataResultCallback<List<Order>> resultCallback) {
        OrderService.getAllOrder(orderType,pageNumber,null, resultCallback);
    }


        public static void getAllOrder(String orderType, int pageNumber, String orderStatus,IDataResultCallback<List<Order>> resultCallback) {
        String restaurantId = Helper.read(Constants.RESTAURANT_ID);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE,-1);
        String strStartDate = Helper.dateSQLiteFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE,2);
        String strEndDate = Helper.dateSQLiteFormat.format(calendar.getTime());
        Call<ResponseAdapter<OrderListData>> responseCall = RetrofitClient.getInstance().getAppService()
                .getAllOrder(restaurantId,orderType,pageNumber, orderStatus, strStartDate,strEndDate);
        responseCall.enqueue(new Callback<ResponseAdapter<OrderListData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderListData>> call, Response<ResponseAdapter<OrderListData>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_SUCCESS) {
                    ResponseAdapter<OrderListData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_SUCCESS) {
                        resultCallback.onSuccess(true, res.getData().getOrders());
                    } else {
                        resultCallback.onSuccess(false,null);
                    }
                } else {
                    resultCallback.onSuccess(false,null);
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderListData>> call, Throwable t) {
                LoggerHelper.CheckAndLogInfo(this,t.getMessage());
                resultCallback.onSuccess(false,null);
            }
        });
    }


    public static void confirmOrder(String orderId, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .confirmOrder(orderId);
        responseCall.enqueue(new Callback<ResponseAdapter<String>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<String>>call, Response<ResponseAdapter<String>> response) {
                if (response.errorBody() != null) {
                    try {
                        LoggerHelper.CheckAndLogInfo(this,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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


    public static void voidOrder(String orderId,List<String> orderItemIds,String cashierNote, IResultCallback resultCallback) {
        Call<ResponseAdapter<String>> responseCall = RetrofitClient.getInstance().getAppService()
                .voidOrder(orderId, new VoidOrderBody(orderItemIds,cashierNote));
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

}
