package com.example.hei_sir.link;

/**
 * Created by hei_sir on 2018/1/18.
 */

public class User {
    private String user;
    private String password;
    private String name;
    private String school;
    private String number;
    private String identity;

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

    public String getGradeclass() {
        return gradeclass;
    }

    public void setGradeclass(String gradeclass) {
        this.gradeclass = gradeclass;
    }

    private String gradeclass;
}