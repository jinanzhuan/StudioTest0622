package com.example.shuwei.imageloader.bean;

import java.io.Serializable;

/**
 * 主界面的初始化对象类
 * Created by shuwei on 2016/6/19.
 */
public class InitMessage implements Serializable {
    private String title;
    private int image;
    private String url;

    public InitMessage() {
    }

    public InitMessage(String title,int image, String url) {
        this.title = title;
        this.image = image;
        this.url=url;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "InitMessage{" +
                "image=" + image +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
