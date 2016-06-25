package com.guigu.loaderofimage.bean;

import java.io.Serializable;

/**
 * webpictureactivity下的封装对象类
 * Created by shuwei on 2016/6/23.
 */
public class ImageBean implements Serializable{
    private String url;
    private boolean ischecked;

    public ImageBean() {
    }

    public ImageBean(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "url='" + url + '\'' +
                ", ischecked=" + ischecked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageBean imageBean = (ImageBean) o;

        return !(url != null ? !url.equals(imageBean.url) : imageBean.url != null);

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
