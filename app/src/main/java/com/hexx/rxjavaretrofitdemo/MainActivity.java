package com.hexx.rxjavaretrofitdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hexx.rxjavaretrofitdemo.bean.Top250Bean;
import com.hexx.rxjavaretrofitdemo.bean.USBoxBean;
import com.hexx.rxjavaretrofitdemo.retrofit.RetrofitHelper;
import com.hexx.rxjavaretrofitdemo.retrofit.ServiceApi;
import com.hexx.rxjavaretrofitdemo.view.Top250Adapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.top250recyclerView)
    RecyclerView top250recyclerView;
    @BindView(R.id.top250refreshLayout)
    SmartRefreshLayout top250refreshLayout;
    Top250Adapter top250Adapter;
    List<Top250Bean.SubjectsBean> top250Data = new ArrayList<>();
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
        //结合rxjava
        ServiceApi service = RetrofitHelper.getService().create(ServiceApi.class);
//        service.getTop250(start, count)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Result<Top250Bean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(Result<Top250Bean> value) {
//                        top250Data.addAll(value.response().body().getSubjects());
//                        if (value.response().body().getStart() < (value.response().body().getTotal() / value.response().body().getCount())) {
//                            start++;
//                            canLoadMore = true;
//                        } else {
//                            canLoadMore = false;
//                        }
//
//                        if (refreshlayout != null) {
//                            if (refreshType == REFRESH) {
//                                refreshlayout.finishRefresh();
//                            } else if (refreshType == LOAD_MORE) {
//                                refreshlayout.finishLoadmore();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        top250Adapter.notifyDataSetChanged();
//                    }
//                });

        Observable top250Observable = service.getTop250(start, count);
        Observable usboxObservable = service.getWeekly(start, count);

        Observable.merge(top250Observable, usboxObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {
                        if (value instanceof Top250Bean) {
                            top250Data.addAll(((Top250Bean) value).getSubjects());
                            if (((Top250Bean) value).getStart() < ((Top250Bean) value).getTotal() / ((Top250Bean) value).getCount()) {
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
                        } else if (value instanceof USBoxBean) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            alertDialogBuilder.setTitle(((USBoxBean) value).getTitle()).setMessage(((USBoxBean) value).getDate())
                                    .setPositiveButton("确定", null)
                                    .setNegativeButton("取消", null).show();
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
