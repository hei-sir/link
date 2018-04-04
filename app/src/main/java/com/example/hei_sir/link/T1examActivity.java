package com.example.hei_sir.link;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import de.hdodenhof.circleimageview.CircleImageView;

public class T1examActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private CardView mCardView0,mCardView1,mCardView2,mCardView3;
    private TextView txt,name;
    private static String userName;
    private String notice2,school1,grade1,clsses1,notice1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t1exam);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ico_sign_01);
        }
        name=(TextView) findViewById(R.id.username);
        CircleImageView icon=(CircleImageView)findViewById(R.id.icon_image);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        init();
    }




    private void init(){
        mCardView1=(CardView)findViewById(R.id.cardView1);
        mCardView1.setOnClickListener(this);
        mCardView2=(CardView)findViewById(R.id.cardView2);
        mCardView2.setOnClickListener(this);
    }


    public boolean onOptionsItemSelected(MenuItem item){                    //滑动菜单键
        switch (item.getItemId()){
            case R.id.back:
                Intent intent2=new Intent(T1examActivity.this,EnterActivity.class);//返回登陆页面
                startActivity(intent2);
                finish();
                break;
            case R.id.about:
                Toast.makeText(T1examActivity.this, "版本号：1", Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardView1:
                Intent intent = new Intent(T1examActivity.this, T2examActivity.class);       //进入语文科目成绩
                startActivity(intent);
                intent.putExtra("extra_data",userName);
                break;
            case R.id.cardView2:

                break;
            default:
        }
    }
}
