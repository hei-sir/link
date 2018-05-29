package com.example.hei_sir.link;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpCallbackListener;
import com.example.hei_sir.link.helper.HttpUtil;
import com.example.hei_sir.link.helper.JsonParser;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Qa2tActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String QA_SNAME="qa_sname";
    public static final String QA_TNAME="qa_tname";
    public static final String QA_CONTENT="qa_content";
    public static final String QA_ANSWER="qa_answer";
    private Button button2,button;
    private EditText qaAnswer;
    private RecognizerDialog recognizerDialog;
    private static String qatname,qaContent,qaAnswer1,qasname,status;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    private String school,grade,clsses,userName,answer1;

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
            Toast.makeText(Qa2tActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

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
        SpeechUtility.createUtility(Qa2tActivity.this, SpeechConstant.APPID +"=5b068ac5");
        TextView qaSnameText=(TextView)findViewById(R.id.qa_name);
        TextView qaContentText=(TextView)findViewById(R.id.qa_content);
        qaAnswer=(EditText)findViewById(R.id.qa_answer);
        qaSnameText.setText(qasname);
        qaContentText.setText(qaContent);

        KLog.d(qatname+"+"+qasname+"+"+qaContent);
        Cursor cursor=DataSupport.findBySQL("select * from Qa where tname=? and sname = ? and content=?" ,qatname,qasname,qaContent);
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            do {
                status=cursor.getString(cursor.getColumnIndex("status"));
                KLog.d(cursor.getString(cursor.getColumnIndex("content")));
            }while (cursor.moveToNext());
        }

        init();

    }

    private void init(){
        recognizerDialog=new RecognizerDialog(Qa2tActivity.this,listener);
        recognizerDialog.setParameter(SpeechConstant.DOMAIN,"iat");
        recognizerDialog.setParameter(SpeechConstant.SAMPLE_RATE,"16000");
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        TextView text = (TextView) findViewById(R.id.complete);
        text.setOnClickListener(this);
        button2=(Button)findViewById(R.id.button2);
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP){
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);  //开启睡眠两秒
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();  //开启线程
                }
                if (event.getAction() ==MotionEvent.ACTION_DOWN){
                    //当手势按下时获取音频
                    if (ContextCompat.checkSelfPermission(Qa2tActivity.this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(Qa2tActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
                    }else {
                        setDialog();
                    }
                }
                return false;
            }
        });
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
        Cursor cursor=DataSupport.findBySQL("select * from User where user = ?",qatname);          //没有考虑重名情况
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            userName=cursor.getString(cursor.getColumnIndex("name"));
            KLog.d(userName);
        }
        cursor.close();
        if (TextUtils.isEmpty(answer)) {  //当提问没有输入时
            Toast.makeText(this, "答案不能为空！", Toast.LENGTH_SHORT).show();
            qaAnswer.requestFocus();//使输入框失去焦点
            return;
        }else {

            Log.d("Qa2tActivity",qasname);
            KLog.d(qatname+"+"+userName);
            Log.d("Qa2tActivity",qaContent);


            Log.d("Qa2tActivity","输入的问题"+":"+qaContent);
            Log.d("Qa2tActivity","输入的答案"+":"+answer);

//上传数据
            String originAddress = "http://"+ GsonTools.ip+":8080/Test/QaUpServlet";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Qa.TNAME,userName);
            params.put(Qa.SNAME,qasname);
            params.put(Qa.TIME,sdf.format(new Date()));
            params.put(Qa.CONTENT,qaContent);
            params.put(Qa.ANSWER,answer);
            params.put(Qa.STATUS,"1");
            params.put(Qa.AAA,"1");      //代表学生端上传
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
                    //采用contentValues方法更新数据
                    ContentValues values=new ContentValues();
                    values.put("answer",answer);
                    values.put("time", sdf.format(new Date()));
                    values.put("status","1");
                    DataSupport.updateAll(Qa.class,values," tname=? and sname=? and content=?",userName,qasname,qaContent);
            Toast.makeText(this,"回答成功",Toast.LENGTH_SHORT).show();

            Qa2tActivity.this.finish();
        }
    }
    private void setDialog(){
        // qaContent.setText("");
        recognizerDialog.setListener(dialogListener);
        recognizerDialog.show();
    }

    private RecognizerDialogListener dialogListener=new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String text= JsonParser.parseIatResult(recognizerResult.getResultString());
            qaAnswer.append(text);


        }

        @Override
        public void onError(SpeechError speechError) {

        }
    };

    private InitListener listener=new InitListener() {
        @Override
        public void onInit(int i) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
