package com.hexx.rxjavaretrofitdemo.retrofit;


import com.hexx.rxjavaretrofitdemo.bean.BookResultBean;
import com.hexx.rxjavaretrofitdemo.bean.Top250Bean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HE on 2018/1/24.
 */

public interface ServiceApi {
    String BaseUrl = "https://api.douban.com/";

    @GET("/v2/movie/top250")
    Observable<Result<Top250Bean>> getTop250(@Query("start") int start, @Query("count") int count);

    @GET("/v2/book/search")
    Observable<Result<BookResultBean>> getBookResult(@Query("q") String q, @Query("start") int start, @Query("count") int count);
}
