package com.example.shuwei.imageloader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.shuwei.imageloader.bean.ImageBean;
import com.example.shuwei.imageloader.fragment.ImageFragment;

import java.util.List;

/**
 * FragmentStatePagerAdapter:适用于显示的item较多时，其会适时的销毁item
 * FragmentPagerAdapter：适用于显示item较小的时候，不会销毁item
 * <p>
 * Created by shuwei on 2016/6/22.
 */
public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<ImageBean> list;

    public PictureSlidePagerAdapter(FragmentManager fm, List<ImageBean> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        String url = list.get(position).getUrl();
        return ImageFragment.getInstance(url);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
