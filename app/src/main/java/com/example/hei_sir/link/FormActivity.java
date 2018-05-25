package com.example.hei_sir.link;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


public class FormActivity extends AppCompatActivity {

    private TextView Mon01,Mon02,Mon03,Mon04,Mon05,Mon06,Mon07,Mon08,Mon09,Mon10,Mon11,Mon12,
            Tues01,Tues02,Tues03,Tues04,Tues05,Tues06,Tues07,Tues08,Tues09,Tues10,Tues11,Tues12,
            Wed01,Wed02,Wed03,Wed04,Wed05,Wed06,Wed07,Wed08,Wed09,Wed10,Wed11,Wed12,
            Thur01,Thur02,Thur03,Thur04,Thur05,Thur06,Thur07,Thur08,Thur09,Thur10,Thur11,Thur12,
            Fri01,Fri02,Fri03,Fri04,Fri05,Fri06,Fri07,Fri08,Fri09,Fri10,Fri11,Fri12,
            Sat01,Sat02,Sat03,Sat04,Sat05,Sat06,Sat07,Sat08,Sat09,Sat10,Sat11,Sat12,
            Sun01,Sun02,Sun03,Sun04,Sun05,Sun06,Sun07,Sun08,Sun09,Sun10,Sun11,Sun12;
    String[] dates={"Mon","Tues","Wed","Thur","Fri","Sat","Sun"};
    String[] times={"01","02","03","04","05","06","07","08","09","10","11","12"};


    private static final String NAME="item_name";
    private static final String IMAGE="item_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        init();
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormActivity.this,FromAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        Mon01=(TextView)findViewById(R.id.Mon01);Mon02=(TextView)findViewById(R.id.Mon02);Mon03=(TextView)findViewById(R.id.Mon03);Mon04=(TextView)findViewById(R.id.Mon04);
        Mon05=(TextView)findViewById(R.id.Mon05);Mon06=(TextView)findViewById(R.id.Mon06);Mon07=(TextView)findViewById(R.id.Mon07);Mon08=(TextView)findViewById(R.id.Mon08);
        Mon09=(TextView)findViewById(R.id.Mon09);Mon10=(TextView)findViewById(R.id.Mon10);Mon11=(TextView)findViewById(R.id.Mon11);Mon12=(TextView)findViewById(R.id.Mon12);
        Tues01=(TextView)findViewById(R.id.Tues01);Tues02=(TextView)findViewById(R.id.Tues02);Tues03=(TextView)findViewById(R.id.Tues03);Tues04=(TextView)findViewById(R.id.Tues04);
        Tues05=(TextView)findViewById(R.id.Tues05);Tues06=(TextView)findViewById(R.id.Tues06);Tues07=(TextView)findViewById(R.id.Tues07);Tues08=(TextView)findViewById(R.id.Tues08);
        Tues09=(TextView)findViewById(R.id.Tues09);Tues10=(TextView)findViewById(R.id.Tues10);Tues11=(TextView)findViewById(R.id.Tues11);Tues12=(TextView)findViewById(R.id.Tues12);
        Wed01=(TextView)findViewById(R.id.Wed01);Wed02=(TextView)findViewById(R.id.Wed02);Wed03=(TextView)findViewById(R.id.Wed03);Wed04=(TextView)findViewById(R.id.Wed04);
        Wed05=(TextView)findViewById(R.id.Wed05);Wed06=(TextView)findViewById(R.id.Wed06);Wed07=(TextView)findViewById(R.id.Wed07);Wed08=(TextView)findViewById(R.id.Wed08);
        Wed09=(TextView)findViewById(R.id.Wed09);Wed10=(TextView)findViewById(R.id.Wed10);Wed11=(TextView)findViewById(R.id.Wed11);Wed12=(TextView)findViewById(R.id.Wed12);
        Thur01=(TextView)findViewById(R.id.Thur01);Thur02=(TextView)findViewById(R.id.Thur02);Thur03=(TextView)findViewById(R.id.Thur03);Thur04=(TextView)findViewById(R.id.Thur04);
        Thur05=(TextView)findViewById(R.id.Thur05);Thur06=(TextView)findViewById(R.id.Thur06);Thur07=(TextView)findViewById(R.id.Thur07);Thur08=(TextView)findViewById(R.id.Thur08);
        Thur09=(TextView)findViewById(R.id.Thur09);Thur10=(TextView)findViewById(R.id.Thur10);Thur11=(TextView)findViewById(R.id.Thur11);Thur12=(TextView)findViewById(R.id.Thur12);
        Fri01=(TextView)findViewById(R.id.Fri01);Fri02=(TextView)findViewById(R.id.Fri02);Fri03=(TextView)findViewById(R.id.Fri03);Fri04=(TextView)findViewById(R.id.Fri04);
        Fri05=(TextView)findViewById(R.id.Fri05);Fri06=(TextView)findViewById(R.id.Fri06);Fri07=(TextView)findViewById(R.id.Fri07);Fri08=(TextView)findViewById(R.id.Fri08);
        Fri09=(TextView)findViewById(R.id.Fri09);Fri10=(TextView)findViewById(R.id.Fri10);Fri11=(TextView)findViewById(R.id.Fri11);Fri12=(TextView)findViewById(R.id.Fri12);
        Sat01=(TextView)findViewById(R.id.Sat01);Sat02=(TextView)findViewById(R.id.Sat02);Sat03=(TextView)findViewById(R.id.Sat03);Sat04=(TextView)findViewById(R.id.Sat04);
        Sat05=(TextView)findViewById(R.id.Sat05);Sat06=(TextView)findViewById(R.id.Sat06);Sat07=(TextView)findViewById(R.id.Sat07);Sat08=(TextView)findViewById(R.id.Sat08);
        Sat09=(TextView)findViewById(R.id.Sat09);Sat10=(TextView)findViewById(R.id.Sat10);Sat11=(TextView)findViewById(R.id.Sat11);Sat12=(TextView)findViewById(R.id.Sat12);
        Sun01=(TextView)findViewById(R.id.Sun01);Sun02=(TextView)findViewById(R.id.Sun02);Sun03=(TextView)findViewById(R.id.Sun03);Sun04=(TextView)findViewById(R.id.Sun04);
        Sun05=(TextView)findViewById(R.id.Sun05);Sun06=(TextView)findViewById(R.id.Sun06);Sun07=(TextView)findViewById(R.id.Sun07);Sun08=(TextView)findViewById(R.id.Sun08);
        Sun09=(TextView)findViewById(R.id.Sun09);Sun10=(TextView)findViewById(R.id.Sun10);Sun11=(TextView)findViewById(R.id.Sun11);Sun12=(TextView)findViewById(R.id.Sun12);           //定义TextView控件
        TextView[] datetimes={Mon01,Mon02,Mon03,Mon04,Mon05,Mon06,Mon07,Mon08,Mon09,Mon10,Mon11,Mon12,
                Tues01,Tues02,Tues03,Tues04,Tues05,Tues06,Tues07,Tues08,Tues09,Tues10,Tues11,Tues12,
                Wed01,Wed02,Wed03,Wed04,Wed05,Wed06,Wed07,Wed08,Wed09,Wed10,Wed11,Wed12,
                Thur01,Thur02,Thur03,Thur04,Thur05,Thur06,Thur07,Thur08,Thur09,Thur10,Thur11,Thur12,
                Fri01,Fri02,Fri03,Fri04,Fri05,Fri06,Fri07,Fri08,Fri09,Fri10,Fri11,Fri12,
                Sat01,Sat02,Sat03,Sat04,Sat05,Sat06,Sat07,Sat08,Sat09,Sat10,Sat11,Sat12,
                Sun01,Sun02,Sun03,Sun04,Sun05,Sun06,Sun07,Sun08,Sun09,Sun10,Sun11,Sun12};
        for(int i=1;i<8;i++) {
            for (int j=1;j<13;j++) {
                String datetime= dates[i-1].toString()+times[j-1].toString();
                Cursor cursor= DataSupport.findBySQL("select lesson from Form where datetime = ?",datetime);     //查询此课程时间下数据库是否有值
                if (cursor.moveToFirst() == true) {                               //有值,修改科目内容
                    KLog.d(datetime);
                    List<Form> forms=DataSupport.where("datetime = ?",datetime).find(Form.class);
                    for(Form form:forms){
                        datetimes[(i-1)*12+j-1].setText(form.getLesson());
                        KLog.d(datetimes[(i-1)*12+j-1]);
                    }
                    Toast.makeText(this, "课程表加载成功", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
    }
}
