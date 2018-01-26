package com.hexx.rxjavaretrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.hexx.rxjavaretrofitdemo.bean.DataBean;
import com.hexx.rxjavaretrofitdemo.retrofit.ResponseApi;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvResponse)
    TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getData();
    }


    private void getData() {
        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ResponseApi.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //拿到代理对象
        ResponseApi responseApi = retrofit.create(ResponseApi.class);

        //调用接口-Gson解析
        Call<DataBean> call1 = responseApi.getTop250(1, 5);
        call1.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                tvResponse.setText(response.body().getTitle()
                        + "页数:" + response.body().getStart() + "/" + response.body().getTotal() + "页");
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
                tvResponse.setText(t.getMessage());
            }
        });
    }

}
