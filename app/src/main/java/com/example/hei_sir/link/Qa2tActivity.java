package com.example.hei_sir.link;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class Qa2tActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String QA_NAME="qa_name";
    public static final String QA_CONTENT="qa_content";
    private EditText qaAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa2t);
        Intent intent=getIntent();
        String qaNname=intent.getStringExtra(QA_NAME);
        String qaContent=intent.getStringExtra(QA_CONTENT);
        //Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        TextView qaSnameText=(TextView)findViewById(R.id.qa_name);
        TextView qaContentText=(TextView)findViewById(R.id.qa_content);
        qaAnswer=(EditText)findViewById(R.id.qa_answer);
        qaSnameText.setText(qaNname);
        qaContentText.setText(qaContent);
        init();

    }

    private void init(){
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        TextView text = (TextView) findViewById(R.id.complete);
        text.setOnClickListener(this);
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

    }
}
