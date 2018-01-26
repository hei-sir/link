package com.example.hei_sir.link;

/**
 * Created by hei_sir on 2018/1/10.
 */

public class Info {
    private String user;
    private String data;

    public Info(String user,String data){
        this.user=user;
        this.data=data;
    }

    public String getUser(){
        return user;
    }

    public String getData(){
        return data;
    }
}
