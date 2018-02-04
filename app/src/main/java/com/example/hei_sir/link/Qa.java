package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

/**
 * Created by liuzi on 2018/1/29.
 */

public class Qa extends DataSupport{
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String sname;
    private String tname;
    private int imageId;
    private String content;
    private String time;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Qa(String sname,String tname,String time,int imageId,String content,String answer){
        this.sname=sname;
        this.tname=tname;
        this.time=time;
        this.imageId=imageId;
        this.content=content;
        this.answer=answer;
    }
}
