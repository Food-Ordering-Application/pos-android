package com.foa.pos.network;

import com.foa.pos.network.entity.LoginBody;
import com.foa.pos.network.entity.NewOrderBody;
import com.foa.pos.network.entity.SendOrderItem;
import com.foa.pos.network.entity.UpdateQuantityBody;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.MenuData;
import com.foa.pos.network.response.OrderData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.network.response.ToppingGroupData;
import com.foa.pos.network.response.VerifyAppResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppService {


    @POST("/user/pos/login")
    Call<ResponseAdapter<LoginData>> login (
            @Body LoginBody login
    );

    @FormUrlEncoded
    @POST("/user/pos/verify-app-key")
    Call<VerifyAppResponse> verifyAppKey (
            @Field("posAppKey") String posAppKey,
            @Field("deviceId") String deviceId
    );

    @POST("/order")
    Call<ResponseAdapter<OrderData>> createOrderAndAddFirstOrderItem (
            @Body NewOrderBody newOrder
    );


    @PATCH("/order/{orderId}/add-new-item")
    Call<ResponseAdapter<OrderData>> addOrderItem (
            @Path("orderId") String orderId,
            @Body SendOrderItem sendItem
    );


    @PATCH("/order/{orderId}/update-orditem-quantity")
    Call<ResponseAdapter<OrderData>> updateOrderItemQuantity (
            @Path("orderId") String orderId,
            @Body UpdateQuantityBody body
    );

    @FormUrlEncoded
    @PATCH("/remove-orditem")
    Call<ResponseAdapter<OrderData>> removeOrderItem (
            @Field("orderItemId") String orderItemId
    );


    @GET("/restaurant/{restaurantId}/get-menu-information")
    Call<ResponseAdapter<MenuData>> getMenuByRestaurantId (
            @Path("restaurantId") String restaurantId
    );

    @FormUrlEncoded
    @POST("/restaurant/get-menu-item-topping-info")
    Call<ResponseAdapter<ToppingGroupData>> getToppingsByMenuItemId (
            @Field("menuItemId") String menuItemId
    );
}
