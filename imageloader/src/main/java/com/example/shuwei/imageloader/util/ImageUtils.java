package com.example.shuwei.imageloader.util;

import android.widget.Toast;

import com.example.shuwei.imageloader.ui.BaiduParams;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

/**
 * Created by shuwei on 2016/6/20.
 */
public class ImageUtils {


    BaiduParams params=new BaiduParams();





    Callback.Cancelable cancelable
            = x.http().get(params,

            new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    //Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    if (ex instanceof HttpException) { // 网络错误
                        HttpException httpEx = (HttpException) ex;
                        int responseCode = httpEx.getCode();
                        String responseMsg = httpEx.getMessage();
                        String errorResult = httpEx.getResult();
                        // ...
                    } else { // 其他错误
                        // ...
                    }
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinished() {

                }
            });

// cancelable.cancel(); // 取消请求
}
