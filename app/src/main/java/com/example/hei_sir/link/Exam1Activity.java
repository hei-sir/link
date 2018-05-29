package com.example.hei_sir.link;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.hei_sir.link.helper.ExamAdapter;
import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpUtils;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Exam1Activity extends AppCompatActivity {
    private List<Exam> examList = new ArrayList<>();
    private ExamAdapter adapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    private static boolean isExit = false;
    private SwipeRefreshLayout swipeRefresh;
    private static String userName,ename,tname1,qsname1,qtname1;
    private int chinese, math, english, politics,physics, chemical, score;
    private String  examId, userId, name, rank,time;

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
        setContentView(R.layout.activity_exam1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);       //一行只显示一个数据
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExamAdapter(examList) {
            @Override
            public int getItemCount() {
                Cursor cursor= DataSupport.findBySQL("select * from Exam where userId=?",userName);
                return cursor.getCount();
            }        //sql语句，返回查询的行数，实现数目统一
        };
        initExams();      //初始化信息栏
        recyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                ReflashExam();
            }
        });
    }

    private void initExams() {               //初始化信息栏
        Cursor cursor= DataSupport.findBySQL("select * from Exam order by time desc");
        if(cursor.moveToFirst()==false){
            Toast.makeText(Exam1Activity.this,"没有考试成绩 ",Toast.LENGTH_SHORT).show();
        }else {
            List<Exam> exams=DataSupport.findAll(Exam.class);
            for (Exam exam1:exams){
                KLog.d(exam1.getExamId()+"已存在");
                name=  exam1.getName();
                KLog.d();
                KLog.d(userName);
                time=  exam1.getTime();
                userId=exam1.getUserId();
                rank=exam1.getRank();
                chinese=exam1.getChinese();
                math=exam1.getMath();
                english=exam1.getEnglish();
                politics=exam1.getPolitics();
                physics=exam1.getPhysics();
                chemical=exam1.getChemical();
                score=exam1.getScore();
                examId=exam1.getExamId();
                Log.d("QaActivity",examId);
                if (userId.equals(userName)) {
                    Exam exam = new Exam(chinese, math, english, politics, physics, chemical, score, examId, userId, name, rank, time);
                    examList.add(exam);
                }

                //Log.d("QaActivity",status);
            }
        }
        cursor.close();

    }

    private void ReflashExam(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //获取成绩系统数据！
                String path = "http://" + GsonTools.ip + ":8080/Test/ExamServlet";
                String jsonString = HttpUtils.getJsonContent(path);//从网络获取数据
                List<Exam> list = GsonTools.stringToList(jsonString, Exam.class);

                for (Exam exam : list) {
                    String lexamId;
                    Cursor c = DataSupport.findBySQL("select * from Exam where userId = ? and examId=?", userName,exam.getExamId());        //搜索本地账户是否有数据
                    KLog.d("进入问答循环：username=" + userName +"examId" +exam.getExamId());
                    if (c.moveToFirst()==false) {
                        //无值，可以新建成绩
                        KLog.d("判断数据库无值");
                        Exam exam1 = new Exam(exam.getChinese(), exam.getMath(), exam.getEnglish(), exam.getPolitics(), exam.getPhysics(), exam.getChemical(), exam.getScore(), exam.getExamId(), exam.getUserId(), exam.getName(), exam.getRank(), exam.getTime());
                        KLog.d(exam.getExamId());
                        exam1.save();
                    }
                    Log.d("完成","全部");
                    c.close();
                }//在此处输入操作

                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initExams();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        Exam1Activity.this.finish();
    }
}
