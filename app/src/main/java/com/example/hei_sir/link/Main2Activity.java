package com.example.hei_sir.link;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefresh();                                              //未完成
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
                        Toast.makeText(Main2Activity.this, "这是个人信息", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(Main2Activity.this, Main3Activity.class);  //进入主界面
                        startActivity(intent1);  //开始跳转
                        finish();  //finish掉此界面
                        break;
                    case R.id.nav_main:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(Main2Activity.this, "这是主页", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);  //进入主界面
                        startActivity(intent);  //开始跳转
                        finish();  //finish掉此界面
                        break;
                    case R.id.nav_zone:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(Main2Activity.this, "这是班级天地", Toast.LENGTH_SHORT).show();
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
}