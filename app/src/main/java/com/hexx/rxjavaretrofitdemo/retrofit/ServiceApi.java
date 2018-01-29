package com.hexx.rxjavaretrofitdemo.retrofit;


import com.hexx.rxjavaretrofitdemo.bean.Top250Bean;
import com.hexx.rxjavaretrofitdemo.bean.USBoxBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HE on 2018/1/24.
 */

public interface ServiceApi {
    String BaseUrl = "http://api.douban.com/";

    @GET("/v2/movie/top250")
    Observable<Top250Bean> getTop250(@Query("start") int start, @Query("count") int count);

    @GET("/v2/movie/us_box")
    Observable<USBoxBean> getWeekly(@Query("start") int start, @Query("count") int count);

}
