package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

public class Exam extends DataSupport {
    public static String CHINESE="chinese";
    public static String MATH="math";
    public static String ENGLISH="english";
    public static String POLITICS="politics";
    public static String PHYSICS="physics";
    public static String CHEMICAL="chemical";
    public static String EXAMID="examid";
    public static String USERID="userid";
    public static String SCORE="score";
    public static String RANK="rank";
    public static String NAME="name";
    public static String TIME="time";

    private int id;
    private int chinese;
    private int math;
    private int english;
    private int politics;
    private int physics;
    private int chemical;
    private String examId;
    private String userId;
    private int score;
    private String rank;
    private String name;
    private String time;

    public Exam(int chinese,int math,int english,int politics,int physics,int chemical,int score,String examId,String userId,String name,String rank,String time){
        this.chinese=chinese;
        this.math=math;
        this.english=english;
        this.politics=politics;
        this.physics=physics;
        this.chemical=chemical;
        this.score=score;
        this.examId=examId;
        this.userId=userId;
        this.name=name;
        this.rank=rank;
        this.time=time;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChinese() {
        return chinese;
    }

    public void setChinese(int chinese) {
        this.chinese = chinese;
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getPolitics() {
        return politics;
    }

    public void setPolitics(int politics) {
        this.politics = politics;
    }

    public int getPhysics() {
        return physics;
    }

    public void setPhysics(int physics) {
        this.physics = physics;
    }

    public int getChemical() {
        return chemical;
    }

    public void setChemical(int chemical) {
        this.chemical = chemical;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
