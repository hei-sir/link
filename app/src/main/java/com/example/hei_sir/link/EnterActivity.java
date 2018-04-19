package com.example.hei_sir.link;

import android.app.ProgressDialog;
import android.content.ContentValues;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpUtils;
import com.socks.library.KLog;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class EnterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editPerson, editCode,et_password,et_username;
    private TextView textViewR;
    private Button btn,bfp;
    private boolean autoLogin = false;
    public static String currentUsername,username,school,grade,clsses,sname1,tname1,qsname1,qtname1,school2,grade2,clsses2;
    private String currentPassword,sc,gr,cl,ide,examname;
    private boolean progressShow;
    //用于接收Http请求的servlet的URL地址，请自己定义
    private String originAddress = "http://"+GsonTools.ip+":8080/Test/LoginServlet";
    //用于处理消息的Handler
    Handler mHandler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";

            if ("OK".equals(msg.obj.toString())){
                //getInfo();
                success();

            }else if ("Wrong".equals(msg.obj.toString())){
                Toast.makeText(EnterActivity.this,"账户或密码错误",Toast.LENGTH_SHORT).show();
                et_username.setText("");
                et_password.setText("");
            }else {
                result = msg.obj.toString();
                Toast.makeText(EnterActivity.this,getString(R.string.error_invalid_internet),Toast.LENGTH_SHORT).show();
            }
        }
    };

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
       //Toast.makeText(this,account+"和"+password,Toast.LENGTH_SHORT).show();
        init();
    }

    private void init() {
        btn = (Button) findViewById(R.id.bn_common_login);
        btn.setOnClickListener(this);
        bfp = (Button) findViewById(R.id.bn_forget_password);
        bfp.setOnClickListener(this);
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
            case R.id.bn_forget_password:
                Intent intent1=new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent1);
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

    /*private void getInfo(){

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {

            String path2 = "http://192.168.0.6:8080/Test/test";
            String jsonString2 = HttpUtils.getJsonContent(path2);//从网络获取数据
            List<User> listMe = GsonTools.stringToList(jsonString2, User.class);
            String userName=et_username.getText().toString();
            //日志打印
            for (User user : listMe) {
                if (user.getUser().equals(userName)) {

                    Cursor cursor=DataSupport.findBySQL("select * from User where user = ?",userName);
                    if (cursor.moveToFirst()==false) {
                        KLog.d(userName);
                        KLog.d("这里是数据库");
                        User user1 = new User();         //无值，可以新建用户
                        user1.setUser(userName);
                        user1.setPassword(user.getPassword());
                        user1.setSchool(user.getSchool());
                        user1.setGrade(user.getGrade());
                        user1.setClsses(user.getClsses());
                        user1.setIdentity(user.getIdentity());
                        user1.setName(user.getName());
                        user1.setNumber(user.getNumber());
                        user1.save();
                    }
                }
            }

        }
    });
        thread.start();
}*/




    public void success(){
        editor = pref.edit();
        if (rememberPass.isChecked()) {
            editor.putBoolean("remenber_password", true);
            editor.putString("account", currentUsername);
            editor.putString("password", currentPassword);
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
                //获取个人信息数据和公告数据！
                String path2 = "http://"+GsonTools.ip+":8080/Test/test";
                String jsonString2 = HttpUtils.getJsonContent(path2);//从网络获取数据
                List<User> listMe = GsonTools.stringToList(jsonString2, User.class);
                String userName=et_username.getText().toString();
                //日志打印
                for (User user : listMe) {
                    username = user.getUser();
                    if (user.getUser().equals(userName)) {


                        Cursor cursor = DataSupport.findBySQL("select * from User where user = ?", username);  //搜寻本地是否有当前账户数据
                        if (cursor.moveToFirst() == false) {
                            KLog.d("这里是数据库");
                            KLog.d("新建用户:"+username);
                            User user1 = new User();         //无值，可以新建用户
                            user1.setUser(userName);
                            user1.setPassword(user.getPassword());
                            user1.setSchool(user.getSchool());
                            user1.setGrade(user.getGrade());
                            user1.setClsses(user.getClsses());
                            user1.setIdentity(user.getIdentity());
                            user1.setName(user.getName());
                            user1.setNumber(user.getNumber());
                            KLog.d("存储"+username+"数据");
                            user1.setId(user.getId());
                            user1.setNotice(user.getNotice());
                            user1.setPhoto(user.getPhoto());
                            user1.save();
                        }
                        cursor.close();
                    }
                        Cursor cursor = DataSupport.findBySQL("select * from User where user = ?", username);     //查询当前账户所在班级
                        if (cursor.moveToFirst()) {
                            cursor.moveToFirst();
                            school = cursor.getString(cursor.getColumnIndex("school"));
                            grade = cursor.getString(cursor.getColumnIndex("grade"));
                            clsses = cursor.getString(cursor.getColumnIndex("clsses"));
                        }
                        cursor.close();

                        if (user.getSchool().equals(school)&&user.getGrade().equals(grade)&&user.getClsses().equals(clsses)) {                                  //循环判断其他属于这个班的账户
                            KLog.d(username+ "     "+school+"      "+grade+"      "+clsses);
                            Cursor cursor1 = DataSupport.findBySQL("select * from User where user=? ",user.getUser());
                            if (cursor1.moveToFirst()==false) {
                                KLog.d("新建用户:"+user.getUser());
                                User user1 = new User();         //无值，可以新建用户
                                user1.setUser(user.getUser());
                                user1.setPassword(user.getPassword());
                                user1.setSchool(user.getSchool());
                                user1.setGrade(user.getGrade());
                                user1.setClsses(user.getClsses());
                                user1.setIdentity(user.getIdentity());
                                user1.setName(user.getName());
                                user1.setNumber(user.getNumber());
                                KLog.d(user.getUser()+"      "+user.getSchool()+"      "+user.getGrade()+"       "+user.getClsses());
                                user1.setId(user.getId());
                                user1.setNotice(user.getNotice());
                                user1.setPhoto(user.getPhoto());
                                user1.save();

                            }else{
                                KLog.d("更新用户:"+user.getUser());
                                ContentValues values=new ContentValues();                  //采用contentValues方法更新数据
                                values.put("notice",user.getNotice());
                                values.put("photo",user.getPhoto());
                                values.put("name",user.getName());
                                values.put("number",user.getNumber());
                                DataSupport.updateAll(User.class,values,"user=?", user.getUser());
                            }
                        }

                    KLog.d("用户系统更新完成");
                }//在此处输入操作
                   //c重复刷新，拉去漏数据
                for (User user : listMe) {
                        if (user.getSchool().equals(school)&&user.getGrade().equals(grade)&&user.getClsses().equals(clsses)) {                                  //循环判断其他属于这个班的账户
                            KLog.d(username+ "     "+school+"      "+grade+"      "+clsses);
                            Cursor cursor1 = DataSupport.findBySQL("select * from User where user=? ",user.getUser());
                            if (cursor1.moveToFirst()==false) {
                                KLog.d("新建用户:"+user.getUser());
                                User user1 = new User();         //无值，可以新建用户
                                user1.setUser(user.getUser());
                                user1.setPassword(user.getPassword());
                                user1.setSchool(user.getSchool());
                                user1.setGrade(user.getGrade());
                                user1.setClsses(user.getClsses());
                                user1.setIdentity(user.getIdentity());
                                user1.setName(user.getName());
                                user1.setNumber(user.getNumber());
                                KLog.d(user.getUser()+"      "+user.getSchool()+"      "+user.getGrade()+"       "+user.getClsses());
                                user1.setId(user.getId());
                                user1.setNotice(user.getNotice());
                                user1.setPhoto(user.getPhoto());
                                user1.save();

                            }else{
                                KLog.d("更新用户:"+user.getUser());
                                ContentValues values=new ContentValues();                  //采用contentValues方法更新数据
                                values.put("notice",user.getNotice());
                                values.put("photo",user.getPhoto());
                                values.put("name",user.getName());
                                values.put("number",user.getNumber());
                                DataSupport.updateAll(User.class,values,"user=?", user.getUser());
                            }
                        }
                    }
                Cursor cursor=DataSupport.findBySQL("select * from User");
                if (cursor.moveToFirst()){
                    cursor.moveToFirst();
                    do {
                        KLog.d(cursor.getCount()+"    "+cursor.getString(cursor.getColumnIndex("user")));
                    }while (cursor.moveToNext());
                }
                cursor.close();


                //获取问答系统数据！
                String path = "http://" + GsonTools.ip + ":8080/Test/QaServet";
                String jsonString = HttpUtils.getJsonContent(path);//从网络获取数据
                List<Qa> list = GsonTools.stringToList(jsonString, Qa.class);

                for (Qa qa : list) {
                    Cursor c = DataSupport.findBySQL("select * from User where user = ?", et_username.getText().toString());
                    KLog.d("进入问答循环：username=" + et_username.getText().toString());
                    if (c.moveToFirst()) {
                        c.moveToFirst();
                        qsname1 = c.getString(c.getColumnIndex("name"));
                        KLog.d(qsname1);
                        qtname1 = c.getString(c.getColumnIndex("name"));
                    }
                    c.close();
                    KLog.d("进入Json循环" + "   " + qa.getSname());
                    if (qa.getSname().equals(qsname1)) {
                        sname1 = qa.getSname();
                        KLog.d("进入学生循环" + "   " + qa.getSname());
                        Cursor cursor1 = DataSupport.findBySQL("select * from Qa where sname = ? and content=?", sname1, qa.getContent());  //搜寻本地是否有当前账户数据
                        if (cursor1.moveToFirst() == false) {
                            KLog.d("这里是数据库" + sname1);
                            KLog.d("新建学生问题:" + qa.getContent());
                            if (qa.getAnswer() == null) {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_red, qa.getContent(), null, qa.getStatus());
                                qa1.save();
                            } else {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_green, qa.getContent(), qa.getAnswer(), qa.getStatus());
                                qa1.save();
                            }
                        } else {
                            KLog.d("更新学生问题:" + sname1 + qa.getContent());
                            ContentValues values = new ContentValues();                  //采用contentValues方法更新数据
                            values.put("answer", qa.getAnswer());
                            values.put("time", qa.getTime());
                            values.put("status", qa.getStatus());
                            KLog.d(qa.getSname() + "+" + qa.getTname() + "+" + qa.getContent() + "+" + qa.getAnswer() + "+" + qa.getStatus() + "+" + qa.getTime());
                            DataSupport.updateAll(Qa.class, values, " sname=? and content=?", qa.getSname(), qa.getContent());
                        }
                        cursor1.close();
                    } else if (qa.getTname().equals(qtname1)) {
                        tname1 = qa.getTname();
                        Cursor cursor1 = DataSupport.findBySQL("select * from Qa where tname = ? and content=?", tname1, qa.getContent());  //搜寻本地是否有当前账户数据
                        if (cursor1.moveToFirst() == false) {
                            KLog.d(tname1);
                            KLog.d("这里是数据库");
                            KLog.d("新建教师问题:" + qa.getContent());
                            if (qa.getStatus().equals("0")) {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_red, qa.getContent(), null, qa.getStatus());
                                qa1.save();
                            } else {
                                Qa qa1 = new Qa(qa.getSname(), qa.getTname(), qa.getTime(), R.drawable.qa_green, qa.getContent(), qa.getAnswer(), qa.getStatus());
                                qa1.save();
                            }
                        } else {
                            KLog.d("更新老师问题:" + tname1 + qa.getContent());
                            ContentValues values = new ContentValues();                  //采用contentValues方法更新数据
                            values.put("answer", qa.getAnswer());
                            values.put("time", qa.getTime());
                            values.put("status", qa.getStatus());
                            DataSupport.updateAll(Qa.class, values, " tname=? and content=?", qa.getTname(), qa.getContent());
                        }
                        cursor1.close();
                    }


                }//在此处输入操作

                Cursor cursor2 = DataSupport.findBySQL("select * from Qa");
                if (cursor2.moveToFirst()) {
                    cursor2.moveToFirst();
                    do {
                        KLog.d(cursor2.getCount() + "    " + cursor2.getString(cursor2.getColumnIndex("content")));
                    } while (cursor2.moveToNext());
                }
                cursor2.close();

                //获取空间数据！
                String path3 = "http://"+GsonTools.ip+":8080/Test/ZoneServlet";
                String jsonString3 = HttpUtils.getJsonContent(path3);//从网络获取数据
                List<Zone> listzone = GsonTools.stringToList(jsonString3,Zone.class);
                String name=et_username.getText().toString();
                //日志打印
                for (Zone zone : listzone) {


                        Cursor cursor1 = DataSupport.findBySQL("select * from Zone where username = ? and content=?", zone.getUsername(),zone.getContent());  //搜寻本地是否有当前账户数据
                        if (cursor1.moveToFirst() == false) {
                            KLog.d("这里是数据库");
                            KLog.d("新建用户:"+zone.getUsername());
                            Zone zone1 = new Zone(zone.getUsername(),zone.getName(),zone.getTime(),zone.getContent(),zone.getImagePath());         //无值，可以新建用户
                            zone1.save();
                        }else {
                            KLog.d("更新用户:" + zone.getUsername());
                            ContentValues values = new ContentValues();                  //采用contentValues方法更新数据
                            values.put("name", zone.getName());
                            DataSupport.updateAll(Zone.class, values, "username=? and content = ?", zone.getUsername(),zone.getContent());
                        }
                        cursor1.close();



                    KLog.d("用户系统更新完成");
                }//在此处输入操作

                Cursor cursor3=DataSupport.findBySQL("select * from Zone");
                if (cursor3.moveToFirst()){
                    cursor3.moveToFirst();
                    do {
                        KLog.d(cursor3.getCount()+"    "+cursor3.getString(cursor3.getColumnIndex("content")));
                    }while (cursor.moveToNext());
                }
                cursor3.close();

                //获取成绩数据
                String path4 = "http://" + GsonTools.ip + ":8080/Test/ExamServlet";
                String jsonString4 = HttpUtils.getJsonContent(path4);//从网络获取数据
                List<Exam> listexam = GsonTools.stringToList(jsonString4, Exam.class);

                for (Exam exam : listexam) {
                    String lexamId;
                    Cursor cursor1 = DataSupport.findBySQL("select * from User where user = ?", et_username.getText().toString());
                    KLog.d("进入问答循环：username=" + et_username.getText().toString());
                    if (cursor1.moveToFirst()) {
                        cursor1.moveToFirst();
                        examname = cursor1.getString(cursor1.getColumnIndex("name"));
                        KLog.d(examname);
                        sc=cursor1.getString(cursor1.getColumnIndex("school"));
                        gr=cursor1.getString(cursor1.getColumnIndex("grade"));
                        cl=cursor1.getString(cursor1.getColumnIndex("clsses"));
                        ide=cursor1.getString(cursor1.getColumnIndex("identity"));
                    }
                    cursor1.close();
                    if (ide.equals("学生")) {
                        Cursor c = DataSupport.findBySQL("select * from Exam where userId = ? and examId=?", userName, exam.getExamId());        //搜索本地账户是否有数据
                        KLog.d("进入问答循环：username=" + userName + "examId" + exam.getExamId());
                        if (c.moveToFirst() == false) {
                            //无值，可以新建成绩
                            KLog.d("判断数据库无值");
                            Exam exam1 = new Exam(exam.getChinese(), exam.getMath(), exam.getEnglish(), exam.getPolitics(), exam.getPhysics(), exam.getChemical(), exam.getScore(), exam.getExamId(), exam.getUserId(), exam.getName(), exam.getRank(), exam.getTime());
                            KLog.d("新建" + exam.getExamId());
                            exam1.save();
                        } else {
                            c.moveToFirst();
                            KLog.d("判断数据库有值");
                            List<Exam> exams = DataSupport.findAll(Exam.class);
                            for (Exam exam1 : exams) {
                                KLog.d(exam1.getExamId() + "已存在");
                            }
                        }
                        Log.d("完成", "全部");
                        c.close();
                    }else if(ide.equals("老师")){
                        Cursor cursor4=DataSupport.findBySQL("select * from User where school=? and grade = ? and clsses = ?",sc,gr,cl);
                        if (cursor4.moveToFirst()==false){
                            KLog.d("该班级没有同学");
                        }else{
                            cursor4.moveToFirst();
                            do {
                                KLog.d(cursor4.getString(cursor4.getColumnIndex("user")));
                                if (exam.getUserId().equals(cursor4.getString(cursor4.getColumnIndex("user")))){
                                    Cursor c = DataSupport.findBySQL("select * from Exam where userId = ? and examId=?",cursor4.getString(cursor4.getColumnIndex("user")) , exam.getExamId());        //搜索本地账户是否有数据
                                    KLog.d("进入问答循环：username=" + userName + "examId" + exam.getExamId());
                                    if (c.moveToFirst() == false) {
                                        //无值，可以新建成绩
                                        KLog.d("判断数据库无值");
                                        Exam exam1 = new Exam(exam.getChinese(), exam.getMath(), exam.getEnglish(), exam.getPolitics(), exam.getPhysics(), exam.getChemical(), exam.getScore(), exam.getExamId(), exam.getUserId(), exam.getName(), exam.getRank(), exam.getTime());
                                        KLog.d("新建" + exam.getExamId());
                                        exam1.save();
                                    } else {
                                        c.moveToFirst();
                                        KLog.d("判断数据库有值");
                                        List<Exam> exams = DataSupport.findAll(Exam.class);
                                        for (Exam exam1 : exams) {
                                            KLog.d(exam1.getExamId() + "已存在");
                                        }
                                    }
                                    Log.d("完成", "全部");
                                    c.close();
                                }
                            }while (cursor4.moveToNext());
                        }
                    }
                }




                try {
                    Thread.sleep(1000);  //在此处睡眠两秒
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

    /**
     * 登录
     *
     * @param view
     */

    public void login(View view) {

        currentUsername = editPerson.getText().toString().trim(); //去除空格，获取手机号
        currentPassword = editCode.getText().toString().trim();  //去除空格，获取密码

        //Cursor cursor= DataSupport.findBySQL("select * from User where user = ? and password = ?",currentUsername,currentPassword);

        if (TextUtils.isEmpty(currentUsername)) { //判断手机号是不是为空
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(currentPassword)) {  //判断密码是不是空
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //构造HashMap
           HashMap<String, String> params = new HashMap<String, String>();
            params.put(User.USER, editPerson.getText().toString());
            params.put(User.PASSWORD, editCode.getText().toString());
            params.put(User.STATUS,"1");
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}
