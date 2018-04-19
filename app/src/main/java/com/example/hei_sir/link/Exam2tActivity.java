package com.example.hei_sir.link;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Exam2tActivity extends AppCompatActivity {
    private List<Exam> examList = new ArrayList<>();
    private Exam2tAdapter adapter;
    public static final String EXAMNAME="exam_name";
    public static final String EXAMEXAM="exam_exam";
    public static final String EXAMCOUNT="exam_count";
    private ImageView imageView;
    private int chinese,math,english,politics,physics,chemical,score;
    private String name,rank,userId,time,examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2t);
        Intent intent=getIntent();
        String examName=intent.getStringExtra(EXAMNAME);
        final String examExam=intent.getStringExtra(EXAMEXAM);
        final String examCount=intent.getStringExtra(EXAMCOUNT);
        KLog.d(examCount);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_tollbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);       //一行只显示一个数据
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Exam2tAdapter(examList) {
            @Override
            public int getItemCount() {
                Cursor cursor= DataSupport.findBySQL("select distinct * from Exam where examId=?",examExam);
                return cursor.getCount();
            }        //sql语句，返回查询的行数，实现数目统一
        };
        imageView=(ImageView)findViewById(R.id.image_view);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Exam2tActivity.this,ExamAddActivity.class);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(examExam);
        Glide.with(this).load(R.drawable.qa_green).into(imageView);


        Cursor cursor= DataSupport.findBySQL("select * from Exam where examId=?",examExam);
        if(cursor.moveToFirst()==false){
            Toast.makeText(Exam2tActivity.this,"没有考试成绩 ",Toast.LENGTH_SHORT).show();
        }else {
            List<Exam> exams=DataSupport.findAll(Exam.class);
            for (Exam exam1:exams){
                KLog.d(exam1.getExamId()+exam1.getName()+"已存在");
                examId=exam1.getExamId();
                userId=exam1.getUserId();
                name=  exam1.getName();
                rank=exam1.getRank();
                chinese=exam1.getChinese();
                math=exam1.getMath();
                english=exam1.getEnglish();
                politics=exam1.getPolitics();
                physics=exam1.getPhysics();
                chemical=exam1.getChemical();
                score=exam1.getScore();
                time=exam1.getTime();
                if (examId.equals(examExam)) {
                    Exam exam = new Exam(chinese, math, english, politics, physics, chemical, score, examExam, userId, name, rank, time);
                    examList.add(exam);
                }

                //Log.d("QaActivity",status);
            }
        }
        cursor.close();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
