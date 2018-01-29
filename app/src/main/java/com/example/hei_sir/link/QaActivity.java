package com.example.hei_sir.link;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class QaActivity extends AppCompatActivity {

    private List<Qa> qaList = new ArrayList<>();
    private QaAdapter adapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        initQas();      //初始化信息栏
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QaAdapter(qaList) {
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        navView.setCheckedItem(R.id.nav_info);
    }

    private void initQas() {               //初始化信息栏
        for (int i = 0; i < 2; i++) {
            Qa Name = new Qa("张三","张老师",sdf.format(new Date()),R.mipmap.ic_launcher,"今天作业是什么？","");
            qaList.add(Name);
        }
    }
}

