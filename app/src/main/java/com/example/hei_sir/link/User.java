package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

/**
 * Created by hei_sir on 2018/1/18.
 */

public class User extends DataSupport{

    public static String USER= "user";
    public static String PASSWORD = "password";
    public static String NAME="name";
    public static String SCHOOL="school";
    public static String NUMBER="number";
    public static String IDENTITY="identity";
    public static String GRADE="grade";
    public static String CLSSES="clsses";
    public static String NOTICE="notice";
    public static String PHOTO="photo";
    public static String STATUS="status";
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String user;
    private String password;
    private String name;
    private String school;
    private String number;
    private String identity;
    private String grade;
    private String clsses;
    private String notice;
    private String photo;

    public String getPhoto(){return photo;}

    public void setPhoto(String photo){this.photo=photo;}

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClsses() {
        return clsses;
    }

    public void setClsses(String clsses) {
        this.clsses = clsses;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public User(){
        super();
    }

    public User(String user,String name,String identity,String school,String grade,String clsses,String number){
        super();
        this.user=user;
        this.name=name;
        this.identity=identity;
        this.school=school;
        this.grade=grade;
        this.clsses=clsses;
        this.number=number;
    }

}