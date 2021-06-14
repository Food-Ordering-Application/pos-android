package com.foa.smartpos.network;

import com.foa.smartpos.model.MenuGroup;
import com.foa.smartpos.model.MenuItem;
import com.foa.smartpos.model.MenuItemTopping;
import com.foa.smartpos.model.ToppingGroup;
import com.foa.smartpos.model.ToppingItem;
import com.foa.smartpos.network.entity.LoginBody;
import com.foa.smartpos.network.entity.UpdateStockStateBody;
import com.foa.smartpos.network.entity.VoidOrderBody;
import com.foa.smartpos.network.response.AutoConfirmData;
import com.foa.smartpos.network.response.LoginData;
import com.foa.smartpos.network.response.MenuData;
import com.foa.smartpos.network.response.OrderData;
import com.foa.smartpos.network.response.OrderListData;
import com.foa.smartpos.network.response.ResponseAdapter;
import com.foa.smartpos.network.response.RestaurantServiceData;
import com.foa.smartpos.network.response.VerifyAppResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/user/pos/menu")
    Call<ResponseAdapter<MenuData>> getMenuId (

    );
    @GET("/user/pos/menu/{menuId}/menu-item")
    Call<ResponseAdapter<RestaurantServiceData<MenuItem>>> getMenuItems (
            @Path("menuId") String menuId
    );
    @GET("/user/pos/menu/{menuId}/menu-group")
    Call<ResponseAdapter<RestaurantServiceData<MenuGroup>>> getMenuGroups (
            @Path("menuId") String menuId
    );
    @GET("/user/pos/menu/{menuId}/topping-item")
    Call<ResponseAdapter<RestaurantServiceData<ToppingItem>>> getToppingItems(
            @Path("menuId") String menuId
    );
    @GET("/user/pos/menu/{menuId}/topping-group")
    Call<ResponseAdapter<RestaurantServiceData<ToppingGroup>>> getToppingGroups(
            @Path("menuId") String menuId
    );
    @GET("/user/pos/menu/{menuId}/menu-item-topping")
    Call<ResponseAdapter<RestaurantServiceData<MenuItemTopping>>> getMenuItemsTopping(
            @Path("menuId") String menuId
    );

    @POST("/user/pos/order/save-order")
    Call<ResponseAdapter<OrderData>> syncOrder(
            @Body OrderData order
    );


    @GET("/order/{orderId}")
    Call<ResponseAdapter<OrderData>> getOrderById(
            @Path("orderId") String orderId
    );


    @GET("/order/get-all-restaurant-orders")
    Call<ResponseAdapter<OrderListData>> getAllOrder(
            @Query("restaurantId") String restaurantId,
            @Query("query") String orderType,
            @Query("pageNumber") int pageNumber,
            @Query("orderStatus") String orderStatus,
            @Query("start") String startDate,
            @Query("end") String endDate
    );

    @POST("/user/pos/order/{orderId}/confirm")
    Call<ResponseAdapter<String>> confirmOrder(
            @Path("orderId") String orderId
    );



    @POST("/user/pos/order/{orderId}/void")
    Call<ResponseAdapter<String>> voidOrder(
            @Path("orderId") String orderId,
            @Body VoidOrderBody voidOrderBody
    );


    @PATCH("/user/pos/menu-item/{menuItemId}")
    Call<ResponseAdapter<String>> updateMenuItem(
            @Path("menuItemId") String menuItemId,
            @Body UpdateStockStateBody body
    );

    @PATCH("/user/pos/topping-item/{toppingItemId}")
    Call<ResponseAdapter<String>> updateToppingItem(
            @Path("toppingItemId") String toppingItemId,
            @Body UpdateStockStateBody body
    );

    @GET("/user/pos/get-isautoconfirm")
    Call<ResponseAdapter<AutoConfirmData>> getAutoConfirm(
    );

    @PATCH("/user/pos/update-isautoconfirm")
    Call<ResponseAdapter<AutoConfirmData>> updateAutoConfirm(
            @Query("orderId") boolean isAutoConfirm,
            @Query("restaurantId") String restaurantId
    );

    @POST("/user/pos/order/{orderId}/finish")
    Call<ResponseAdapter<String>> finnishOrder(
            @Path("orderId") String orderId
    );
}
