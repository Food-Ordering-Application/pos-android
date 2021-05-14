package com.foa.pos.service;

import android.util.Log;

import com.foa.pos.model.IDataResultCallback;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.entity.NewOrderBody;
import com.foa.pos.network.entity.SendOrderItem;
import com.foa.pos.network.response.OrderData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.utils.OrderSession;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {

    public void createOrderAndFirstOrderItemOnline(OrderItem orderItem, IDataResultCallback<Order> resultCallback) {
        final SendOrderItem sendOrderItem = orderItem.createSendOrderItem();
        Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
                .createOrderAndAddFirstOrderItem(
                        new NewOrderBody(sendOrderItem, Helper.read(Constants.RESTAURANT_ID),
                                "", LoginSession.getInstance().getStaff().getId()));
        responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {
                if (response.errorBody() != null) {
                    try {
                        Log.e("[OrderFragment][Api error]", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.code() == Constants.STATUS_CODE_CREATED) {

                    ResponseAdapter<OrderData> res = response.body();
                    assert res != null;
                    if (res.getStatus() == Constants.STATUS_CODE_CREATED) {
                        resultCallback.onSuccess(true, res.getData().getOrder());
                    } else {
                        Log.e("[Order fragment]", "Create order fail");
                    }
                } else {
                    resultCallback.onSuccess(false, null);
                    Log.e("[Order fragment]", "Create order fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
                Log.e("Login Error", t.getMessage());
            }
        });
    }

    public void addOrderItemOnline(OrderItem orderItem, IDataResultCallback<Order> resultCallback) {

        final SendOrderItem sendOrderItem = orderItem.createSendOrderItem();
        Call<ResponseAdapter<OrderData>> responseCall = RetrofitClient.getInstance().getAppService()
                .addOrderItem(OrderSession.getInstance().getId(),sendOrderItem);
        responseCall.enqueue(new Callback<ResponseAdapter<OrderData>>() {
            @Override
            public void onResponse(Call<ResponseAdapter<OrderData>> call, Response<ResponseAdapter<OrderData>> response) {
                if (response.errorBody() != null) {
                    try {
                        Log.e("[OrderFragment][Api error]", response.errorBody().string());
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
                        Log.e("[Order fragment]", "Create order fail");
                    }
                } else {
                    resultCallback.onSuccess(false, null);
                    Log.e("[Order fragment]", "Create order fail");
                }

            }

            @Override
            public void onFailure(Call<ResponseAdapter<OrderData>> call, Throwable t) {
                Log.e("Login Error", t.getMessage());
            }
        });
    }

}
