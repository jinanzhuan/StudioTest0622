package com.guigu.loaderofimage.activity;

import android.app.Application;

import com.example.shuwei.loaderofimage.BuildConfig;
import com.guigu.loaderofimage.util.AppUtils;

import org.xutils.x;

/**
 * Created by shuwei on 2016/6/23.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //对xutils进行初始化,并需要在功能清单中进行注册
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        //初始保存context对象
        AppUtils.initContext(this);
    }
}
