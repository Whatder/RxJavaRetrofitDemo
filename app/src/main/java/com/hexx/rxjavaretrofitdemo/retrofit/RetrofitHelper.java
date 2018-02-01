package com.hexx.rxjavaretrofitdemo.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HE on 2018/1/29.
 */

public class RetrofitHelper {
    public static ServiceApi getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceApi.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ServiceApi service = retrofit.create(ServiceApi.class);
        return service;
    }
}
