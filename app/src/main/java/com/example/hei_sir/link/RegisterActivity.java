package com.example.hei_sir.link;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpCallbackListener;
import com.example.hei_sir.link.helper.HttpUtil;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextP,editTextCT,editname,editschool,editnumber,editphone;
    private Button button;
    private TextView enterText;
    private ImageView returnImage;
    private String userok;
    private static String passwordMd5;
    Spinner identity,grades,classes;

    String[] identitys={"老师","学生"};
    String[] gradeses={"1","2","3","4","5","6","7","8","9","10","11","12"};
    String[] classeses={"1","2","3","4","5","6","7","8","9","10"};
    private String originAddress = "http://"+ GsonTools.ip+":8080/Test/LoginServlet";
    private String uidentity,ugrades,uclasses;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";
            if ("OK".equals(msg.obj.toString())){
                result = "success";
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
                startActivity(intent);
                finish();
            }else if ("Wrong".equals(msg.obj.toString())){
                result = "fail";
                Toast.makeText(RegisterActivity.this,"用户名已存在，请修改", Toast.LENGTH_SHORT).show();
            }else if ("WrongNum".equals(msg.obj.toString())){
                result = "fail";
                Toast.makeText(RegisterActivity.this,"学号/工号已存在，请修改", Toast.LENGTH_SHORT).show();
            }else if ("WrongId".equals(msg.obj.toString())){
                result = "fail";
                Toast.makeText(RegisterActivity.this,"该班级已有班主任注册，请检查后重试", Toast.LENGTH_SHORT).show();
            }else {
                result = msg.obj.toString();
                Toast.makeText(RegisterActivity.this, getString(R.string.error_invalid_internet), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);
       init();
    }

    private void init() {
        editTextP = (EditText) findViewById(R.id.et_user);//账户
        //editSMS = (EditText) findViewById(R.id.et_sms_code);  //短信验证码
        editTextCT = (EditText) findViewById(R.id.et_password);//密码
        editname=(EditText)findViewById(R.id.et_name);
        editschool=(EditText)findViewById(R.id.et_school);
        editnumber=(EditText)findViewById(R.id.et_number);
        editphone=(EditText)findViewById(R.id.et_phone);
        identity=(Spinner)findViewById(R.id.identity);
        grades=(Spinner)findViewById(R.id.grades);
        classes=(Spinner)findViewById(R.id.classes);
        button = (Button) findViewById(R.id.bn_immediateRegistration);//注册键
        button.setOnClickListener(this);
        enterText = (TextView) findViewById(R.id.tv_enter);//右上角登陆键
        enterText.setOnClickListener(this);
       // SMSBtn = (Button) findViewById(R.id.bn_sms_code);
        //SMSBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_immediateRegistration:
                register();
                break;
            case R.id.tv_enter:
                returnEnter();
                break;
            /*case R.id.bn_sms_code:
                final String username = editTextP.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
                    editTextP.requestFocus();
                }else {
                    Toast.makeText(this, "验证码获取成功", Toast.LENGTH_SHORT).show();
                }
                break;*/
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){         //给spinner数据定位
        uidentity=String.valueOf(identitys[position]);
        uclasses=String.valueOf(classeses[position]);
        ugrades=String.valueOf(gradeses[position]);
    }

    public void onNothingSelected(AdapterView<?> parent){
    }

    private void returnEnter() {
        RegisterActivity.this.finish();
    }

    public void register() {
        final String username = editTextP.getText().toString().trim();
        final String password = editTextCT.getText().toString().trim();
        final String name=editname.getText().toString().trim();
        final String school=editschool.getText().toString().trim();
        final String number=editnumber.getText().toString().trim();
        final String phone=editphone.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {  //当手机号没有输入时
            Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();//使输入框失去焦点
            return;
        }else if(username.length()<6){      //用户名小于6位
            Toast.makeText(this, "用户名长度不能小于6位！", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(password)) {//当注册密码没有输入时
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            editTextCT.requestFocus();//使输入框失去焦点
            return;
        } else if(password.length()<8){
            Toast.makeText(this, "密码长度不能小于8位！", Toast.LENGTH_SHORT).show();
            editTextCT.requestFocus();//使输入框失去焦点
            return;
        } else if (TextUtils.isEmpty(name)) {//当验证码没有输入时
            Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
            editname.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(phone)) {//当验证码没有输入时
            Toast.makeText(this, "联系电话不能为空！", Toast.LENGTH_SHORT).show();
            editphone.requestFocus();//使输入框失去焦点
            return;
        }else if (phone.length()!=11) {//当验证码没有输入时
            Toast.makeText(this, "手机号码应为11位", Toast.LENGTH_SHORT).show();
            editphone.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(school)) {//当验证码没有输入时
            Toast.makeText(this, "学校不能为空！", Toast.LENGTH_SHORT).show();
            editschool.requestFocus();//使输入框失去焦点
            return;
        }else if (TextUtils.isEmpty(number)) {//当验证码没有输入时
            Toast.makeText(this, "学号/职工号不能为空！", Toast.LENGTH_SHORT).show();
            editnumber.requestFocus();//使输入框失去焦点
            return;
        } else {          //如果全部都已填写，则进行注册操作



            int s1=identity.getSelectedItemPosition();                          //注册的操作放在此处
            int s2=grades.getSelectedItemPosition();
            int s3=classes.getSelectedItemPosition();
            String spinnerid=identitys[s1];
            String spinnergr=gradeses[s2];
            String spinnercl=classeses[s3];
            User user=new User();
            user.setUser(username);
            user.setPassword(password);
            user.setName(name);
            user.setSchool(school);
            user.setGrade(spinnergr);
            user.setIdentity(spinnerid);
            user.setClsses(spinnercl);
            user.setNumber(number);
            user.save();

            List<User> user1=DataSupport.where("user=?",username).find(User.class);
            for (User user2:user1){
                passwordMd5=user2.getPassword().toString();
            }
            DataSupport.deleteAll(User.class,"user = ?",username);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(User.USER, editTextP.getText().toString());
            params.put(User.PASSWORD,passwordMd5);
            params.put(User.NAME, editname.getText().toString());
            params.put(User.PHONE, editphone.getText().toString());
            params.put(User.SCHOOL, editschool.getText().toString());
            params.put(User.NUMBER,editnumber.getText().toString());
            params.put(User.IDENTITY,spinnerid);
            params.put(User.GRADE,spinnergr);
            params.put(User.CLSSES,spinnercl);

            params.put(User.STATUS,"0");
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
//            Cursor cursor= DataSupport.findBySQL("select * from User where user = ?",username);     //查询此用户名下数据库是否有值"select * from Book where name = ?","589");
//            Cursor cursor1=DataSupport.findBySQL("select * from User where school = ? and number= ?and identity = ?",school,number,spinnerid);
//            if (cursor.moveToFirst() == true) {                        //有值,提示用户名已被注册
//                Toast.makeText(RegisterActivity.this, "用户名已被注册", Toast.LENGTH_SHORT).show();
//                editTextP.setText("");
//            }else if(cursor1.moveToFirst() == true) {
//                Toast.makeText(RegisterActivity.this,"信息已存在,请确认学号（职工号）和学校信息是否无误",Toast.LENGTH_SHORT).show();
//                editschool.setText("");
//                editnumber.setText("");
//            }else{
//                User user=new User();         //无值，可以新建用户
//                user.setUser(username);
//                user.setPassword(password);
//                user.setSchool(school);
//                user.setGrade(spinnergr);
//                user.setClsses(spinnercl);
//                user.setIdentity(spinnerid);
//                user.setName(name);
//                user.setNumber(number);
//                user.save();
//                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(RegisterActivity.this, EnterActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            cursor.close();
//            cursor1.close();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {               //退出确认
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
