package com.example.hei_sir.link;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class QaActivity extends AppCompatActivity {

    private List<Qa> qaList = new ArrayList<>();
    private QaAdapter adapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        initQas();      //初始化信息栏
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);       //一行只显示一个数据
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QaAdapter(qaList) {
            @Override
            public int getItemCount() {
                return 2;
            }        //sql语句，返回查询的行数，实现数目统一
        };
        recyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        navView.setCheckedItem(R.id.nav_info);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                ReflashQa();
            }
        });
    }

    private void initQas() {               //初始化信息栏
        for (int i = 0; i < 2; i++) {
            Qa q1 = new Qa("张三","张老师",sdf.format(new Date()),R.mipmap.ic_launcher,"今天作业是什么？22222222222222222222222222222222222222222222222222222222222222222","");
            qaList.add(q1);
            Qa q2 = new Qa("李四","张老师",sdf.format(new Date()),R.mipmap.ic_launcher,"今天作业是什么？22222222222222222222222222222222222222222222222222222222222222222","");
            qaList.add(q2);
        }
    }

    private void ReflashQa(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initQas();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}

