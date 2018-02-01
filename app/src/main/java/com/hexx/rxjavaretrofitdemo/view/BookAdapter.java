package com.hexx.rxjavaretrofitdemo.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hexx.rxjavaretrofitdemo.R;
import com.hexx.rxjavaretrofitdemo.bean.BookResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 2018/2/1.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context mContext;
    private List<BookResultBean.BooksBean> data;

    public BookAdapter(Context mContext, List<BookResultBean.BooksBean> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_book, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Glide.with(mContext).load(data.get(position).getImages().getLarge()).into(holder.iconBook);
        holder.summaryBook.setText(data.get(position).getTitle() + "\n" + data.get(position).getPublisher());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_book)
        ImageView iconBook;
        @BindView(R.id.summary_book)
        TextView summaryBook;
        @BindView(R.id.card_book)
        CardView cardBook;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
