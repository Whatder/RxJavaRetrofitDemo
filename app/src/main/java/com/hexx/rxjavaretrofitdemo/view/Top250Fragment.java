package com.hexx.rxjavaretrofitdemo.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hexx.rxjavaretrofitdemo.R;
import com.hexx.rxjavaretrofitdemo.bean.Top250Bean;
import com.hexx.rxjavaretrofitdemo.retrofit.RetrofitHelper;
import com.hexx.rxjavaretrofitdemo.retrofit.ServiceApi;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by HE on 2018/2/1.
 */

public class Top250Fragment extends Fragment {
    @BindView(R.id.top250recyclerView)
    RecyclerView top250recyclerView;
    @BindView(R.id.top250refreshLayout)
    SmartRefreshLayout top250refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.top250panel)
    LinearLayout top250panel;
    @BindView(R.id.pub_loading)
    RelativeLayout pubLoading;
    private Activity mActivity;
    private int start, count = 20;
    private boolean canLoadMore = false;
    private int REFRESH = -1, LOAD_MORE = 1;
    private Top250Adapter top250Adapter;
    private List<Top250Bean.SubjectsBean> top250Data = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top250, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        top250Adapter = new Top250Adapter(mActivity, top250Data);
        top250recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        top250recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        top250recyclerView.setAdapter(top250Adapter);
        getData(null, 0);
        initRefresh();
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

    private void getData(final RefreshLayout refreshlayout, final int refreshType) {
        //创建retrofit对象
        //结合rxjava
        ServiceApi service = RetrofitHelper.getService().create(ServiceApi.class);
        service.getTop250(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<Top250Bean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Result<Top250Bean> value) {
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
                        pubLoading.setVisibility(View.GONE);
                        top250panel.setVisibility(View.VISIBLE);
                        top250Adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
