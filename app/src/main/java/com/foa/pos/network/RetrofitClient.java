package com.foa.pos.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  Retrofit retrofit;
    private  static RetrofitClient instance;
    private static final String BASE_URL = "https://api-gateway-pos.herokuapp.com/";
    private  RetrofitClient() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if(instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public AppService getAppService() {
        return retrofit.create(AppService.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
