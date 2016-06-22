package com.example.shuwei.imageloader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.shuwei.imageloader.R;
import com.example.shuwei.imageloader.adapter.MainAdapter;
import com.example.shuwei.imageloader.bean.InitMessage;
import com.example.shuwei.imageloader.dao.HistoryDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    GridView gv_main_list;
    private List<InitMessage> list;
    private HistoryDAO historyDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取list控件对象
        gv_main_list = (GridView) findViewById(R.id.gv_main_list);

        //设置数据
        initData();

        //设置适配器
        MainAdapter adapter = new MainAdapter(list, this);
        gv_main_list.setAdapter(adapter);

        gv_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WebPictureActivity.class);
                String url = list.get(position).getUrl();
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //设置两组选项
        menu.add(Menu.NONE,1,Menu.NONE,"查看历史记录");
        menu.add(Menu.NONE,2,Menu.NONE,"查看本地图片");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId){
            case 1:
                Intent intent=new Intent(MainActivity.this,WebPictureActivity.class);
                intent.putExtra("state", Constants.S_WEB);
                startActivity(intent);
                break;
            case 2:
                Intent intent2=new Intent(MainActivity.this,WebPictureActivity.class);
                intent2.putExtra("state", Constants.S_LOCAL);
                startActivity(intent2);
                break;
        }


        return super.onOptionsItemSelected(item);
    }*/

    private void initData() {
        list = new ArrayList<>();
        list.add(new InitMessage("图片天堂", R.drawable.i1, "www.ivsky.com/"));
        list.add(new InitMessage("硅谷教育", R.drawable.i2, "www.atguigu.com/"));
        list.add(new InitMessage("新闻图库", R.drawable.i3, "www.cnsphoto.com/"));
        list.add(new InitMessage("MOKO美空", R.drawable.i4, "www.moko.cc/"));
        list.add(new InitMessage("114啦", R.drawable.i5, "www.114la.com/mm/index.htm/"));
        list.add(new InitMessage("动漫之家", R.drawable.i6, "www.donghua.dmzj.com/"));
        list.add(new InitMessage("7k7k", R.drawable.i7, "www.7k7k.com/"));
        list.add(new InitMessage("嘻嘻哈哈", R.drawable.i8, "www.xxhh.com/"));
        list.add(new InitMessage("有意思吧", R.drawable.i9, "www.u148.net/"));
    }

    private long lastTime;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastTime > 2000) {
                Toast.makeText(MainActivity.this, "再点击一次，退出应用", Toast.LENGTH_SHORT).show();
                lastTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
