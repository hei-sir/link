package com.example.hei_sir.link;

import android.content.ContentValues;
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

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpUtils;
import com.socks.library.KLog;

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
    private static String userName,sname,sname1,tname1,qsname1,qtname1;
    private String tname,time,status,content,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa1s);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Qa1sActivity.this,Qa2sActivity.class);
                intent.putExtra("extra_data",userName);
                startActivity(intent);
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
                    KLog.d("初始化Qa"+answer);
                    if (status.equals("0")){
                        Qa q = new Qa(tname,sname,time,R.drawable.qa_red,content,"暂无回答",status);
                        qaList.add(q);
                    }else {
                        Qa q= new Qa(tname,sname,time,R.drawable.qa_green,content,answer,status);
                        qaList.add(q);}
                } while (cursor.moveToNext());
        }
        cursor.close();

    }

    private void ReflashQa(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //获取问答系统数据！
                String path = "http://" + GsonTools.ip + ":8080/Test/QaServet";
                String jsonString = HttpUtils.getJsonContent(path);//从网络获取数据
                List<Qa> list = GsonTools.stringToList(jsonString, Qa.class);

                for (Qa qa : list) {
                    Cursor c = DataSupport.findBySQL("select * from User where user = ?", userName);
                    KLog.d("进入问答循环：username=" + userName);
                    if (c.moveToFirst()) {
                        c.moveToFirst();
                        qsname1 = c.getString(c.getColumnIndex("name"));
                        KLog.d(qsname1);
                        qtname1 = c.getString(c.getColumnIndex("name"));
                    }
                    KLog.d("进入Json循环" + "   " + qa.getSname());
                    if (qa.getSname().equals(qsname1)) {
                        sname1 = qa.getSname();
                        KLog.d("进入学生循环" + "   " + qa.getSname());
                        Cursor cursor1 = DataSupport.findBySQL("select * from Qa where sname = ? and content=?", sname1, qa.getContent());  //搜寻本地是否有当前账户数据
                        if (cursor1.moveToFirst() == false) {
                            KLog.d("这里是数据库" + sname1);
                            KLog.d("新建学生问题:" + qa.getContent());
                            if (qa.getAnswer() == null) {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_red, qa.getContent(), null, qa.getStatus());
                                qa1.save();
                            } else {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_green, qa.getContent(), qa.getAnswer(), qa.getStatus());
                                qa1.save();
                            }
                        } else {
                            KLog.d("更新学生问题:" + sname1 + qa.getContent());
                            ContentValues values = new ContentValues();                  //采用contentValues方法更新数据
                            values.put("answer", qa.getAnswer());
                            values.put("time", qa.getTime());
                            values.put("status", qa.getStatus());
                            KLog.d(qa.getSname() + "+" + qa.getTname() + "+" + qa.getContent() + "+" + qa.getAnswer() + "+" + qa.getStatus() + "+" + qa.getTime());
                            DataSupport.updateAll(Qa.class, values, " sname=? and content=?", qa.getSname(), qa.getContent());
                        }
                        cursor1.close();
                    } else if (qa.getTname().equals(qtname1)) {
                        tname1 = qa.getTname();
                        Cursor cursor1 = DataSupport.findBySQL("select * from Qa where tname = ? and content=?", tname1, qa.getContent());  //搜寻本地是否有当前账户数据
                        if (cursor1.moveToFirst() == false) {
                            KLog.d(tname1);
                            KLog.d("这里是数据库");
                            KLog.d("新建教师问题:" + qa.getContent());
                            if (qa.getStatus().equals("0")) {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_red, qa.getContent(), null, qa.getStatus());
                                qa1.save();
                            } else {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_green, qa.getContent(), qa.getAnswer(), qa.getStatus());
                                qa1.save();
                            }
                        } else {
                            KLog.d("更新老师问题:" + tname1 + qa.getContent());
                            ContentValues values = new ContentValues();                  //采用contentValues方法更新数据
                            values.put("answer", qa.getAnswer());
                            values.put("time", qa.getTime());
                            values.put("status", qa.getStatus());
                            DataSupport.updateAll(Qa.class, values, " tname=? and content=?", qa.getTname(), qa.getContent());
                        }
                        cursor1.close();
                    }


                }//在此处输入操作

                Cursor cursor2 = DataSupport.findBySQL("select * from Qa");
                if (cursor2.moveToFirst()) {
                    cursor2.moveToFirst();
                    do {
                        KLog.d(cursor2.getCount() + "    " + cursor2.getString(cursor2.getColumnIndex("content")));
                    } while (cursor2.moveToNext());
                }
                cursor2.close();
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

