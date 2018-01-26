package com.example.hei_sir.link;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;


public class FromAddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText  lession;
    private Button button,button1;
    private TextView enterText;

    Spinner date,time;
    String[] dates={"Mon","Tues","Wed","Thur","Fri","Sat","Sun"};
    String[] times={"01","02","03","04","05","06","07","08","09","10","11","12"};
    private String ddate,dtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_add);
        lession = (EditText) findViewById(R.id.lesson);//科目
        button = (Button) findViewById(R.id.addlesson);//确认添加键
        button.setOnClickListener(this);
        button1 = (Button) findViewById(R.id.deletelesson);//确认删除键
        button1.setOnClickListener(this);
        enterText = (TextView) findViewById(R.id.complete);//右上角完成键
        enterText.setOnClickListener(this);
        date=(Spinner)findViewById(R.id.date);
        time=(Spinner)findViewById(R.id.time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addlesson:
                add();
                break;
            case R.id.complete:
                returnEnter();
                break;
            case R.id.deletelesson:
                delete();
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent,View view,int position,long id){          //选择选项获得对应参数string值
        ddate=String.valueOf(dates[position]);
        dtime=String.valueOf(times[position]);

    }
    public void onNothingSelected(AdapterView<?> parent){
    }

    private void returnEnter() {
        Intent intent = new Intent(this,FormActivity.class);
        startActivity(intent);
        finish();
    }

    public void add() {

        final String lession1 = lession.getText().toString().trim();
        int s1=date.getSelectedItemPosition();
        int s2=time.getSelectedItemPosition();
        String datetime =dates[s1]+times[s2];
        if (TextUtils.isEmpty(lession1)) {  //当手机号没有输入时
            Toast.makeText(this, "科目不能为空！", Toast.LENGTH_SHORT).show();
            lession.requestFocus();//使输入框失去焦点
            return;
        }else{
            Cursor cursor= DataSupport.findBySQL("select * from Form where datetime = ?",datetime);     //查询此课程时间下数据库是否有值"select * from Book where name = ?","589");
            if (cursor.moveToFirst() == true) {                               //有值,修改科目内容
                Form form=new Form();
                form.setLesson(lession1);
                form.updateAll("datetime=? and classes=?",datetime,"1");
                Toast.makeText(this, "课程修改成功", Toast.LENGTH_SHORT).show();
                lession.setText("");
            }else{
                Form form=new Form();                                              //无值，可以新建科目
                form.setClasses("1");
                form.setLesson(lession1);
                form.setDatetime(datetime);
                form.save();
                Toast.makeText(this, "课程加入成功", Toast.LENGTH_SHORT).show();
                lession.setText("");
            }
            cursor.close();
        }
    }

    public void delete(){
        int s1=date.getSelectedItemPosition();
        int s2=time.getSelectedItemPosition();
        String datetime =dates[s1]+times[s2];
        Cursor cursor= DataSupport.findBySQL("select * from Form where datetime = ?",datetime);     //查询此课程时间下数据库是否有值"select * from Book where name = ?","589");
        if (cursor.moveToFirst() == true) {                               //有值,修改科目内容
            DataSupport.deleteAll(Form.class,"datetime=?",datetime);
            Toast.makeText(this, "课程删除成功", Toast.LENGTH_SHORT).show();
            lession.setText("");
        }else{
            Toast.makeText(this, "当前时间无课程", Toast.LENGTH_SHORT).show();
            lession.setText("");
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {               //退出确认
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent1=new Intent(FromAddActivity.this, FormActivity.class);
            startActivity(intent1);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}