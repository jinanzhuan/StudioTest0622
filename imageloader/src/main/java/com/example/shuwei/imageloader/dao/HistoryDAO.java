package com.example.shuwei.imageloader.dao;

import com.example.shuwei.imageloader.bean.HistoryUrl;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作数据表的CURD操作，使用xutils中的方法进行操作
 * Created by shuwei on 2016/6/21.
 */
public class HistoryDAO {
    private DbManager.DaoConfig daoConfig;

    //初始化daoconfig，通过配置指明数据库名和版本号
    public HistoryDAO(){
        daoConfig=new DbManager.DaoConfig()
                .setDbName("imageloader.db")
                .setDbVersion(1);
    }

    public List<HistoryUrl> getAll(){
        List<HistoryUrl> list=null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            //查询数据表数据，并返回一个list集合
            list=dbManager.findAll(HistoryUrl.class);
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            try {
                dbManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(list==null){
                list=new ArrayList<>();
            }
        }

        return list;
    }

    public void add(HistoryUrl historyUrl){
        DbManager dbManager = x.getDb(daoConfig);
        try {
            //增加数据表数据，添加的同时，还可修改historurl的id属性值为数据表中添加条目的“_id”
            dbManager.saveBindingId(historyUrl);
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            try {
                dbManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
