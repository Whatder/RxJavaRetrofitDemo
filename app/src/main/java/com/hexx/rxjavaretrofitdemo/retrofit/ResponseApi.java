package com.hexx.rxjavaretrofitdemo.retrofit;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by HE on 2018/1/24.
 */

public interface ResponseApi {
    @GET("{user}")
    Call<ResponseBody> getTestData(@Path("user") String user);
}
