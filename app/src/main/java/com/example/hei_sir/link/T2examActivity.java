package com.example.hei_sir.link;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class T2examActivity extends AppCompatActivity {
    private List<Yuwen> yuwenList = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private T2examAdapter adapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    private SwipeRefreshLayout swipeRefresh;
    private static String userName,tname;
    private String sname,time,status,content,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t2exam);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        initInfo();      //初始化信息栏
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        T2examAdapter adapter= new T2examAdapter(yuwenList) {
            @Override
            public int getItemCount() {
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ico_sign_01);
        }
        navView.setCheckedItem(R.id.nav_info);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.back:
                Intent intent2=new Intent(T2examActivity.this,EnterActivity.class);//返回登陆页面
                startActivity(intent2);
                finish();
                break;
            case R.id.about:
                Toast.makeText(T2examActivity.this, "版本号：1", Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    private void initInfo(){               //初始化信息栏
       /* List<User> users= DataSupport.where("user = ? ",userName).find(User.class);
        for(User user:users){
            for (int i=0;i<2;i++) {
                Info User = new Info("账户", user.getUser());
                Info Name = new Info("姓名", user.getName() + "  " + user.getIdentity());
                Info School = new Info("学校", user.getSchool());
                Info Grade = new Info("年级", user.getGrade());
                Info Class = new Info("班级", user.getClsses());
                Info Num = new Info("学号", user.getNumber());
                infoList.add(User);
                infoList.add(Name);
                infoList.add(School);
                infoList.add(Grade);
                infoList.add(Class);
                infoList.add(Num);
            }
        }*/
    }

}
