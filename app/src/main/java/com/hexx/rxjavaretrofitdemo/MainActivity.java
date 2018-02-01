package com.hexx.rxjavaretrofitdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hexx.rxjavaretrofitdemo.utils.ToastUtil;
import com.hexx.rxjavaretrofitdemo.view.BookFragment;
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
    @BindView(R.id.mainPanel)
    FrameLayout mainPanel;
    @BindView(R.id.nav_menu)
    NavigationView navMenu;
    private ActionBarDrawerToggle mToggle;
    Top250Fragment top250Fragment;
    BookFragment bookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //toolBar初始化
        setSupportActionBar(pubToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, pubToolbar, R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();
        drawerLayout.setDrawerListener(mToggle);
        //fragment
        top250Fragment = new Top250Fragment();
        bookFragment = new BookFragment();
        initView();
    }

    private void initView() {
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_movie:
                        setTop250Fragment();
                        break;
                    case R.id.item_book:
                        setBookFragment();
                        break;
                    case R.id.item_music:
                        break;
                    case R.id.item_about:
                        ToastUtil.show(MainActivity.this, "About");
                        break;

                }
                if (item.getItemId() != R.id.item_about) {
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });

        setTop250Fragment();
    }


    void setTop250Fragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        tvToolbarTitle.setText("Movie");
        transaction.replace(R.id.mainPanel, top250Fragment);
        transaction.commit();
    }

    void setBookFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        tvToolbarTitle.setText("Book");
        transaction.replace(R.id.mainPanel, bookFragment);
        transaction.commit();
    }


}
