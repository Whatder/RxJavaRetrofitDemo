package com.hexx.rxjavaretrofitdemo.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hexx.rxjavaretrofitdemo.R;
import com.hexx.rxjavaretrofitdemo.bean.BookResultBean;
import com.hexx.rxjavaretrofitdemo.retrofit.RetrofitHelper;
import com.hexx.rxjavaretrofitdemo.retrofit.ServiceApi;
import com.hexx.rxjavaretrofitdemo.utils.DisplayUtil;
import com.hexx.rxjavaretrofitdemo.utils.ToastUtil;
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

public class BookFragment extends Fragment {
    @BindView(R.id.pub_loading)
    RelativeLayout pubLoading;
    @BindView(R.id.bookRecyclerView)
    RecyclerView bookRecyclerView;
    @BindView(R.id.bookRefreshLayout)
    SmartRefreshLayout bookRefreshLayout;
    @BindView(R.id.bookPanel)
    LinearLayout bookPanel;
    Unbinder unbinder;
    private Activity mActivity;
    private List<BookResultBean.BooksBean> bookList = new ArrayList<>();
    private int REFRESH = -1, LOAD_MORE = 1;
    private boolean canLoadMore = false;
    private BookAdapter adapter;
    private int start=1, count = 20;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BookAdapter(mActivity, bookList);
        bookRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        bookRecyclerView.setAdapter(adapter);
        bookRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = DisplayUtil.dp2px(mActivity, 4);
                outRect.right = DisplayUtil.dp2px(mActivity, 4);
                outRect.left = DisplayUtil.dp2px(mActivity, 4);
                outRect.bottom = DisplayUtil.dp2px(mActivity, 4);
            }
        });
        initRefresh();
        getData(null, 0);
    }

    private void getData(final RefreshLayout refreshlayout, final int refreshType) {
        ServiceApi service = RetrofitHelper.getService();
        service.getBookResult("android", start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<BookResultBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<BookResultBean> result) {
                        if (result.response().body() == null)
                            return;
                        bookList.addAll(result.response().body().getBooks());
                        if (result.response().body().getStart() < (result.response().body().getTotal() / result.response().body().getCount())) {
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
                        ToastUtil.show(mActivity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                        pubLoading.setVisibility(View.GONE);
                        bookPanel.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void initRefresh() {
        bookRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                start = 1;
                bookList.clear();
                getData(refreshlayout, REFRESH);
            }
        });
        bookRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (canLoadMore) {
                    getData(refreshlayout, LOAD_MORE);
                }
            }
        });
    }
}
