package com.example.hei_sir.link;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpCallbackListener;
import com.example.hei_sir.link.helper.HttpUtil;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ExamAddActivity extends AppCompatActivity {
    private EditText yuwen,shuxue,yingyu,wuli,huaxue,zhengzhi,rank,examid,name;
    private static String username,exam,school,grade,clsses;;
    private TextView time;
    private Button button;
    private String date;
    private int order;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private String originAddress = "http://"+ GsonTools.ip+":8080/Test/ExamAddServlet";

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            if ("OK".equals(msg.obj.toString())){
                result = "success";
                Toast.makeText(ExamAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                //finish();
            }else if ("Wrong".equals(msg.obj.toString())){
                result = "fail";
                Toast.makeText(ExamAddActivity.this,"该学生不存在或其家长未注册，请修改学生姓名", Toast.LENGTH_SHORT).show();
            }else {
                result = msg.obj.toString();
                Toast.makeText(ExamAddActivity.this, result, Toast.LENGTH_SHORT).show();
                //Toast.makeText(ExamAddActivity.this, getString(R.string.error_invalid_internet), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_add);
        Intent intent=getIntent();
        username=intent.getStringExtra("extra_data");
        exam=intent.getStringExtra("extra_exam");
        date=sdf.format( new Date());
        KLog.d(username);
        examid=(EditText)findViewById(R.id.examid);
        examid.setText(exam);
        name=(EditText)findViewById(R.id.name);
        yuwen=(EditText)findViewById(R.id.yuwen);
        shuxue=(EditText)findViewById(R.id.shuxue);
        yingyu=(EditText)findViewById(R.id.yingyu);
        wuli=(EditText)findViewById(R.id.wuli);
        huaxue=(EditText)findViewById(R.id.huaxue);
        zhengzhi=(EditText)findViewById(R.id.zhengzhi);
        rank=(EditText)findViewById(R.id.rank);
        time=(TextView)findViewById(R.id.time);
        time.setText(date);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    add();
            }
        });

    }

    private void add(){         //这是确定录入的操作
        final String name1=name.getText().toString().trim();
        final String examid1=examid.getText().toString().trim();
        final int rank1= Integer.parseInt(rank.getText().toString().trim());
        final int yuwen1=Integer.parseInt(yuwen.getText().toString().trim());
        final int shuxue1=Integer.parseInt(shuxue.getText().toString().trim());
        final int yingyu1=Integer.parseInt(yingyu.getText().toString().trim());
        final int wuli1=Integer.parseInt(wuli.getText().toString().trim());
        final int huaxue1=Integer.parseInt(huaxue.getText().toString().trim());
        final int zhengzhi1=Integer.parseInt(zhengzhi.getText().toString().trim());
        if (TextUtils.isEmpty(name1)){
            Toast.makeText(this, "学生姓名不能为空！", Toast.LENGTH_SHORT).show();
            name.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(examid1)){
            Toast.makeText(this, "考试名称不能为空！", Toast.LENGTH_SHORT).show();
            examid.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(rank.getText().toString().trim())){
            Toast.makeText(this, "班级排名不能为空！", Toast.LENGTH_SHORT).show();
            rank.requestFocus();//使输入框失去焦点
            return;
        }else if (rank1<0) {
            Toast.makeText(this, "班级排名应为正数", Toast.LENGTH_SHORT).show();
            rank.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(yuwen.getText().toString().trim())) {
            Toast.makeText(this, "语文成绩不能为空！", Toast.LENGTH_SHORT).show();
            yuwen.requestFocus();//使输入框失去焦点
            return;
        }else if (yuwen1>150&&yuwen1<0) {
            Toast.makeText(this, "语文成绩必须为正数，且小于150！", Toast.LENGTH_SHORT).show();
            yuwen.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(shuxue.getText().toString().trim())) {
            Toast.makeText(this, "数学成绩不能为空！", Toast.LENGTH_SHORT).show();
            shuxue.requestFocus();//使输入框失去焦点
            return;
        }else if (shuxue1>150&&shuxue1<0) {
            Toast.makeText(this, "数学成绩必须为正数，且小于150！", Toast.LENGTH_SHORT).show();
            shuxue.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(yingyu.getText().toString().trim())) {
            Toast.makeText(this, "英语成绩不能为空！", Toast.LENGTH_SHORT).show();
            yingyu.requestFocus();//使输入框失去焦点
            return;
        }else if (yingyu1>150&&yingyu1<0) {
            Toast.makeText(this, "英语成绩必须为正数，且小于150！", Toast.LENGTH_SHORT).show();
            yingyu.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(wuli.getText().toString().trim())) {
            Toast.makeText(this, "物理成绩不能为空！", Toast.LENGTH_SHORT).show();
            wuli.requestFocus();//使输入框失去焦点
            return;
        }else if (wuli1>150&&wuli1<0) {
            Toast.makeText(this, "物理成绩必须为正数，且小于150！", Toast.LENGTH_SHORT).show();
            wuli.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(huaxue.getText().toString().trim())) {
            Toast.makeText(this, "化学成绩不能为空！", Toast.LENGTH_SHORT).show();
            huaxue.requestFocus();//使输入框失去焦点
            return;
        }else if (huaxue1>150&&huaxue1<0) {
            Toast.makeText(this, "化学成绩必须为正数，且小于150！", Toast.LENGTH_SHORT).show();
            huaxue.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(zhengzhi.getText().toString().trim())) {
            Toast.makeText(this, "政治成绩不能为空！", Toast.LENGTH_SHORT).show();
            zhengzhi.requestFocus();//使输入框失去焦点
            return;
        }else if (zhengzhi1>150&&zhengzhi1<0) {
            Toast.makeText(this, "政治成绩必须为正数，且小于150！", Toast.LENGTH_SHORT).show();
            zhengzhi.requestFocus();//使输入框失去焦点
            return;
        }else {
            Cursor cursor=DataSupport.findBySQL("select * from User where user = ?",username);
            if (cursor.moveToFirst()){
                cursor.moveToFirst();
                school=cursor.getString(cursor.getColumnIndex("school"));
                grade=cursor.getString(cursor.getColumnIndex("grade"));
                clsses=cursor.getString(cursor.getColumnIndex("clsses"));
            }
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Exam.CHINESE,yuwen.getText().toString().trim());
            params.put(Exam.MATH,shuxue.getText().toString().trim());
            params.put(Exam.ENGLISH,yingyu.getText().toString().trim());
            params.put(Exam.PHYSICS,wuli.getText().toString().trim());
            params.put(Exam.CHEMICAL,huaxue.getText().toString().trim());
            params.put(Exam.POLITICS,zhengzhi.getText().toString().trim());
            params.put(Exam.EXAMID,examid1);
            params.put(Exam.USERID,"null");
            params.put(Exam.SCORE,String.valueOf(yuwen1+shuxue1+yingyu1+wuli1+huaxue1+zhengzhi1));
            params.put(Exam.RANK,rank.getText().toString().trim());
            params.put(Exam.NAME,name1);
            params.put(User.SCHOOL,school);
            params.put(User.GRADE,grade);
            params.put(User.CLSSES,clsses);
            params.put(Exam.TIME,time.getText().toString());
            try {
                //构造完整URL
                String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
                //发送请求
                HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        Message message = new Message();
                        message.obj = response;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        Message message = new Message();
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void getDate() {                //获取日期
        String[]  strs=date.split("-");
        for(int i=0,len=strs.length;i<len;i++){
            System.out.println(strs[i].toString());
        }
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                KLog.d(String.format("%d%d%d",i,i1+1,i2));
                order= Integer.parseInt(String.format("%d%d%d",i,i1+1,i2));
                KLog.d(String.valueOf(order+1));
                date=String.format("%d-%d-%d",i,i1+1,i2);
                time.setText(date);
            }
        },Integer.parseInt(strs[0].toString()),Integer.parseInt(strs[1].toString())-1,Integer.parseInt(strs[2].toString())).show();
    }
}
