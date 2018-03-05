package com.example.hei_sir.link;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Qa1sActivity extends AppCompatActivity {
    private List<Qa> qaList = new ArrayList<>();
    private Qa1sAdapter adapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
    private SwipeRefreshLayout swipeRefresh;
    private static String userName,sname;
    private String tname,time,status,content,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa1s);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Qa1sActivity.this,Qa2sActivity.class);
                intent.putExtra("extra_data",userName);
                startActivity(intent);
                finish();
            }
        });
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);       //一行只显示一个数据
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Qa1sAdapter(qaList) {
            @Override
            public int getItemCount() {
                Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
                if (cursor1.moveToFirst()){
                    sname=cursor1.getString(cursor1.getColumnIndex("name"));
                }
                Cursor cursor= DataSupport.findBySQL("select * from Qa where sname = ? ",sname);
                return cursor.getCount();
            }        //sql语句，返回查询的行数，实现数目统一
        };
        initQas();      //初始化信息栏
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
        Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
        if (cursor1.moveToFirst()){
            sname=cursor1.getString(cursor1.getColumnIndex("name"));
        }
        cursor1.close();
        Cursor cursor= DataSupport.findBySQL("select * from Qa where sname = ?  order by time desc",sname);
        if(cursor.moveToFirst()==false){
            Toast.makeText(Qa1sActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            if (cursor.moveToFirst())
                do {
                    tname=  cursor.getString(cursor.getColumnIndex("tname"));
                    time=  cursor.getString(cursor.getColumnIndex("time"));
                    status=cursor.getString(cursor.getColumnIndex("status"));
                    content=cursor.getString(cursor.getColumnIndex("content"));
                    answer=cursor.getString(cursor.getColumnIndex("answer"));
                    if (answer.equals("")){
                        Qa q = new Qa(tname,sname,time,R.drawable.qa_red,"     问题："+content+"\n\n"+"     暂未回答     "+answer,answer,status);
                        qaList.add(q);
                    }else {
                        Qa q= new Qa(tname,sname,time,R.drawable.qa_green,"     问题："+content+"\n\n"+"     已回答：     "+answer,answer,status);
                        qaList.add(q);}
                } while (cursor.moveToNext());
        }
        cursor.close();

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

