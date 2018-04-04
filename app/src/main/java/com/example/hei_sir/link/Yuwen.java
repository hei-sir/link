package com.example.hei_sir.link;

/**
 * Created by liuzi on 2018/3/14.
 */

public class Yuwen {
    private int id;
    private String time;
    private String exam;
    private String user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public Yuwen(String time,String exam,String user){
        this.time=time;
        this.exam=exam;
        this.user=user;
    }
}
