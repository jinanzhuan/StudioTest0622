package com.example.shuwei.day0617_applistview;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuwei.day0617_applistview.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lv_main_list;
    private List<AppInfo> list;
    private PopupWindow popupWindow;
    private ScaleAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取listView对象。
        lv_main_list = (ListView) findViewById(R.id.lv_main_list);

        //设置数据
        initData();

        //设置适配器
        MyAdapter adapter = new MyAdapter();
        lv_main_list.setAdapter(adapter);

        animation = new ScaleAnimation(0, 1, 0, 1);
        animation.setDuration(500);

        lv_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private View pwView;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断popupwindow是否为空，如果为空，则创建其对象
                if (popupWindow == null) {

                    //导入popupwindow的布局
                    pwView = View.inflate(MainActivity.this, R.layout.pw_ll, null);

                    int width = view.getWidth() - 150;
                    int height = view.getHeight();
                    //创建popupwindow对象
                    popupWindow = new PopupWindow(pwView, width, height);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());

                    LinearLayout ll_pw_layout1 = (LinearLayout) pwView.findViewById(R.id.ll_pw_layout1);
                    LinearLayout ll_pw_layout2 = (LinearLayout) pwView.findViewById(R.id.ll_pw_layout2);
                    LinearLayout ll_pw_layout3 = (LinearLayout) pwView.findViewById(R.id.ll_pw_layout3);

                    ll_pw_layout1.setOnClickListener(MainActivity.this);
                    ll_pw_layout2.setOnClickListener(MainActivity.this);
                    ll_pw_layout3.setOnClickListener(MainActivity.this);

                }

                //如果popupwindow不为空，且正在运行，则将其关闭
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }


                pwView.startAnimation(animation);
                //展示popupwindow。
                popupWindow.showAsDropDown(view, 150, -view.getHeight());

            }
        });

        lv_main_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    private void initData() {

        list = new ArrayList<>();

        //得到软件管理器
        PackageManager manager = getPackageManager();
        //创建主界面上的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //得到包含应用信息的列表
        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolve : resolveInfos) {
            //得到包名
            String packageName = resolve.activityInfo.packageName;
            //得到图标
            Drawable icon = resolve.loadIcon(manager);
            //得到应用名称
            String appName = resolve.loadLabel(manager).toString();
            //封装应用信息到对象中
            AppInfo appInfo = new AppInfo(icon, appName);
            list.add(appInfo);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_pw_layout1:
                Toast.makeText(MainActivity.this, "启动", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_pw_layout2:
                Toast.makeText(MainActivity.this, "退出", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_pw_layout3:
                Toast.makeText(MainActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;

        }

        popupWindow.dismiss();

    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view = null;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_list_layout, null);
                holder = new ViewHolder();
                convertView.setTag(holder);

                ImageView iv_item_image = (ImageView) convertView.findViewById(R.id.iv_item_image);
                TextView tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);

                holder.iv_item_image = iv_item_image;
                holder.tv_item_name = tv_item_name;
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Drawable icon = list.get(position).getIcon();
            String appName = list.get(position).getAppName();

            holder.iv_item_image.setImageDrawable(icon);
            holder.tv_item_name.setText(appName);

            return convertView;
        }

        private class ViewHolder {
            ImageView iv_item_image;
            TextView tv_item_name;

        }
    }
}
