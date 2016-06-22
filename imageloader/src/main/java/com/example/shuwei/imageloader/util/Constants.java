package com.example.shuwei.imageloader.util;

import android.os.Environment;

/**
 * Created by shuwei on 2016/6/21.
 */
public class Constants {
    public static final int S_WEB=0;
    public static final int S_LOCAL=1;

    public static int state=S_WEB;
    public static final String downloadPath= Environment.getExternalStorageDirectory()+"/loadToLocalImage/";
}
