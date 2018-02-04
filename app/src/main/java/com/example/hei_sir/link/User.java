package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

/**
 * Created by hei_sir on 2018/1/18.
 */

public class User extends DataSupport{
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

}