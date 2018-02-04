package com.example.hei_sir.link;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private List<Info> infoList=new ArrayList<>();
    private static String userName;

    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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
        InfoAdapter adapter= new InfoAdapter(infoList) {
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recyclerView.setAdapter(adapter);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ico_sign_01);
        }
        navView.setCheckedItem(R.id.nav_info);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {          //菜单栏监听器
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.nav_info:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(Main3Activity.this, "这是个人信息", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_main:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(Main3Activity.this, "这是主页", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Main3Activity.this, MainActivity.class);  //进入主界面
                        intent.putExtra("extra_data",userName);
                        startActivity(intent);  //开始跳转
                        finish();  //finish掉此界面
                        break;
                    case R.id.nav_zone:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        Toast.makeText(Main3Activity.this, "这是班级天地", Toast.LENGTH_SHORT).show();
                        Intent intent1= new Intent(Main3Activity.this, Main2Activity.class);  //进入主界面
                        intent1.putExtra("extra_data",userName);
                        startActivity(intent1);  //开始跳转
                        finish();  //finish掉此界面

                        break;
                }return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.back:
                Intent intent2=new Intent(Main3Activity.this,EnterActivity.class);//返回登陆页面
                startActivity(intent2);
                finish();
                break;
            case R.id.about:
                Toast.makeText(Main3Activity.this, "版本号：1", Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    private void initInfo(){               //初始化信息栏
        List<User> users= DataSupport.where("user = ? ",userName).find(User.class);
        for(User user:users){
            String id;
            if(user.getIdentity()=="1"){
                id="老师";
            }else {
                id="学生";
            }
            for (int i=0;i<2;i++) {
                Info User=new Info("账户",user.getUser());
            Info Name = new Info("姓名", user.getName()+"  "+id);
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
        }
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
