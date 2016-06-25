package com.guigu.loaderofimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuwei.loaderofimage.R;
import com.guigu.loaderofimage.adapter.TitleAdapter;
import com.guigu.loaderofimage.bean.TitleBean;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TextView tv_main_title;
    private GridView gv_main_layout;
    private List<TitleBean> list;
    private long firstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件对象
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        gv_main_layout = (GridView) findViewById(R.id.gv_main_layout);
        //初始化数据
        initData();
        //初始化适配器,并给gridview设置适配器
        TitleAdapter adapter = new TitleAdapter(this, list);
        gv_main_layout.setAdapter(adapter);

        //设置监听，跳转到下一个界面
        gv_main_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WebPicturesActivity.class);
                String url = list.get(position).getUrl();
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

    }

    /*
    2秒内连续单击退出,并释放系统内存
     */

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(System.currentTimeMillis()-firstTime>2000){
            Toast.makeText(MainActivity.this, "请再单击一次退出", Toast.LENGTH_SHORT).show();
            firstTime=System.currentTimeMillis();
            return true;
        }else{
            //清除缓存（内存）
            x.image().clearMemCache();
            //清除缓存（文件）
            x.image().clearCacheFiles();

        }
            return super.onKeyUp(keyCode, event);
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new TitleBean("图片天堂", R.drawable.i1, "www.ivsky.com/"));
        list.add(new TitleBean("硅谷教育", R.drawable.i2, "www.atguigu.com/"));
        list.add(new TitleBean("新闻图库", R.drawable.i3, "www.cnsphoto.com/"));
        list.add(new TitleBean("MOKO美空", R.drawable.i4, "www.moko.cc/"));
        list.add(new TitleBean("114啦", R.drawable.i5, "www.114la.com/mm/index.htm/"));
        list.add(new TitleBean("动漫之家", R.drawable.i6, "www.donghua.dmzj.com/"));
        list.add(new TitleBean("7k7k", R.drawable.i7, "www.7k7k.com/"));
        list.add(new TitleBean("嘻嘻哈哈", R.drawable.i8, "www.xxhh.com/"));
        list.add(new TitleBean("有意思吧", R.drawable.i9, "www.u148.net/"));
    }

}
