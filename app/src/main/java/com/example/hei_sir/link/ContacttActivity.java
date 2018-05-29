package com.example.hei_sir.link;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpUtils;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ContacttActivity extends AppCompatActivity {
    private static String userName;
    String school,grade,clsses;
    private List<User> userList = new ArrayList<>();
    private ContacttAdapter adapter;
    private String phone,name;
    private static int io;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactt);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);       //一行只显示一个数据
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ContacttAdapter(userList) {
            @Override
            public int getItemCount() {
                Cursor cursor1=DataSupport.findBySQL("select * from User where user= ?",userName);
                if (cursor1.moveToFirst()){
                    cursor1.moveToFirst();
                    school=cursor1.getString(cursor1.getColumnIndex("school"));
                    grade=cursor1.getString(cursor1.getColumnIndex("grade"));
                    clsses=cursor1.getString(cursor1.getColumnIndex("clsses"));
                }
                cursor1.close();
                Cursor cursor= DataSupport.findBySQL("select * from User where school= ? and grade = ? and clsses = ?",school,grade,clsses);
                io=cursor.getCount();
                return cursor.getCount();
            }        //sql语句，返回查询的行数，实现数目统一
        };
        initPhone();      //初始化信息栏
        recyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
    }

    private void initPhone(){
        Cursor cursor1=DataSupport.findBySQL("select * from User where user= ?",userName);
        if (cursor1.moveToFirst()){
            cursor1.moveToFirst();
            school=cursor1.getString(cursor1.getColumnIndex("school"));
            grade=cursor1.getString(cursor1.getColumnIndex("grade"));
            clsses=cursor1.getString(cursor1.getColumnIndex("clsses"));
        }
        cursor1.close();
        Cursor cursor= DataSupport.findBySQL("select * from User where school= ? and grade = ? and clsses = ?",school,grade,clsses);
        if(cursor.moveToFirst()==false){
            Toast.makeText(ContacttActivity.this,"目前还没有家长注册",Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();    //移动到第一行
            do {
                name=  cursor.getString(cursor.getColumnIndex("name"));
                phone=  cursor.getString(cursor.getColumnIndex("phone"));
                // Log.d("QaActivity",answer);
                User u= new User();
                u.setPhone(phone);
                u.setName(name);
                KLog.d(name);
                userList.add(u);
                //Log.d("QaActivity",status);
            } while (cursor.moveToNext());
            KLog.d(userList.size());
        }
        cursor.close();
    }

}
