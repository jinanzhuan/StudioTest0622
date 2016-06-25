package com.guigu.loaderofimage.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuwei.loaderofimage.R;
import com.guigu.loaderofimage.adapter.ImageAdapter;
import com.guigu.loaderofimage.bean.ImageBean;
import com.guigu.loaderofimage.listener.MyCacheCallBack;
import com.guigu.loaderofimage.util.AppUtils;
import com.guigu.loaderofimage.util.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class WebPicturesActivity extends Activity {
    private TextView tv_picture_titile;
    private GridView gv_picture_layout;
    private ImageView iv_picture_download;
    private CheckBox cb_picture_select;
    private Button btn_picture_stopsearch;
    private ProgressBar pb_picture_bar;
    private List<ImageBean> imageBeanList=new ArrayList<>();
    private String url;
    public static boolean isEdit=false;
    private ImageAdapter adapter;
    private boolean flag=false;//是否停止深度抓取的标记符
    private int position;
    private int selectcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置屏幕常亮
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        setContentView(R.layout.activity_web_pictures);

        //获取控件对象
        tv_picture_titile = (TextView) findViewById(R.id.tv_picture_titile);
        gv_picture_layout = (GridView) findViewById(R.id.gv_picture_layout);
        iv_picture_download = (ImageView) findViewById(R.id.iv_picture_download);
        cb_picture_select = (CheckBox) findViewById(R.id.cb_picture_select);
        btn_picture_stopsearch = (Button)findViewById(R.id.btn_picture_stopsearch);
        pb_picture_bar = (ProgressBar)findViewById(R.id.pb_picture_bar);

        //设置适配器
        adapter = new ImageAdapter(this);
        gv_picture_layout.setAdapter(adapter);

        //初始化数据
        initDate();

        gv_picture_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isEdit = true;
                imageBeanList.get(position).setIschecked(true);
                selectcount=imageBeanList.get(position).ischecked()?selectcount -1:selectcount+1;
                initContent();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initDate() {

        isEdit=false;
        //初始化内容
        initContent();

        //获取从上一个界面得到的数据
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        //对所处状态进行标记
        Constants.state=Constants.S_WEB;

        //联网从这个网址获得html文件，并进行解析
        getHttpFromUrl(url);


    }

    private void initContent() {
        if(!isEdit){//查看状态
            tv_picture_titile.setText("请点击下面网站,开始精彩之旅!");
            iv_picture_download.setVisibility(View.GONE);
            cb_picture_select.setVisibility(View.GONE);
//            pb_picture_bar.setVisibility(View.GONE);
            btn_picture_stopsearch.setVisibility(View.GONE);
        }else{//编辑状态
            tv_picture_titile.setText(selectcount+"/"+imageBeanList.size());
            if(Constants.state==Constants.S_WEB){
                iv_picture_download.setImageResource(R.drawable.icon_s_download_press);
            }else if(Constants.state==Constants.S_Local){
                iv_picture_download.setImageResource(R.drawable.op_del_press);
            }
            iv_picture_download.setVisibility(View.VISIBLE);
            cb_picture_select.setVisibility(View.VISIBLE);
//            btn_picture_stopsearch.setVisibility(View.GONE);
        }
    }

    private void getHttpFromUrl(String url) {
        //清空集合中的数据，防止出现不是本网站的链接
        AppUtils.imageBeanSet.clear();
        imageBeanList.clear();

        //对网址的前缀进行检查，如果不是以http开头，则加上网络协议
        url= AppUtils.checkUrlPre(url);
        Log.i("TAG", "checkUrlPre(url)="+url);

        //用对话框展示，带进度条
        String msg="正在抓取"+url+"网页上的图片";
        AppUtils.showProgressDialog(this, msg, false);

        //调用xutils方法获得html文件
        RequestParams params=new RequestParams(url);
        final String finalUrl = url;
        Log.i("TAG", "finalUrl="+finalUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String html) {
                Log.e("TAG", "html=" + html);
                //根据得到的html文件，调用jsoup对html文件进行解析
                imageBeanList = getImageFromHtml(finalUrl, html);
                adapter.setList(imageBeanList);
                adapter.notifyDataSetChanged();
                AppUtils.dialog.dismiss();

                deepGetFromUrl(html);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(WebPicturesActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                AppUtils.dialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(WebPicturesActivity.this, "onCancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinished() {
                Toast.makeText(WebPicturesActivity.this, "onFinished", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deepGetFromUrl(final String html) {
        flag=false;
        new AlertDialog.Builder(this)
                    .setTitle("请确认")
                    .setMessage(url + "的首页已经抓取完毕，是否进行深度抓取？（请确认是否是在WIFI环境下）")
                    .setPositiveButton("深度抓取", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getUrlFromHtml(html);
                        }
                    })
                    .setNegativeButton("下回吧", null)
                    .show();
    }

    /*
    从html文件中解析二级网址，深度抓取二级网站上的图片
     */

    private void getUrlFromHtml(String html) {
        pb_picture_bar.setVisibility(View.VISIBLE);
        btn_picture_stopsearch.setVisibility(View.VISIBLE);
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href]");
        List<String> useableLinks = AppUtils.getUseableLinks(links, url);
        pb_picture_bar.setMax(useableLinks.size());
        for(int i = 0; i < useableLinks.size(); i++) {
            final String link = useableLinks.get(i);
            RequestParams params=new RequestParams(link);
            x.http().get(params,new MyCacheCallBack<String>(){
                @Override
                public void onSuccess(String html) {
                    super.onSuccess(html);
                    if(flag){
                        Log.e("flag", "flag=" + flag);
                        pb_picture_bar.setVisibility(View.GONE);
                        btn_picture_stopsearch.setVisibility(View.GONE);
                        return;
                    }
                    imageBeanList.addAll(getImageFromHtml(link, html));
                    updateProgress(imageBeanList);
                    adapter.setList(imageBeanList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                }
            });
        }

    }

    private void updateProgress(List<ImageBean> imageBeanList) {
        pb_picture_bar.incrementProgressBy(1);
        tv_picture_titile.setText("在" + url + "中总共抓到" + imageBeanList.size() + "张图片!");
        if(pb_picture_bar.getProgress()==pb_picture_bar.getMax()){
            tv_picture_titile.setText("全部抓取完成, 共抓到" + imageBeanList.size() + "张图片");
            pb_picture_bar.setVisibility(View.GONE);
            btn_picture_stopsearch.setVisibility(View.GONE);
        }

    }


    /*
    从html文件中解析得到有效的图片的url路径
     */
    private List<ImageBean> getImageFromHtml(String url,String html) {
        List<ImageBean> list = AppUtils.parseHtml(url,html);
        Log.e("TAG", "list.size()="+list.size());
        return list;
    }

    /*
    停止深度抓取
     */
    public void stopsearch(View v) {
            flag=true;
        Log.e("flag", "stopsearch="+flag);
    }



}
