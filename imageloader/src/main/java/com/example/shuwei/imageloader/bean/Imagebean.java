package com.example.shuwei.imageloader.bean;

import java.io.Serializable;

/**
 * Created by shuwei on 2016/6/20.
 */
public class ImageBean implements Serializable {
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

        if (ischecked != imageBean.ischecked) return false;
        return !(url != null ? !url.equals(imageBean.url) : imageBean.url != null);

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (ischecked ? 1 : 0);
        return result;
    }
}
