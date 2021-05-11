package com.foa.pos.network;

import com.foa.pos.utils.Constants;
import com.foa.pos.utils.EnumRetrofitConverterFactory;
import com.foa.pos.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  Retrofit retrofit;
    private  static RetrofitClient instance;
    private static final String BASE_URL = "https://apigway.herokuapp.com/";

    private  RetrofitClient() {

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private  RetrofitClient(String accessToken) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Helper.initialize(Helper.getContext());
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", accessToken).build();
            return chain.proceed(request);
        });
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(new EnumRetrofitConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
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

    public void setAuthorizationHeader(String accessToken){
        instance  = new RetrofitClient(accessToken);
    }

}
