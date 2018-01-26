package com.hexx.rxjavaretrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
    private int REFRESH = -1, LOAD_MORE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        getData(null, 0);
        initRefresh();
    }

    private void initView() {
        top250Adapter = new Top250Adapter(this, top250Data);
        top250recyclerView.setLayoutManager(new LinearLayoutManager(this));
        top250recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        top250recyclerView.setAdapter(top250Adapter);
    }

    private void getData(final RefreshLayout refreshlayout, final int refreshType) {
        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ResponseApi.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //拿到代理对象
//        ResponseApi responseApi = retrofit.create(ResponseApi.class);
        //调用接口-Gson解析
//        Call<DataBean> call1 = responseApi.getTop250(start, count);
//        call1.enqueue(new Callback<DataBean>() {
//            @Override
//            public void onResponse(Call<DataBean> call, Response<DataBean> response) {
//                top250Data.addAll(response.body().getSubjects());
//                top250Adapter.notifyDataSetChanged();
//
//                if (response.body().getStart() < (response.body().getTotal() / response.body().getCount())) {
//                    start++;
//                    canLoadMore = true;
//                } else {
//                    canLoadMore = false;
//                }
//
//                if (refreshlayout != null) {
//                    if (refreshType == REFRESH) {
//                        refreshlayout.finishRefresh();
//                    } else if (refreshType == LOAD_MORE) {
//                        refreshlayout.finishLoadmore();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DataBean> call, Throwable t) {
//            }
//        });


        //结合rxjava
        ResponseApi service = retrofit.create(ResponseApi.class);
        service.getTop250(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<DataBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Result<DataBean> value) {
                        top250Data.addAll(value.response().body().getSubjects());
                        if (value.response().body().getStart() < (value.response().body().getTotal() / value.response().body().getCount())) {
                            start++;
                            canLoadMore = true;
                        } else {
                            canLoadMore = false;
                        }

                        if (refreshlayout != null) {
                            if (refreshType == REFRESH) {
                                refreshlayout.finishRefresh();
                            } else if (refreshType == LOAD_MORE) {
                                refreshlayout.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        top250Adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initRefresh() {
        top250refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                start = 0;
                top250Data.clear();
                getData(refreshlayout, REFRESH);
            }
        });
        top250refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (canLoadMore) {
                    getData(refreshlayout, LOAD_MORE);
                }
            }
        });
    }
}
