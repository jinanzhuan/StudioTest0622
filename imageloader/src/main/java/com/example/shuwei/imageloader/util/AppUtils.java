package com.example.shuwei.imageloader.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.shuwei.imageloader.R;

import org.xutils.image.ImageOptions;

/**
 * Created by shuwei on 2016/6/19.
 */
public final class AppUtils {

    public static Context context;

    public static void initContext(Context context) {
        AppUtils.context = context;
    }

    //对下载图片格式进行处理

    public static ImageOptions smallImageOptions;
    public static ImageOptions bigImageOptions;

    static {
        smallImageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER) //等比例放大/缩小到充满长/宽居中显示
                .setLoadingDrawableId(R.drawable.default_image)
                .setFailureDrawableId(R.drawable.default_image)
                        //.setConfig(Bitmap.Config.RGB_565)
                .build();

        bigImageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_INSIDE)//等比例缩小到充满长/宽居中显示, 或原样显示
                .setLoadingDrawableId(R.drawable.default_image)
                .setFailureDrawableId(R.drawable.default_image)
                .setConfig(Bitmap.Config.ARGB_8888)
                .build();
    }

    public static String cutImagePath(String url){
        String res="";
        int start=url.lastIndexOf("/")+1;
        res=url.substring(start);
        return res;
    }


}
