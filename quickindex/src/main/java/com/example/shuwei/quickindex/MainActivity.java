package com.example.shuwei.quickindex;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.base.CommonBaseAdapter;
import com.example.adapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private ListView lv_main;
    private TextView tv_main_word;
    private QuickIndex qi_main_quickindex;
    private List<Person> data;
    private Handler handler = new Handler();
    private CommonBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        lv_main = (ListView) findViewById(R.id.lv_main);
        tv_main_word = (TextView) findViewById(R.id.tv_main_word);
        qi_main_quickindex = (QuickIndex) findViewById(R.id.qi_main_quickindex);

        //自定义监听器
        qi_main_quickindex.setOnIndexChangesListener(new QuickIndex.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String text) {
                Log.e("TAG", "onIndexChanged");
                handler.removeCallbacksAndMessages(null);
                tv_main_word.setText(text);
                tv_main_word.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUp() {
                Log.e("TAG", "onUp");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_main_word.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });

        //初始化数据
        initData();

        adapter = new CommonBaseAdapter<Person>(this, data, R.layout.item_main) {
            @Override
            public void convert(ViewHolder holder, int position) {

                Person person = data.get(position);
                holder.setText(R.id.tv_item_title,person.getPinYin().substring(0,1));
                holder.setText(R.id.tv_item_name,person.getName());

                //设置可见性
                if (position == 0) {
                    holder.getView(R.id.tv_item_title).setVisibility(View.VISIBLE);
                } else {
                    String currentChar = person.getPinYin().substring(0, 1);//当前位置的姓名的拼音首字母
                    String preChar=data.get(position - 1).getPinYin().substring(0, 1);
                    if(currentChar.equals(preChar)){
                        holder.getView(R.id.tv_item_title).setVisibility(View.GONE);
                    }else{
                        holder.getView(R.id.tv_item_title).setVisibility(View.VISIBLE);
                    }

                }
            }
        };

        lv_main.setAdapter(adapter);

        qi_main_quickindex.setOnIndexChangesListener(new QuickIndex.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String text) {
                for (int i = 0; i < data.size(); i++) {
                    String currChar = data.get(i).getPinYin().substring(0, 1);
                    if (text.equals(currChar)) {
                        lv_main.setSelection(i);
                        return;
                    }
                }
            }

            @Override
            public void onUp() {

            }
        });

        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, data.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void initData() {
        data = new ArrayList<>();
        // 虚拟数据
        data.add(new Person("张晓飞"));
        data.add(new Person("杨光福"));
        data.add(new Person("胡继群"));
        data.add(new Person("刘畅"));

        data.add(new Person("钟泽兴"));
        data.add(new Person("尹革新"));
        data.add(new Person("安传鑫"));
        data.add(new Person("张骞壬"));

        data.add(new Person("温松"));
        data.add(new Person("李凤秋"));
        data.add(new Person("刘甫"));
        data.add(new Person("娄全超"));
        data.add(new Person("张猛"));

        data.add(new Person("王英杰"));
        data.add(new Person("李振南"));
        data.add(new Person("孙仁政"));
        data.add(new Person("唐春雷"));
        data.add(new Person("牛鹏伟"));
        data.add(new Person("姜宇航"));

        data.add(new Person("刘挺"));
        data.add(new Person("张洪瑞"));
        data.add(new Person("张建忠"));
        data.add(new Person("侯亚帅"));
        data.add(new Person("刘帅"));

        data.add(new Person("乔竞飞"));
        data.add(new Person("徐雨健"));
        data.add(new Person("吴亮"));
        data.add(new Person("王兆霖"));

        data.add(new Person("阿三"));

        Collections.sort(data);//需要data集合中的数据实现comparable接口，并重写compareTo()方法


    }


}
