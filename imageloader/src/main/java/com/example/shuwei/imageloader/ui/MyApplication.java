package com.example.shuwei.imageloader.ui;

import android.app.Application;

import com.example.shuwei.imageloader.util.AppUtils;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by shuwei on 2016/6/19.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils框架
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        AppUtils.initContext(this);
    }
}
