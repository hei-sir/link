package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

/**
 * Created by liuzi on 2018/2/22.
 */

public class Zone extends DataSupport {
    private int id;
    private String username;
    private String name;
    private String time;
    private String content;
    private int imageId;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Zone(String username,String name,String time,String content,int imageId,String imagePath){
        this.username=username;
        this.name=name;
        this.time=time;
        this.content=content;
        this.imageId=imageId;
        this.imagePath=imagePath;
    }
}
