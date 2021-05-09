package com.foa.pos.network;

import com.foa.pos.network.entity.Login;
import com.foa.pos.network.entity.NewOrder;
import com.foa.pos.network.entity.SendOrderItem;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.OrderData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.network.response.VerifyAppResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AppService {


    @POST("/user/pos/login")
    Call<ResponseAdapter<LoginData>> login (
            @Body Login login
    );

    @FormUrlEncoded
    @POST("/user/pos/verify-app-key")
    Call<VerifyAppResponse> verifyAppKey (
            @Field("posAppKey") String posAppKey,
            @Field("deviceId") String deviceId
    );

    @POST("/order")
    Call<ResponseAdapter<OrderData>> createOrderAndAddFirstOrderItem (
            @Body NewOrder newOrder
    );

    @FormUrlEncoded
    @POST("/add-new-item")
    Call<OrderData> addOrderItem (
            @Field("sendItem") SendOrderItem sendItem,
            @Field("orderId") String orderId
    );

    @FormUrlEncoded
    @POST("/reduce-orditem-quantity")
    Call<OrderData> reduceOrderItemQuantity (
            @Field("orderItemId") String orderItemId
    );

    @FormUrlEncoded
    @POST("/increase-orditem-quantity")
    Call<OrderData> increaseOrderItemQuantity (
            @Field("orderItemId") String orderItemId
    );
    @FormUrlEncoded
    @POST("/update-orditem-quantity")
    Call<ResponseAdapter<OrderData>> updateOrderItemQuantity (
            @Field("orderItemId") String orderItemId,
            @Field("quantity") int quantity
    );

    @FormUrlEncoded
    @POST("/remove-orditem")
    Call<OrderData> removeOrderItem (
            @Field("orderItemId") String orderItemId
    );
}
