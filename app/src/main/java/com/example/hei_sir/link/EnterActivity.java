package com.example.hei_sir.link;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;


public class EnterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editPerson, editCode,et_password,et_username;
    private TextView textViewR;
    private Button btn;
    private boolean autoLogin = false;
    public static String currentUsername;
    private String currentPassword;
    private boolean progressShow;

    //private ImageView qq, weixin, weibo;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;   //记住密码
    private CheckBox rememberPass;
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_enter_activity);
        LitePal.getDatabase();
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        et_password=(EditText) findViewById(R.id.et_password);
        et_username=(EditText)findViewById(R.id.et_username);
        rememberPass=(CheckBox)findViewById(R.id.remenber_pass);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if(isRemember){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            et_username.setText(account);
            et_password.setText(password);
            rememberPass.setChecked(true);//识别保存账号密码
        }
        init();
    }

    private void init() {
        btn = (Button) findViewById(R.id.bn_common_login);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.bn_forget_password);
        btn.setOnClickListener(this);
        editCode = (EditText) findViewById(R.id.et_password);
        editPerson = (EditText) findViewById(R.id.et_username);
        textViewR = (TextView) findViewById(R.id.tv_register);
        /*qq = (ImageView) findViewById(R.id.iv_qq_login);
        weixin = (ImageView) findViewById(R.id.iv_weixin_login);
        weibo = (ImageView) findViewById(R.id.iv_sina_login);
        qq.setOnClickListener(this);
        weixin.setOnClickListener(this);
        weibo.setOnClickListener(this);*/
        textViewR.setOnClickListener(this);
    }

    /**
     * 点击事件
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_common_login:  //登录按钮
                login(v);
                break;
            case R.id.tv_register:  //注册按钮
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            /*case R.id.iv_qq_login:  //QQ登录
                Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_weixin_login:  //微信登录
                Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_sina_login:    //微博登录
                Toast.makeText(this, "微博登录", Toast.LENGTH_SHORT).show();
                break;*/

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }

    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {

        currentUsername = editPerson.getText().toString().trim(); //去除空格，获取手机号
        currentPassword = editCode.getText().toString().trim();  //去除空格，获取密码

        Cursor cursor= DataSupport.findBySQL("select * from User where user = ? and password = ?",currentUsername,currentPassword);

        if (TextUtils.isEmpty(currentUsername)) { //判断手机号是不是为空
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(currentPassword)) {  //判断密码是不是空
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else if(cursor.moveToFirst() == false) {
            Toast.makeText(this,"账户或密码错误",Toast.LENGTH_SHORT).show();
            et_username.setText("");
            et_password.setText("");
        }else {
            editor = pref.edit();
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            if (rememberPass.isChecked()) {
                editor.putBoolean("remenber_password", true);
                editor.putString("account", account);
                editor.putString("password", password);
            } else {
                editor.clear();
            }
            editor.apply();

            progressShow = true;
            final ProgressDialog pd = new ProgressDialog(EnterActivity.this);  //初始化等待动画
            /**
             * 设置监听
             * */
            pd.setCanceledOnTouchOutside(false);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;   //设置Boolean值为false
                }
            });
            pd.setMessage("正在登录....");  //等待动画的标题
            pd.show();  //显示等待动画

            new Thread(new Runnable() {
                public void run() {
                    //在此处输入操作
                    try {
                        Thread.sleep(2000);  //在此处睡眠两秒
                    } catch (InterruptedException e) {
                    }

                    /**
                     * 两秒之后
                     * */
                    pd.dismiss();    //等待条消失
                    Intent intent = new Intent(EnterActivity.this, MainActivity.class);  //进入主界面
                    intent.putExtra("extra_data",currentUsername);
                    startActivity(intent);  //开始跳转
                    finish();  //finish掉此界面
                }
            }).start();  //开始线程
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {               //退出确认
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用han dler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }



}
