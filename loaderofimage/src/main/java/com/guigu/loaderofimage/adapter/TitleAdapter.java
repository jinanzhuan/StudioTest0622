package com.guigu.loaderofimage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shuwei.loaderofimage.R;
import com.guigu.loaderofimage.bean.TitleBean;

import java.util.List;

/**
 * mainactivity中的适配器
 * Created by shuwei on 2016/6/23.
 */
public class TitleAdapter extends BaseAdapter {
    private Context context;
    private List<TitleBean> list;

    public TitleAdapter(Context context, List<TitleBean> list) {
        this.context = context;
        this.list = list;
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
        ViewHolder holder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_title,null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.iv_title_image= (ImageView) convertView.findViewById(R.id.iv_title_image);
            holder.tv_title_title= (TextView) convertView.findViewById(R.id.tv_title_title);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        TitleBean titleBean = list.get(position);
        holder.iv_title_image.setImageResource(titleBean.getImage());
        holder.tv_title_title.setText(titleBean.getTitle());

        return convertView;
    }

    class ViewHolder{
        ImageView iv_title_image;
        TextView tv_title_title;

    }
}
