package com.example.shuwei.imageloader.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * java程序操作数据库时，对应着一个ORM思想：
 * 一个java类对应一个数据表，java类的一个对象对应数据表的一条记录
 * java类的一个属性对应数据表的一个字段
 *
 * 以下是通过xutils的注解的方式对数据表进行创建
 *
 * Created by shuwei on 2016/6/21.
 */
@Table(name="history_url")
public class HistoryUrl {
    @Column(name = "_id",isId = true)
    private int id;

    @Column(name = "url")
    private String url;

    public HistoryUrl() {
    }

    public HistoryUrl(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryUrl that = (HistoryUrl) o;

        return !(url != null ? !url.equals(that.url) : that.url != null);

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
