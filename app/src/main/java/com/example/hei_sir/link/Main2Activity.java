package com.example.hei_sir.link;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private static String userName;
    private List<Zone> zoneList = new ArrayList<>();
    String[][] array;
    String[] user;
    private ZoneAdapter adapter;
    private Bitmap bitmap;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String name,time,content,username,school,grade,clsses,imagePath;
    private int imageId,num1;
    private String num="0";

    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
       /* num=intent.getStringExtra("extra_num");
        if (num.equals("0")){
            num1=0;
        }else if (num.equals("2")){
            imagePath1=intent.getStringExtra("extra_photo");
            num1=1;
        }*/
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,ZoneActivity.class);
                intent.putExtra("extra_data",userName);
                startActivity(intent);
                finish();
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ZoneAdapter(zoneList) {
            @Override
            public int getItemCount() {
                Cursor cursor = DataSupport.findBySQL("select * from User where user = ?", userName);
                if (cursor.moveToFirst() == false) {
                    //Toast.makeText(Main2Activity.this,"没有数据",Toast.LENGTH_SHORT).show();
                } else {
                    cursor.moveToFirst();    //移动到第一行
                    do {
                        school = cursor.getString(cursor.getColumnIndex("school"));
                        grade = cursor.getString(cursor.getColumnIndex("grade"));
                        clsses = cursor.getString(cursor.getColumnIndex("clsses"));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                Cursor cursor1 = DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? ", school, grade, clsses);
                int count=0;
                if (cursor1.moveToFirst() == true) {
                    cursor1.moveToFirst();
                    do {
                        username = cursor1.getString(cursor1.getColumnIndex("user"));
                        //Log.d("Main2Activity", "计数用户"+username);
                        Cursor cursor2 = DataSupport.findBySQL("select * from Zone where username=?", username);
                        //Log.d("Main2Activity","数目："+String.valueOf(count));
                        count =count +cursor2.getCount();
                    } while (cursor1.moveToNext());
                }        //sql语句，返回查询的行数，实现数目统一
                return count;
            }
        };
        initZones();      //初始化信息栏
        recyclerView.setAdapter(adapter);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshZone();                                              //未完成
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ico_sign_01);
        }
        navView.setCheckedItem(R.id.nav_zone);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {          //菜单栏监听器
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_info:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        //Toast.makeText(Main2Activity.this, "这是个人信息", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(Main2Activity.this, Main3Activity.class);  //进入主界面
                        intent1.putExtra("extra_data",userName);
                        startActivity(intent1);  //开始跳转
                        finish();  //finish掉此界面
                        break;
                    case R.id.nav_main:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        //Toast.makeText(Main2Activity.this, "这是主页", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);  //进入主界面
                        intent.putExtra("extra_data",userName);
                        startActivity(intent);  //开始跳转
                        finish();  //finish掉此界面
                        break;
                    case R.id.nav_zone:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        //Toast.makeText(Main2Activity.this, "这是班级天地", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.back:
                Intent intent2 = new Intent(Main2Activity.this, EnterActivity.class);//返回登陆页面
                startActivity(intent2);
                finish();
                break;
            case R.id.about:
                Toast.makeText(Main2Activity.this, "版本号：1", Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {               //退出确认
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initZones() {
       /* Cursor cursor= DataSupport.findBySQL("select * from User where user = ?",userName);
        if(cursor.moveToFirst()==false){
            //Toast.makeText(Main2Activity.this,"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();    //移动到第一行
            do {
                school=  cursor.getString(cursor.getColumnIndex("school"));
                grade=  cursor.getString(cursor.getColumnIndex("grade"));
                clsses=cursor.getString(cursor.getColumnIndex("clsses"));
                    //Qa q= new Qa(sname,userName,time,R.drawable.qa_green,"     问题："+content+"\n\n"+"     已回答：     "+answer,answer,status);
                    //qaList.add(q);}
            } while (cursor.moveToNext());
        }
        cursor.close();
        Cursor cursor1= DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? ",school,grade,clsses);
        if (cursor1.moveToFirst()==true){
            cursor1.moveToFirst();
            do{
                username=cursor1.getString(cursor1.getColumnIndex("user"));
                Log.d("Main2Activity",username);
            }while (cursor1.moveToNext());
        }cursor1.close();*/

        //以下代码为先按照时间倒序查找所有内容，然后排除掉不属于的username，剩下的应该是需要的部分
        int arraycount=0;      //判断数组总量
        Cursor cursor2=DataSupport.findBySQL("select * from Zone order by time desc");
        array= new String[cursor2.getCount()][5];
        if (cursor2.moveToFirst()==false){
            Toast.makeText(Main2Activity.this,"最新动态刷新失败",Toast.LENGTH_SHORT).show();
        }else{
            int i=0;
            do {
                username=cursor2.getString(cursor2.getColumnIndex("username"));
                name = cursor2.getString(cursor2.getColumnIndex("name"));
                time = cursor2.getString(cursor2.getColumnIndex("time"));
                content = cursor2.getString(cursor2.getColumnIndex("content"));
//                imagePath=cursor2.getString(cursor2.getColumnIndex("imagePath"));
               // imagePath=cursor2.getString(cursor2.getColumnIndex("imagePath"));
                Log.d("Main2Activity",content);
                //imageId = cursor2.getInt(cursor2.getColumnIndex("imageId"));
                String[] arraypath=content.split("n:n+");
                Log.d("这应该是内容",arraypath[0]);
                Log.d("这应该是图片",arraypath[1]);
                array[i][0]=username;
                array[i][1]=name;
                array[i][2]=time;
                array[i][3]=arraypath[0];
                array[i][4]=arraypath[1];
                arraycount=i;
                Log.d("Main2Activity",String.valueOf(arraycount));
                i=i+1;                  //将数据暂时存入数组，后面再处理
            }while (cursor2.moveToNext());
        }

        Cursor cursor= DataSupport.findBySQL("select * from User where user = ?",userName);
        if(cursor.moveToFirst()==false){
            //Toast.makeText(Main2Activity.this,"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();    //移动到第一行
            do {
                school=  cursor.getString(cursor.getColumnIndex("school"));
                grade=  cursor.getString(cursor.getColumnIndex("grade"));
                clsses=cursor.getString(cursor.getColumnIndex("clsses"));
                //Qa q= new Qa(sname,userName,time,R.drawable.qa_green,"     问题："+content+"\n\n"+"     已回答：     "+answer,answer,status);
                //qaList.add(q);}
            } while (cursor.moveToNext());
        }
        cursor.close();
        Cursor cursor1= DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? ",school,grade,clsses);
        user=new String[cursor1.getCount()];
        if (cursor1.moveToFirst()==true){
            cursor1.moveToFirst();
            int i =0;
            do{
                username=cursor1.getString(cursor1.getColumnIndex("user"));
                Log.d("Main2Activity",username);
                user[i]=username;
                i=i+1;
            }while (cursor1.moveToNext());
        }
        for (int i =0;i<cursor2.getCount();i++){
            for(int j=0;j<cursor1.getCount();j++){
                if(array[i][0].equals(user[j])){
                    Zone zone=new Zone(array[i][0],array[i][1],array[i][2],array[i][3],R.mipmap.ic_launcher_round,array[i][4]);
                    zoneList.add(zone);
                }
            }
        }

        Toast.makeText(Main2Activity.this,"最新动态加载完成",Toast.LENGTH_SHORT).show();
    }

    private void RefreshZone(){
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
                        initZones();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}