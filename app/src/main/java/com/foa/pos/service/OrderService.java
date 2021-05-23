package com.foa.pos.service;

import android.util.Log;

import com.foa.pos.model.IDataResultCallback;
import com.foa.pos.model.IResultCallback;
import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.network.RetrofitClient;
import com.foa.pos.network.entity.AddNewOrderItemBody;
import com.foa.pos.network.entity.NewOrderBody;
import com.foa.pos.network.entity.SendOrderItem;
import com.foa.pos.network.entity.UpdateQuantityBody;
import com.foa.pos.network.response.OrderData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.utils.Constants;
import com.foa.pos.utils.Helper;
import com.foa.pos.utils.LoggerHelper;
import com.foa.pos.utils.LoginSession;
import com.foa.pos.utils.OrderSession;

import java.io.IOException;

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
}
