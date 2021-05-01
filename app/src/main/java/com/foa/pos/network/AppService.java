package com.foa.pos.network;

import com.foa.pos.model.Order;
import com.foa.pos.model.OrderItem;
import com.foa.pos.network.entity.MenuItemTopping;
import com.foa.pos.network.response.LoginResponse;
import com.foa.pos.network.response.VerifyAppResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AppService {

    @FormUrlEncoded
    @POST("/user/pos/login")
    Call<LoginResponse> login (
            @Field("userName") String username,
            @Field("password") String password,
            @Field("restaurantId") String restaurantId
    );

    @FormUrlEncoded
    @POST("/user/pos/verifi-app-key")
    Call<VerifyAppResponse> verifyAppKey (
            @Field("posAppKey") String posAppKey
    );

    @FormUrlEncoded
    @POST("/order")
    Call<Order> createOrder (
            @Field("orderItem") List<OrderItem> orderItem,
            @Field("restaurantId") String restaurantId,
            @Field("customerId") String customerId
    );

    @FormUrlEncoded
    @POST("/add-new-item")
    Call<Order> addOrderItem (
            @Field("menuItemId") String menuItemId,
            @Field("price") long price,
            @Field("quantity") int quantity,
            @Field("orderItemToppings") List<MenuItemTopping> orderItemToppings
    );

    @FormUrlEncoded
    @POST("/reduce-orditem-quantity")
    Call<Order> reduceOrderItemQuantity (
            @Field("orderItemId") String orderItemId
    );

    @FormUrlEncoded
    @POST("/increase-orditem-quantity")
    Call<Order> increaseOrderItemQuantity (
            @Field("orderItemId") String orderItemId
    );
}
