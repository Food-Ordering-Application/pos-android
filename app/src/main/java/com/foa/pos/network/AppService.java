package com.foa.pos.network;

import com.foa.pos.model.Menu;
import com.foa.pos.model.MenuGroup;
import com.foa.pos.model.MenuItem;
import com.foa.pos.model.MenuItemTopping;
import com.foa.pos.model.Order;
import com.foa.pos.model.ToppingGroup;
import com.foa.pos.model.ToppingItem;
import com.foa.pos.network.entity.AddNewOrderItemBody;
import com.foa.pos.network.entity.LoginBody;
import com.foa.pos.network.entity.NewOrderBody;
import com.foa.pos.network.entity.RemoveOrderItemBody;
import com.foa.pos.network.entity.UpdateQuantityBody;
import com.foa.pos.network.response.LoginData;
import com.foa.pos.network.response.MenuData;
import com.foa.pos.network.response.OrderData;
import com.foa.pos.network.response.ResponseAdapter;
import com.foa.pos.network.response.RestaurantServiceData;
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
}
