package com.hexx.rxjavaretrofitdemo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hexx.rxjavaretrofitdemo.R;
import com.hexx.rxjavaretrofitdemo.bean.Top250Bean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HE on 2018/1/26.
 */

public class Top250Adapter extends RecyclerView.Adapter<Top250Adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Top250Bean.SubjectsBean> data;

    public Top250Adapter(Context context, List<Top250Bean.SubjectsBean> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_top250, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(data.get(position).getImages().getLarge()).into(holder.icon);
        holder.name.setText(data.get(position).getTitle() + "(" + data.get(position).getOriginal_title() + ")");
        holder.summary.setText(data.get(position).getDirectors().get(0).getName() + " " + data.get(position).getYear());
        holder.star.setText(data.get(position).getRating().getAverage() + "分");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.summary)
        TextView summary;
        @BindView(R.id.star)
        TextView star;
        @BindView(R.id.item)
        LinearLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
