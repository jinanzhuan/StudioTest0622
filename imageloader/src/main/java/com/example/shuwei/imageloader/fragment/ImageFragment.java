package com.example.shuwei.imageloader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shuwei.imageloader.R;
import com.example.shuwei.imageloader.util.AppUtils;

import org.xutils.x;

/**
 * 新建ImageFragment并对item布局对象进行创建
 * Created by shuwei on 2016/6/22.
 */
public class ImageFragment extends Fragment {
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //创建一个View对象：①加载布局文件 ②new一个View对象
        ImageView view = (ImageView) View.inflate(getActivity(), R.layout.item_drag_image, null);

        //设置view的属性
        x.image().bind(view,url, AppUtils.bigImageOptions);


        return view;
    }

    public static Fragment getInstance(String url){
        ImageFragment fragment=new ImageFragment();
        fragment.url=url;
        return fragment;
    }
}
