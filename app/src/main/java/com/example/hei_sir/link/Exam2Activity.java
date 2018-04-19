package com.example.hei_sir.link;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.List;

public class Exam2Activity extends AppCompatActivity {
    public static final String EXAMNAME="exam_name";
    public static final String EXAMEXAM="exam_exam";
    public static final String EXAMRANK="exam_rank";
    private ImageView imageView;
    private TextView rank,score,chinese,math,english,politics,physics,chemical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2);
        Intent intent=getIntent();
        String examName=intent.getStringExtra(EXAMNAME);
        String examExam=intent.getStringExtra(EXAMEXAM);
        String examRank=intent.getStringExtra(EXAMRANK);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_tollbar);
        rank=(TextView)findViewById(R.id.rank);
        score=(TextView)findViewById(R.id.score);
        chinese=(TextView)findViewById(R.id.chinese);
        math=(TextView)findViewById(R.id.math);
        english=(TextView)findViewById(R.id.english);
        politics=(TextView)findViewById(R.id.politics);
        physics=(TextView)findViewById(R.id.physics);
        chemical=(TextView)findViewById(R.id.chemical);
        imageView=(ImageView)findViewById(R.id.image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(examExam);
        Glide.with(this).load(R.drawable.exam).into(imageView);
        List<Exam> exams= DataSupport.where("examId = ? and name = ?",examExam,examName).find(Exam.class);
        for (Exam exam1:exams) {
            rank.setText(exam1.getRank());
            score.setText(String.valueOf(exam1.getScore()));
            chinese.setText(String.valueOf(exam1.getChinese()));
            math.setText(String.valueOf(exam1.getMath()));
            english.setText(String.valueOf(exam1.getEnglish()));
            politics.setText(String.valueOf(exam1.getPolitics()));
            physics.setText(String.valueOf(exam1.getPhysics()));
            chemical.setText(String.valueOf(exam1.getChinese()));
        }

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
