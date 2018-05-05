package com.example.hei_sir.link;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Qa2sActivity extends AppCompatActivity implements View.OnClickListener {

    private static String userName;
    private TextView qaTname;
    private EditText qaContent;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    private String school,grade,clsses,tname,sname;

    Handler mHandler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";

            if ("OK".equals(msg.obj.toString())){
                //getInfo();
                result="success";

            }else if ("Wrong".equals(msg.obj.toString())){
                result="fail";
            }else {
                result = msg.obj.toString();
            }
            Toast.makeText(Qa2sActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa2s);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        qaTname=(TextView)findViewById(R.id.qa_name);
        qaContent=(EditText) findViewById(R.id.qa_content);
        final String content = qaContent.getText().toString().trim();
        Cursor cursor = DataSupport.findBySQL("select * from User where user = ?", userName);     //查询此用户名下数据库是否有值"select * from Book where name = ?","589");
        if (cursor.moveToFirst())
            do{
                school = cursor.getString(cursor.getColumnIndex("school"));
                Log.d("Qa2sActivity",school);
                grade = cursor.getString(cursor.getColumnIndex("grade"));
                Log.d("Qa2sActivity",grade);
                clsses = cursor.getString(cursor.getColumnIndex("clsses"));
                Log.d("Qa2sActivity",clsses);
                sname=cursor.getString(cursor.getColumnIndex("name"));
                Log.d("Qa2sActivity",sname);
            }while (cursor.moveToNext());
        cursor.close();
        Cursor cursor1 = DataSupport.findBySQL("select * from User where school = ? and grade= ? and clsses = ? and identity = ? ", school, grade, clsses,"老师");
        if (cursor1.moveToFirst()==true) {
            if (cursor1.moveToFirst()) {
                do {
                    tname = cursor1.getString(cursor1.getColumnIndex("name"));
                    Log.d("Qa2sActivity", tname);
                    qaTname.setText(tname);
                } while (cursor1.moveToNext());
            }
        }else{
                Toast.makeText(this,"您所在的班级暂无老师注册,无法提问",Toast.LENGTH_SHORT).show();
        }
        cursor1.close();
        init();
    }

    private void init(){
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        ImageView text = (ImageView) findViewById(R.id.iv_return);
        text.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return:
                Qa2sActivity.this.finish();
                break;
            case R.id.button:
                question();
                break;
        }
    }
    public void question(){
        final String content = qaContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {  //当提问没有输入时
            Toast.makeText(this, "提问不能为空！", Toast.LENGTH_SHORT).show();
            qaContent.requestFocus();//使输入框失去焦点
            return;
        }else {
            if (tname.equals(null)){
                tname="暂无老师";
            }
            //上传数据
            String originAddress = "http://"+ GsonTools.ip+":8080/Test/QaUpServlet";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Qa.TNAME,tname);
            params.put(Qa.SNAME,sname);
            params.put(Qa.TIME,sdf.format(new Date()));
            params.put(Qa.CONTENT,qaContent.getText().toString());
            params.put(Qa.STATUS,"0");
            params.put(Qa.AAA,"0");      //代表学生端上传
            KLog.d("成功上传");
            try {
                //构造完整URL
                String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
                //发送请求
                HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        message.obj = response;
                        mHandler1.sendMessage(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        Message message = new Message();
                        message.obj = e.toString();
                        mHandler1.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            KLog.d("上传部分运行完了");
            Qa qa=new Qa(sname, tname, sdf.format(new Date()), R.drawable.qa_red, qaContent.getText().toString(), "","0");
            qa.save();
            Log.d("Qa2sActivity",userName);
            Log.d("Qa2sActivity",tname);
            Log.d("Qa2sActivity",sdf.format( new Date()));
            Log.d("Qa2sActivity",qaContent.getText().toString());
            Toast.makeText(this,"提问成功",Toast.LENGTH_SHORT).show();
            Qa2sActivity.this.finish();
        }

    }

}
