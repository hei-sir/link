package com.example.hei_sir.link;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Qa2tActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String QA_SNAME="qa_sname";
    public static final String QA_TNAME="qa_tname";
    public static final String QA_CONTENT="qa_content";
    public static final String QA_ANSWER="qa_answer";
    private EditText qaAnswer;
    private static String qatname,qaContent,qaAnswer1,qasname;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    private String school,grade,clsses,userName,answer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa2t);
        Intent intent=getIntent();
        qatname=intent.getStringExtra(QA_TNAME);
        qasname=intent.getStringExtra(QA_SNAME);
        qaContent=intent.getStringExtra(QA_CONTENT);
        qaAnswer1=intent.getStringExtra(QA_ANSWER);
        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        TextView qaSnameText=(TextView)findViewById(R.id.qa_name);
        TextView qaContentText=(TextView)findViewById(R.id.qa_content);
        Button button = (Button) findViewById(R.id.button);
        qaAnswer=(EditText)findViewById(R.id.qa_answer);
        qaSnameText.setText(qasname);
        qaContentText.setText(qaContent);
        if (qaAnswer1.equals("")) {
            //qaAnswer.setHint("555");
        }else{
            Toast.makeText(this,"再次回答可修改之前的回复",Toast.LENGTH_SHORT).show();
            qaAnswer.setHint("输入想修改的答案");
            button.setText("修改回答");
        }
        init();

    }

    private void init(){
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        TextView text = (TextView) findViewById(R.id.complete);
        text.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.complete:
                finish();
                break;
            case R.id.button:
                answer();

        }

    }

    public void answer(){
        final String answer = qaAnswer.getText().toString().trim();
        String[] array=qaContent.split("\\s+");         //正则表达式提取问题content
        String[] array1=array[1].split("：+");
        if (TextUtils.isEmpty(answer)) {  //当提问没有输入时
            Toast.makeText(this, "答案不能为空！", Toast.LENGTH_SHORT).show();
            qaAnswer.requestFocus();//使输入框失去焦点
            return;
        }else {

            Log.d("Qa2tActivity",qasname);
            Log.d("Qa2tActivity",qaContent);



            Log.d("Qa2tActivity","输入的答案"+":"+answer);

            Cursor cursor3=DataSupport.findBySQL("select * from Qa where tname = ? and sname = ? and content = ?",qatname,qasname,array1[1]);
            if (cursor3.moveToFirst()) {

                do {
                    String content1 = cursor3.getString(cursor3.getColumnIndex("content"));
                    Log.d("Qa2tActivity","数据库的问题"+":"+content1);
                    ContentValues values=new ContentValues();                  //采用contentValues方法更新数据
                    values.put("answer",answer);
                    values.put("time", sdf.format(new Date()));
                    values.put("status","1");
                    DataSupport.updateAll(Qa.class,values," tname=? and sname=? and content=?",qatname,qasname,content1);
                } while (cursor3.moveToNext());
            }else{
                Log.d("Qa2tActivity","没有记录");
            }

            Toast.makeText(this,"回答成功",Toast.LENGTH_SHORT).show();

           Cursor cursor=DataSupport.findBySQL("select * from User where name = ?",qasname);          //没有考虑重名情况
           if (cursor.moveToFirst()){
               school=cursor.getString(cursor.getColumnIndex("school"));
               grade=cursor.getString(cursor.getColumnIndex("grade"));
               clsses=cursor.getString(cursor.getColumnIndex("clsses"));
           }
           cursor.close();
           Cursor cursor1=DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses= ? and identity = ?",school,grade,clsses,"老师");
           if (cursor1.moveToFirst()){
               userName=cursor1.getString(cursor1.getColumnIndex("user"));
           }
           cursor1.close();
            Intent intent=new Intent(this,QaActivity.class);
            intent.putExtra("extra_data",userName);
            startActivity(intent);
            finish();
        }
    }
}
