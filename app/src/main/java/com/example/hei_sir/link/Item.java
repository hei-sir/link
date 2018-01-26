package com.example.hei_sir.link;

/**
 * Created by hei_sir on 2018/1/18.
 */

public class Item {
    private String name;
    private int imageId;
    private String action;
    private String category;

    public String getCategory() {
        return category;
    }

    public String getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
    public Item(String name,int imageId,String action,String category){
        this.name=name;
        this.imageId=imageId;
        this.action=action;
        this.category=category;
    }
}
