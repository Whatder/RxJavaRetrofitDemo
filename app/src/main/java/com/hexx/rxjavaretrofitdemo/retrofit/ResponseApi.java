package com.hexx.rxjavaretrofitdemo.retrofit;


import com.hexx.rxjavaretrofitdemo.bean.DataBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by HE on 2018/1/24.
 */

public interface ResponseApi {
    String BaseUrl = "http://api.douban.com/";

    @GET("/v2/movie/top250")
    Call<DataBean> getTop250(@Query("start") int start, @Query("count") int count);
}
