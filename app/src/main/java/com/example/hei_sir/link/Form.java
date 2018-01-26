package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

/**
 * Created by hei_sir on 2018/1/13.
 */

public class Form extends DataSupport{
    private String classes;
    private String lesson;
    private String datetime;

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
