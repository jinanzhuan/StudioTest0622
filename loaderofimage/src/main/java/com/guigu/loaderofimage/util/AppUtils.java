package com.guigu.loaderofimage.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.shuwei.loaderofimage.R;
import com.guigu.loaderofimage.bean.ImageBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.image.ImageOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 对xutils进行初始化
 * Created by shuwei on 2016/6/23.
 */
public final class AppUtils {
    public static Context context;
    public static ProgressDialog dialog;
    public static Set<ImageBean> imageBeanSet = new HashSet<>();

    public static void initContext(Context context) {
        AppUtils.context = context;
    }

    /*
    检查字符串开头是否以http开头
     */
    public static String checkUrlPre(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    /*
    展示进度对话框，可以选择是水平还是圆形的
     */
    public static void showProgressDialog(Context context, String msg, boolean isHorizontaled) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("提示信息");
        dialog.setMessage(msg);
        if (isHorizontaled) {
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        } else {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        dialog.show();
    }

    /*
    使用Jsoup解析html文件，得到后缀是jpg和png的url集合
     */
    public static List<ImageBean> parseHtml(String url, String html) {
        List<ImageBean> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        List<Element> elementList = document.getElementsByTag("img");
        for (Element element : elementList) {
            String src = element.attr("src");
            if (src.toLowerCase().endsWith("jpg") || src.toLowerCase().endsWith("png")) {
                src = checkSrc(url, src);
                Log.i("TAG", "有效的图片网址 ImageBean(src)=" + src);
                ImageBean imageBean = new ImageBean(src);
                if (!imageBeanSet.contains(imageBean) && src.indexOf("/../") == -1) {
                    imageBeanSet.add(imageBean);
                    list.add(imageBean);
                }

            }
        }
        return list;
    }

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

    /*
    检查网址是否符合规范，如果不符合，则进行修改
     */
    public static String checkSrc(String url, String src) {
        if (src.startsWith("http")) {
            url = src;
        } else {
            if (src.startsWith("/")) {
                url = url + src;
            } else {
                url = url + "/" + src;
            }
        }
        return url;
    }

    /*
    通过Elements集合得到有效的二级网站链接集合list
     */

    public static List<String> getUseableLinks(Elements links, String url) {
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();

        for (Element element : links) {
            String href = element.attr("href");
            if (href.equals("")) {
                continue;
            }
            if (href.equals(url)) {
                continue;
            }
            if (href.startsWith("javascript")) {
                continue;
            }
            if (href.startsWith("/")) {
                href = url + href;
            }

            if (!set.contains(href)) {
                set.add(href);
                list.add(href);
            }

            Log.i("TAG", "有效的url=" + href);

        }
        return list;
    }
}
