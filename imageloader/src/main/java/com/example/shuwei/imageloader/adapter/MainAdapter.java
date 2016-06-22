package com.example.shuwei.imageloader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shuwei.imageloader.R;
import com.example.shuwei.imageloader.bean.InitMessage;

import java.util.List;

/**
 * 主界面gridview的适配器
 * Created by shuwei on 2016/6/19.
 */
public class MainAdapter extends BaseAdapter {
    private List<InitMessage> list;
    private Context context;

    public MainAdapter() {
    }

    public MainAdapter(List<InitMessage> list, Context context) {
        this.list = list;
        this.context = context;
    }

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
        if(convertView==null){
            convertView=View.inflate(context, R.layout.grid_main_layout,null);
            holder=new ViewHolder();
            convertView.setTag(holder);

            ImageView iv_grid_image = (ImageView) convertView.findViewById(R.id.iv_grid_image);
            TextView tv_grid_title = (TextView) convertView.findViewById(R.id.tv_grid_title);

            holder.iv_grid_image=iv_grid_image;
            holder.tv_grid_title=tv_grid_title;

        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        holder.iv_grid_image.setImageResource(list.get(position).getImage());
        holder.tv_grid_title.setText(list.get(position).getTitle());

        return convertView;
    }

    private class ViewHolder {
        ImageView iv_grid_image;
        TextView tv_grid_title;

    }
}
