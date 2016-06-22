package com.example.shuwei.imageloader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.shuwei.imageloader.R;
import com.example.shuwei.imageloader.activity.WebPictureActivity;
import com.example.shuwei.imageloader.bean.ImageBean;
import com.example.shuwei.imageloader.util.AppUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by shuwei on 2016/6/20.
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
            convertView=View.inflate(context, R.layout.item_pictures,null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.iv_itempic_icon= (ImageView) convertView.findViewById(R.id.iv_itempic_icon);
            holder.iv_itempic_checked= (ImageView) convertView.findViewById(R.id.iv_itempic_checked);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        //装配数据
        ImageBean imagebean = list.get(position);
        //根据得到的url进行联网下载图片，并设置
        x.image().bind(holder.iv_itempic_icon, imagebean.getUrl(), AppUtils.smallImageOptions);

        //根据是否选中设置iv_itempic_checked的显示状态
        if(WebPictureActivity.isEdit){//编辑状态
            holder.iv_itempic_checked.setVisibility(View.VISIBLE);
            if(imagebean.ischecked()){//如果对象被选中，则设置为选中的图片
                holder.iv_itempic_checked.setImageResource(R.drawable.blue_selected);
            }else{//如果对象未被选中，则设置为未选中的图片
                holder.iv_itempic_checked.setImageResource(R.drawable.blue_unselected);
            }
        }else{//非编辑状态
            holder.iv_itempic_checked.setVisibility(View.GONE);
        }

        return convertView;
    }

    public boolean checkImage(int position, boolean check) {
        list.get(position).setIschecked(check);
        return list.get(position).ischecked();
    }

    static class ViewHolder{
        ImageView iv_itempic_icon;
        ImageView iv_itempic_checked;

    }
}
