package com.hexx.rxjavaretrofitdemo.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HE on 2018/1/29.
 */

public class RetrofitHelper {
    public static Retrofit getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ResponseApi.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
