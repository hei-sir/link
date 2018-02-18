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

    public static final String QA_NAME="qa_name";
    public static final String QA_CONTENT="qa_content";
    public static final String QA_ANSWER="qa_answer";
    private EditText qaAnswer;
    private static String qaNname,qaContent,qaAnswer1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    private String school,grade,clsses,tname,answer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa2t);
        Intent intent=getIntent();
        qaNname=intent.getStringExtra(QA_NAME);
        qaContent=intent.getStringExtra(QA_CONTENT);
        qaAnswer1=intent.getStringExtra(QA_ANSWER);
        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        TextView qaSnameText=(TextView)findViewById(R.id.qa_name);
        TextView qaContentText=(TextView)findViewById(R.id.qa_content);
        Button button = (Button) findViewById(R.id.button);
        qaAnswer=(EditText)findViewById(R.id.qa_answer);
        qaSnameText.setText(qaNname);
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
        Cursor cursor = DataSupport.findBySQL("select * from User where user = ?", qaNname);     //查询此用户名下数据库是否有值"select * from Book where name = ?","589");
        if (cursor.moveToFirst())
            do{
                school = cursor.getString(cursor.getColumnIndex("school"));
                //Log.d("Qa2sActivity",school);
                grade = cursor.getString(cursor.getColumnIndex("grade"));
                //Log.d("Qa2sActivity",grade);
                clsses = cursor.getString(cursor.getColumnIndex("clsses"));
                //Log.d("Qa2sActivity",clsses);
            }while (cursor.moveToNext());
        cursor.close();
        Cursor cursor1 = DataSupport.findBySQL("select * from User where school = ? and grade= ?and clsses = ? and identity = ?", school, grade, clsses, "老师");
        if (cursor1.moveToFirst())
            do {
                tname = cursor1.getString(cursor1.getColumnIndex("name"));
                Log.d("Qa2sActivity",tname);              //老师名字
            } while (cursor.moveToNext());
        if (TextUtils.isEmpty(answer)) {  //当提问没有输入时
            Toast.makeText(this, "提问不能为空！", Toast.LENGTH_SHORT).show();
            qaAnswer.requestFocus();//使输入框失去焦点
            return;
        }else {

            Log.d("Qa2sActivity",qaNname);
            Log.d("Qa2sActivity",qaContent);

            cursor1.close();
            //Cursor cursor2=DataSupport.findBySQL("update Qa set answer=? and status = ? where tname = ? and sname = ? and content = ?",answer,"1",tname,qaNname,qaContent);     //这是更新数据出现问题！
            //cursor2.close();


            Log.d("Qa2tActivity","输入的答案"+":"+answer);

            Cursor cursor3=DataSupport.findBySQL("select * from Qa where tname = ? and sname = ?",tname,qaNname);
            if (cursor3.moveToFirst()) {

                do {
                    String content1 = cursor3.getString(cursor3.getColumnIndex("content"));
                    Log.d("Qa2tActivity","数据库的问题"+":"+content1);
                    ContentValues values=new ContentValues();                  //采用contentValues方法更新数据
                    values.put("answer",answer);
                    values.put("time", sdf.format(new Date()));
                    values.put("status","1");
                    DataSupport.updateAll(Qa.class,values," tname=? and sname=? and content=?",tname,qaNname,content1);
                } while (cursor.moveToNext());
            }else{
                Log.d("Qa2tActivity","没有记录");
            }

            Toast.makeText(this,"回答成功",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,QaActivity.class);
            intent.putExtra("extra_data",tname);
            startActivity(intent);
            finish();
        }
    }
}
