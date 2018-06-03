package com.example.hei_sir.link;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Contact2Activity extends AppCompatActivity implements View.OnClickListener {
    public static final String PHONE="phone";
    public static final String NAME="name";
    public static final String RED="red";
    public static final String GREEN="green";
    public static final String BLUE="blue";
    private ImageView imageView;
    private TextView phone1;
    private CardView call,msn;
    private static String name,phone,red,green,blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact2);
        Intent intent=getIntent();
        phone=intent.getStringExtra(PHONE);
        name=intent.getStringExtra(NAME);
        red=intent.getStringExtra(RED);
        green=intent.getStringExtra(GREEN);
        blue=intent.getStringExtra(BLUE);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_tollbar);
        phone1=(TextView)findViewById(R.id.phone1);
        call=(CardView)findViewById(R.id.call);
        msn=(CardView)findViewById(R.id.msn);
        msn.setOnClickListener(this);
        call.setOnClickListener(this);
        imageView=(ImageView)findViewById(R.id.image_view);
        phone1.setText(phone+"的家长");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(name);
//        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(Integer.parseInt(red),Integer.parseInt(green),Integer.parseInt(blue)));
        //Glide.with(this).load(R.drawable.exam).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Contact2Activity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void call(){
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }

    private void msn(){
        Uri uri = Uri.parse("smsto:"+phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "");
        startActivity(it);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call:
                    if (ContextCompat.checkSelfPermission(Contact2Activity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(Contact2Activity.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    }else {
                        call();
                    }
                break;
            case R.id.msn:
                if (ContextCompat.checkSelfPermission(Contact2Activity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Contact2Activity.this,new String[]{Manifest.permission.SEND_SMS},1);
                }else {
                    msn();
                }
                break;
                default:
        }

    }

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
