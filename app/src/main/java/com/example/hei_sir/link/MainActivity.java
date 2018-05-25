package com.example.hei_sir.link;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v7.widget.GridLayoutManager;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.example.hei_sir.link.helper.HttpCallbackListener;
import com.example.hei_sir.link.helper.HttpUtil;
import com.example.hei_sir.link.helper.HttpUtils;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout mDrawerLayout;
    private CardView mCardView0,mCardView1,mCardView2,mCardView3;
    private TextView txt,name;
    private static boolean isExit = false;
    private static String userName,school,grade,clsses;
    private String notice2,school1,grade1,clsses1,notice1;
    private Dialog mDialog;
    private Button bt_confirm;
    private Button bt_cancel;
    private EditText editText;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {                 //用于返回键的定义
            super.handleMessage(msg);
            isExit = false;
        }
    };
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
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.txt);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ico_sign_01);
        }
        navView.setCheckedItem(R.id.nav_main);
        name=(TextView) findViewById(R.id.username);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        init();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {          //菜单栏监听器
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_info:
                    mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                    //Toast.makeText(MainActivity.this, "这是个人信息", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, Main3Activity.class);  //进入主界面
                        intent1.putExtra("extra_data",userName);
                        startActivity(intent1);  //开始跳转
                        MainActivity.this.finish();
                    break;
                    case R.id.nav_main:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                      //  Toast.makeText(MainActivity.this, "这是主页", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_zone:
                        mDrawerLayout.closeDrawers();                       //关闭滑动菜单
                        //Toast.makeText(MainActivity.this, "这是班级天地", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);  //进入主界面
                        intent.putExtra("extra_data",userName);
                        intent.putExtra("extra_num","0");
                        startActivity(intent);  //开始跳转
                        MainActivity.this.finish();
                        break;
                    default:
                }return true;
            }
        });
    }


    private void init(){
        mCardView0=(CardView)findViewById(R.id.cardView0);
        mCardView0.setOnClickListener(this);
        mCardView1=(CardView)findViewById(R.id.cardView1);
        mCardView1.setOnClickListener(this);
        mCardView2=(CardView)findViewById(R.id.cardView2);
        mCardView2.setOnClickListener(this);
        mCardView3=(CardView)findViewById(R.id.cardView3);
        mCardView3.setOnClickListener(this);

        //获取公告数据！
        new Thread(new Runnable() {
            public void run() {
                String path2 = "http://" + GsonTools.ip + ":8080/Test/NoticeServlet";
                String jsonString2 = HttpUtils.getJsonContent(path2);//从网络获取数据
                List<User> listMe = GsonTools.stringToList(jsonString2, User.class);
                //日志打印
                for (User user : listMe) {
                    Cursor cursor=DataSupport.findBySQL("select * from User");
                    if (cursor.moveToFirst()){
                        String userid;
                        cursor.moveToFirst();
                        do{
                            userid=cursor.getString(cursor.getColumnIndex("user"));
                            if (userid.equals(user.getUser())){
                                ContentValues values=new ContentValues();                  //采用contentValues方法更新数据
                                values.put("notice",user.getNotice());
                                DataSupport.updateAll(User.class,values," user = ?",userid);
                            }
                        }while (cursor.moveToNext());
                    }
                }
            }
        }).start();
        Cursor cursor=DataSupport.findBySQL("select * from User where user=? and identity = ?",userName,"老师");          //后面补上查询
        if (cursor.moveToFirst()==true){
            cursor.moveToFirst();
            notice1=cursor.getString(cursor.getColumnIndex("notice"));
            txt.setText(notice1);
        }else {
            Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
            cursor1.moveToFirst();
            school1=cursor1.getString(cursor1.getColumnIndex("school"));
            grade1=cursor1.getString(cursor1.getColumnIndex("grade"));
            clsses1=cursor1.getString(cursor1.getColumnIndex("clsses"));
            cursor1.close();
            Cursor cursor2=DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? and identity = ?",school1,grade1,clsses1,"老师");
            if (cursor2.moveToFirst()){
                cursor2.moveToFirst();
                notice2=cursor2.getString(cursor2.getColumnIndex("notice"));
                txt.setText(notice2);
            }
            cursor2.close();
        }
        cursor.close();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.cardView0:
                Cursor cursor4= DataSupport.findBySQL("select * from User where user = ? and identity = ?",userName,"老师");
                if (cursor4.moveToFirst()) {
                    showDialog(v);
                }
                cursor4.close();
            break;
            case R.id.cardView1:
                Intent intent=new Intent(MainActivity.this,FormActivity.class);       //进入课程表
                startActivity(intent);
                break;
            case R.id.cardView2:
                Cursor cursor3= DataSupport.findBySQL("select * from User where user = ? and identity = ?",userName,"老师");
                if (cursor3.moveToFirst() == true) {
                    Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
                    String school,grade,clsses;
                    cursor1.moveToFirst();
                    school=cursor1.getString(cursor1.getColumnIndex("school"));
                    grade=cursor1.getString(cursor1.getColumnIndex("grade"));
                    clsses=cursor1.getString(cursor1.getColumnIndex("clsses"));
                    cursor1.close();
                    Cursor cursor2=DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? and identity = ?",school,grade,clsses,"学生");
                    if (cursor2.moveToFirst()){
                        Intent intent1=new Intent(MainActivity.this,Exam1tActivity.class);       //进入教师成绩系统
                        intent1.putExtra("extra_data",userName);
                        startActivity(intent1);
                    }else {
                        Toast.makeText(this,"您所在班级目前没有学生注册",Toast.LENGTH_SHORT).show();
                    }
                    cursor2.close();
                } else {
                    Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
                    String school,grade,clsses;
                    cursor1.moveToFirst();
                    school=cursor1.getString(cursor1.getColumnIndex("school"));
                    grade=cursor1.getString(cursor1.getColumnIndex("grade"));
                    clsses=cursor1.getString(cursor1.getColumnIndex("clsses"));
                    cursor1.close();
                    Cursor cursor2=DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? and identity = ?",school,grade,clsses,"老师");
                    if (cursor2.moveToFirst()){
                        Intent intent2=new Intent(MainActivity.this,Exam1Activity.class);      //进入学生成绩系统
                        intent2.putExtra("extra_data",userName);
                        startActivity(intent2);
                    }else {
                        Toast.makeText(this,"您所在班级目前没有老师注册",Toast.LENGTH_SHORT).show();
                    }
                    cursor2.close();
                }
                cursor3.close();
                break;
            case R.id.cardView3:


                Cursor cursor= DataSupport.findBySQL("select * from User where user = ? and identity = ?",userName,"老师");
                    if (cursor.moveToFirst() == true) {
                        Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
                        String school,grade,clsses;
                        cursor1.moveToFirst();
                        school=cursor1.getString(cursor1.getColumnIndex("school"));
                        grade=cursor1.getString(cursor1.getColumnIndex("grade"));
                        clsses=cursor1.getString(cursor1.getColumnIndex("clsses"));
                        cursor1.close();
                        Cursor cursor2=DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? and identity = ?",school,grade,clsses,"学生");
                        if (cursor2.moveToFirst()){
                            Intent intent1=new Intent(MainActivity.this,QaActivity.class);       //进入教师qa问答系统
                            intent1.putExtra("extra_data",userName);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(this,"您所在班级目前没有学生注册",Toast.LENGTH_SHORT).show();
                        }
                        cursor2.close();
                    } else {
                        Cursor cursor1=DataSupport.findBySQL("select * from User where user = ?",userName);
                        String school,grade,clsses;
                        cursor1.moveToFirst();
                        school=cursor1.getString(cursor1.getColumnIndex("school"));
                        grade=cursor1.getString(cursor1.getColumnIndex("grade"));
                        clsses=cursor1.getString(cursor1.getColumnIndex("clsses"));
                        cursor1.close();
                        Cursor cursor2=DataSupport.findBySQL("select * from User where school = ? and grade = ? and clsses = ? and identity = ?",school,grade,clsses,"老师");
                        if (cursor2.moveToFirst()){
                            Intent intent2=new Intent(MainActivity.this,Qa1sActivity.class);      //进入学生qa问答系统
                            intent2.putExtra("extra_data",userName);
                            startActivity(intent2);
                        }else {
                            Toast.makeText(this,"您所在班级目前没有老师注册",Toast.LENGTH_SHORT).show();
                        }
                        cursor2.close();
                    }
                    cursor.close();
                break;

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){                    //滑动菜单键
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.back:
                Intent intent2=new Intent(MainActivity.this,EnterActivity.class);//返回登陆页面
                startActivity(intent2);
                finish();
                break;
            case R.id.about:
                PackageInfo pkg = null;
                try {
                    pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String appName = pkg.applicationInfo.loadLabel(getPackageManager()).toString();

                String versionName = pkg.versionName;
                Toast.makeText(MainActivity.this, "Version:   "+versionName, Toast.LENGTH_SHORT).show();
                default:
        }
        return true;
    }

    public void showDialog(View view){
        //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
        mDialog = new Dialog(this);
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(this);
        View           dialogView     = inflater.inflate(R.layout.view_dialog, null);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
        WindowManager.LayoutParams lp     = new WindowManager.LayoutParams();
        Window                     window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        window.setAttributes(lp);

        //设置点击其它地方不让消失弹窗
        mDialog.setCancelable(false);
        initDialogView(dialogView);
        initDialogListener();
    }

    private void initDialogView(View view) {
        bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        editText=(EditText)view.findViewById(R.id.edit);
    }

    private void initDialogListener() {

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "请输入公告内容！", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values=new ContentValues();                  //采用contentValues方法更新数据
                    values.put("notice",editText.getText().toString());
                    DataSupport.updateAll(User.class,values," user = ?",userName);
                    Toast.makeText(MainActivity.this,"公告发布成功",Toast.LENGTH_SHORT).show();

                    //上传数据
                    String originAddress = "http://"+GsonTools.ip+":8080/Test/LoginServlet";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(User.USER,userName);
                    params.put(User.NOTICE, editText.getText().toString());
                    params.put(User.STATUS,"2");
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

                mDialog.dismiss();
                init();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
