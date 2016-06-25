package com.guigu.loaderofimage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.shuwei.loaderofimage.R;
import com.guigu.loaderofimage.activity.WebPicturesActivity;
import com.guigu.loaderofimage.bean.ImageBean;
import com.guigu.loaderofimage.util.AppUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by shuwei on 2016/6/23.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<ImageBean> list;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public List<ImageBean> getList() {
        return list;
    }

    public void setList(List<ImageBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list==null)?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return (list==null)?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_picture,null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.iv_item_image= (ImageView) convertView.findViewById(R.id.iv_item_image);
            holder.iv_item_check= (ImageView) convertView.findViewById(R.id.iv_item_check);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
       //数据装配
        String url=list.get(position).getUrl();
        Log.i("TAG", "ImageAdapter+url="+url);
        x.image().bind(holder.iv_item_image,url, AppUtils.smallImageOptions);

        if(WebPicturesActivity.isEdit){
            holder.iv_item_check.setVisibility(View.VISIBLE);
            if(list.get(position).ischecked()){
                holder.iv_item_check.setImageResource(R.drawable.blue_selected);
            }else{
                holder.iv_item_check.setImageResource(R.drawable.blue_unselected);
            }
        }else{
            holder.iv_item_check.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder{
        ImageView iv_item_image;
        ImageView iv_item_check;


    }
}
