package com.foa.pos.network;

import com.foa.pos.network.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AppService {

    @FormUrlEncoded
    @POST("/user/customer/login")
    Call<LoginResponse> login (
            @Field("phoneNumber") String username,
            @Field("password") String password
    );


}
