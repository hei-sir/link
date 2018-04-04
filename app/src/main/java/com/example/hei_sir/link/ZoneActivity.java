package com.example.hei_sir.link;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hei_sir.link.helper.GsonTools;
import com.socks.library.KLog;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ZoneActivity extends AppCompatActivity implements View.OnClickListener {
    private static String userName,name;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒 ");
    String filename=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+"jpg";
    EditText zonecontent;
    TextView complete;
    private Button button;
    private ImageView back,addzoneimage;
    private Uri imageUri;
    private String imagePath;
    private String imagePath1="none";
    public static final int TAKE_PHOTO =1,CHOOSE_PHOTO=2;
    private int photo=0;             //0为未选择照片，1为拍照照片，2为相册照片


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
            Toast.makeText(ZoneActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        Intent intent=getIntent();
        userName=intent.getStringExtra("extra_data");
        zonecontent=(EditText)findViewById(R.id.zone_content);
        init();
    }
    private void init(){
        complete=(TextView) findViewById(R.id.complete);
        complete.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.iv_return);
        back.setOnClickListener(this);
        addzoneimage=(ImageView)findViewById(R.id.add_zoneimage);
        addzoneimage.setOnClickListener(this);
        /*button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_return:
                        Intent intent=new Intent(ZoneActivity.this,Main2Activity.class);
                        intent.putExtra("extra_data",userName);
                        startActivity(intent);  //开始跳转
                        finish();
                        break;
                    case R.id.complete:
                       complete();
                       break;
                    case R.id.add_zoneimage:
                        Toast.makeText(this,"服务器正在完善中，敬请期待",Toast.LENGTH_SHORT).show();
//                        photo=0;
//                        chooseimage();
                        break;
                    /*case R.id.button:
                        photo=0;
                        addimage();
                        break;*/
        }
    }

    public void complete(){
        final String content= zonecontent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {  //当提问没有输入时
            Toast.makeText(this, "写写想要分享的事情把！", Toast.LENGTH_SHORT).show();
            zonecontent.requestFocus();//使输入框失去焦点
            return;
        }else{
            //Qa qa=new Qa(sname, tname, sdf.format(new Date()), R.drawable.qa_red, qaContent.getText().toString(), "","0");
            Cursor cursor= DataSupport.findBySQL("select * from User where user = ?",userName);
            if (cursor.moveToFirst()){
                name=cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();
            //上传数据
            String originAddress = "http://"+ GsonTools.ip+":8080/Test/QaUpServlet";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Zone.USERNAME,userName);
            params.put(Zone.NAME,name);
            params.put(Zone.TIME,sdf.format(new Date()));
            params.put(Zone.CONTENT,content);
            params.put(Zone.AAA,"2");      //代表空间端上传
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

            Zone zone=new Zone(userName,name,sdf.format(new Date()),content,R.mipmap.ic_launcher,"one");
            Log.d("存入的是",imagePath1);
            zone.save();
           /*Log.d("ZoneActivity",userName);
            Log.d("ZoneActivity",name);
            Log.d("ZoneActivity",sdf.format( new Date()));
            Log.d("ZoneActivity",content);*/
            if (photo==0){
                //Toast.makeText(this,"提问成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,Main2Activity.class);
                intent.putExtra("extra_data",userName);
                intent.putExtra("extra_num","0");
                startActivity(intent);
                finish();
            }else if (photo==1){
              //  Toast.makeText(this,"提问成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,Main2Activity.class);
                intent.putExtra("extra_data",userName);
                intent.putExtra("extra_photo","无");
                intent.putExtra("extra_num","1");
                startActivity(intent);
                finish();
              //  Toast.makeText(this,"已拍照",Toast.LENGTH_SHORT).show();
            }else if (photo==2){
               // Toast.makeText(this,"提问成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,Main2Activity.class);
                intent.putExtra("extra_data",userName);
                intent.putExtra("extra_photo",imagePath1);
                intent.putExtra("extra_num","2");
                startActivity(intent);
                finish();
                //Toast.makeText(this,"已从相册选择",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void chooseimage(){
        if (ContextCompat.checkSelfPermission(ZoneActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ZoneActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1
            );
        }else {
            openAlbum();
        }
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    public void addimage(){
        File outputImage=new File(getExternalCacheDir(),"output_image");
        try{
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >=24){
            imageUri= FileProvider.getUriForFile(ZoneActivity.this,"com.example.hei_sir.link.fileprovider",outputImage);
        }else {
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResult){
        switch (requestCode){
            case 1:
                if (grantResult.length>0 && grantResult[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"你拒绝了权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        addzoneimage.setImageBitmap(bitmap);
                        photo=photo+1;
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    if (Build.VERSION.SDK_INT>=19){     //判断手机系统版本号
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        Uri uri=data.getData();
        Log.d("这是相册","第一种"+uri.toString());
        if(DocumentsContract.isDocumentUri(this,uri)){     //如果是document的Uri,则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];    //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){    //如果是content类型的uri，则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){   //如果是file类型的uri，则使用图片路径获取
            imagePath=uri.getPath();
        }
        displayImage(imagePath);       //显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;      //通过Uri和selection来获取图片的真实路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if (imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            photo=photo+2;
            addzoneimage.setImageBitmap(bitmap);
            imagePath1=imagePath;
            Log.d("ZoneActity",imagePath);
        }else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent=new Intent(ZoneActivity.this,Main2Activity.class);
            intent.putExtra("extra_data",userName);
            startActivity(intent);  //开始跳转
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
