package com.example.shuwei.day0617_applistview.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by shuwei on 2016/6/17.
 */
public class AppInfo implements Serializable {
    private Drawable icon;
    private String appName;

    public AppInfo() {
    }

    public AppInfo(Drawable icon, String appName) {
        this.icon = icon;
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", appName='" + appName + '\'' +
                '}';
    }
}
