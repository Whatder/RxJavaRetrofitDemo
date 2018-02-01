package com.hexx.rxjavaretrofitdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hexx.rxjavaretrofitdemo.view.Top250Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.pub_toolbar)
    Toolbar pubToolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ivMenuIcon)
    ImageView ivMenuIcon;
    @BindView(R.id.lvMenuList)
    ListView lvMenuList;
    @BindView(R.id.mainPanel)
    FrameLayout mainPanel;
    private ActionBarDrawerToggle mToggle;

    Top250Fragment top250Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(pubToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, pubToolbar, R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();
        drawerLayout.setDrawerListener(mToggle);
        tvToolbarTitle.setText("电影");
        top250Fragment = new Top250Fragment();

        initView();
    }

    private void initView() {
        String[] items = {"电影", "图书", "音乐"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvMenuList.setAdapter(adapter);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainPanel, top250Fragment);
        transaction.commit();
    }
}
