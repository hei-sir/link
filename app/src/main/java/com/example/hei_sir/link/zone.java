package com.example.hei_sir.link;

import org.litepal.crud.DataSupport;

/**
 * Created by hei_sir on 2018/1/20.
 */

public class zone extends DataSupport {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private String time;
    private String contect;
    private String imageId;

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

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
