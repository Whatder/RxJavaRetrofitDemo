package com.hexx.rxjavaretrofitdemo.retrofit;


import com.hexx.rxjavaretrofitdemo.bean.DataBean;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HE on 2018/1/24.
 */

public interface ResponseApi {
    String BaseUrl = "http://api.douban.com/";

    @GET("/v2/movie/top250")
    Observable<Result<DataBean>> getTop250(@Query("start") int start, @Query("count") int count);
}
