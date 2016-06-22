package com.example.shuwei.imageloader.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuwei.imageloader.R;
import com.example.shuwei.imageloader.adapter.PictureSlidePagerAdapter;
import com.example.shuwei.imageloader.bean.ImageBean;
import com.example.shuwei.imageloader.listener.MyCacheCallBack;
import com.example.shuwei.imageloader.util.AppUtils;
import com.example.shuwei.imageloader.util.Constants;

import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DragImageActivity extends FragmentActivity {
    private ViewPager vp_drag_show;
    private TextView tv_drag_url;
    private TextView tv_drag_pagenum;
    private ImageView iv_drag_download;
    private ImageView iv_drag_share;
    private int position;
    private List<ImageBean> imageBeanList;
    private PictureSlidePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_image);

        //获取控件对象
        vp_drag_show = (ViewPager)findViewById(R.id.vp_drag_show);
        tv_drag_url = (TextView)findViewById(R.id.tv_drag_url);
        tv_drag_pagenum = (TextView)findViewById(R.id.tv_drag_pagenum);
        iv_drag_download = (ImageView)findViewById(R.id.iv_drag_download);
        iv_drag_share = (ImageView)findViewById(R.id.iv_drag_share);

        //隐藏actionbar
        getActionBar().hide();

        //获取前一界面发送过来的数据
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        Log.e("TAG", "position"+position);
        imageBeanList = (List<ImageBean>) intent.getSerializableExtra("imageBeanList");
        Log.e("TAG", "imageBeanList----"+imageBeanList.get(position).getUrl());

        //修改界面显示
        tv_drag_url.setText(imageBeanList.get(position).getUrl());
        tv_drag_pagenum.setText((position+1)+"/"+imageBeanList.size());

        if(Constants.state==Constants.S_WEB){
            iv_drag_download.setImageResource(R.drawable.icon_s_download_press);
            iv_drag_share.setVisibility(View.GONE);
        }else if(Constants.state==Constants.S_LOCAL){
            iv_drag_download.setImageResource(R.drawable.garbage_media_cache);
            iv_drag_share.setVisibility(View.VISIBLE);
        }

        //设置viewpaper的显示
        adapter = new PictureSlidePagerAdapter(this.getSupportFragmentManager(),imageBeanList);
        vp_drag_show.setAdapter(adapter);
        vp_drag_show.setCurrentItem(position);

        //为此控件增加监听
        vp_drag_show.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //当新的item项被选中的时候的回调
            @Override
            public void onPageSelected(int position) {
                DragImageActivity.this.position=position;
                tv_drag_url.setText(imageBeanList.get(position).getUrl());
                tv_drag_pagenum.setText((position+1)+"/"+imageBeanList.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    /*
    为下载图片设置监听
     */
    public void downloadImage(View v) {
        if(Constants.state==Constants.S_WEB){
            Log.i("TAG", "Constants=" + Constants.S_WEB);
            downloadImage(position);
        }else if(Constants.state==Constants.S_LOCAL){
            Log.i("TAG", "Constants="+Constants.S_LOCAL);
            Bitmap bitmap = BitmapFactory.decodeFile(imageBeanList.get(position).getUrl());
            try {
                setWallpaper(bitmap);
                Toast.makeText(DragImageActivity.this, "设置壁纸成功！", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    为分享图标设置监听
     */
    public void shareImage(View v) {

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file=new File(imageBeanList.get(position).getUrl());
        intent.setDataAndType(Uri.fromFile(file),"image/*");
        startActivity(intent);

    }

    /*
    用xutils下载图片
     */

    private void downloadImage(int position) {
        //如果路径不存在，则创建路径
        File fileDir = new File(Constants.downloadPath);
        if (fileDir.exists()) {
            fileDir.mkdirs();
        }
        final String url = imageBeanList.get(position).getUrl();
        Log.i("TAG", "url="+url);
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(5000);
        final String filePath = Constants.downloadPath + System.currentTimeMillis() + AppUtils.cutImagePath(url);
        Log.i("TAG", "filePath=" + filePath);
        x.http().get(params, new MyCacheCallBack<File>() {//利用自定义的类，显式的回调以下方法
            @Override
            public boolean onCache(File result) {
                Log.e("TAG", "onCache() " + result.getAbsolutePath());
                FileUtil.copy(result.getAbsolutePath(), filePath);

                return true;//返回为true意味着将图片同事保存到一级缓存中
            }

            @Override
            public void onSuccess(File result) {

                Log.e("TAG", "onSuccess" + result.getAbsolutePath());
                FileUtil.copy(result.getAbsolutePath(), filePath);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG", "onError() " + ex.getMessage());
                Toast.makeText(DragImageActivity.this, "图片" + url + "下载失败", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(DragImageActivity.this, "图片下载成功，下载网址为："+url, Toast.LENGTH_SHORT).show();
    }

}
