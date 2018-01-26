package com.hexx.rxjavaretrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hexx.rxjavaretrofitdemo.bean.DataBean;
import com.hexx.rxjavaretrofitdemo.retrofit.ResponseApi;
import com.hexx.rxjavaretrofitdemo.view.Top250Adapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.top250recyclerView)
    RecyclerView top250recyclerView;
    @BindView(R.id.top250refreshLayout)
    SmartRefreshLayout top250refreshLayout;
    Top250Adapter top250Adapter;
    List<DataBean.SubjectsBean> top250Data = new ArrayList<>();
    private int start, count = 20;
    private boolean canLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        getData();
        initRefresh();
    }

    private void initView() {
        top250Adapter = new Top250Adapter(this, top250Data);
        top250recyclerView.setLayoutManager(new LinearLayoutManager(this));
        top250recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        top250recyclerView.setAdapter(top250Adapter);
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
        Call<DataBean> call1 = responseApi.getTop250(start, count);
        call1.enqueue(new Callback<DataBean>() {
            @Override
            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
                top250Data.addAll(response.body().getSubjects());
                top250Adapter.notifyDataSetChanged();

                if (response.body().getStart() < (response.body().getTotal() / response.body().getCount())) {
                    start++;
                    canLoadMore = true;
                } else {
                    canLoadMore = false;
                }
            }

            @Override
            public void onFailure(Call<DataBean> call, Throwable t) {
            }
        });
    }

    private void initRefresh() {
        top250refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                start = 0;
                top250Data.clear();
                getData();
                refreshlayout.finishRefresh(2000);
            }
        });
        top250refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (canLoadMore) {
                    getData();
                    refreshlayout.finishLoadmore(2000);
                }
            }
        });
    }
}
