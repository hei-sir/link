package com.example.hei_sir.link;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.GridLayoutManager;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout mDrawerLayout;
    private CardView mCardView1,mCardView2;
    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {                 //用于返回键的定义
            super.handleMessage(msg);
            isExit = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ico_sign_01);
        }
        navView.setCheckedItem(R.id.nav_main);
        init();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {          //菜单栏监听器
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.nav_info:
                    mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                    Toast.makeText(MainActivity.this, "这是个人信息", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, Main3Activity.class);  //进入主界面
                        startActivity(intent1);  //开始跳转
                        finish();
                    break;
                    case R.id.nav_main:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(MainActivity.this, "这是主页", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_zone:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(MainActivity.this, "这是班级天地", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);  //进入主界面
                        startActivity(intent);  //开始跳转

                        break;
                }return true;
            }
        });
    }

    private void init(){
        mCardView1=(CardView)findViewById(R.id.cardView1);
        mCardView1.setOnClickListener(this);
        mCardView2=(CardView)findViewById(R.id.cardView2);
        mCardView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.cardView1:
                Intent intent=new Intent(MainActivity.this,FormActivity.class);
                startActivity(intent);
                break;
            case R.id.cardView2:

                break;

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){                    //滑动菜单键
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.back:
                Intent intent2=new Intent(MainActivity.this,EnterActivity.class);//返回登陆页面
                startActivity(intent2);
                finish();
                break;
            case R.id.about:
                Toast.makeText(MainActivity.this, "版本号：1", Toast.LENGTH_SHORT).show();
                default:
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
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

}
