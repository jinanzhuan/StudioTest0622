package com.guigu.loaderofimage.bean;

import java.io.Serializable;

/**
 * maniactivity中对象类
 * Created by shuwei on 2016/6/23.
 */
public class TitleBean implements Serializable {
    private String url;
    private int image;
    private String title;

    public TitleBean() {
    }

    public TitleBean(String title, int image, String url) {
        this.title = title;
        this.image = image;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return "TitleBean{" +
                "url='" + url + '\'' +
                ", image=" + image +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TitleBean titleBean = (TitleBean) o;

        return !(url != null ? !url.equals(titleBean.url) : titleBean.url != null);

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
