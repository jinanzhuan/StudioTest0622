package com.example.shuwei.imageloader.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shuwei.imageloader.R;
import com.example.shuwei.imageloader.adapter.ImageAdapter;
import com.example.shuwei.imageloader.bean.HistoryUrl;
import com.example.shuwei.imageloader.bean.ImageBean;
import com.example.shuwei.imageloader.dao.HistoryDAO;
import com.example.shuwei.imageloader.listener.MyCacheCallBack;
import com.example.shuwei.imageloader.util.AppUtils;
import com.example.shuwei.imageloader.util.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebPictureActivity extends Activity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private GridView gv_web_list;
    private ProgressBar pb_picture_loading;
    private Button btn_picture_cancelload;
    private ImageView iv_picture_load;
    private CheckBox cb_picture_ischecked;
    private TextView tv_web_url;
    private ImageAdapter adapter;
    public String url;
    private HistoryDAO historyDAO;
    private HistoryUrl historyUrl;
    //标识是否在编辑状态
    public static boolean isEdit = false;
    private int selectCount;
    private SearchView searchView;
    private SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String url) {
            //对url进行检查
            url = checkUrlPre(url);
            //联网操作，获取url对应的网站，返回html,解析后并封装到对象中，并联网显示在list中
            getHttpImages(url);

//            CharSequence query = searchView.getQuery();
            //点击搜索后，文本框内内容清空
            searchView.setQuery(null, true);


            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置屏幕常亮
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        setContentView(R.layout.activity_web_picture);

        //在actionbar的左侧添加返回的标识
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //设置数据
        initData();

        //为gridview设置监听
        gv_web_list.setOnItemClickListener(this);
        //为gridview设置长按监听
        gv_web_list.setOnItemLongClickListener(this);

    }

    /*
    添加主菜单，增加三个选项
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        MenuItem item = menu.findItem(R.id.item_menu_search);
        //得到搜索组件对象
        searchView = (SearchView) item.getActionView();
        //设置其基本属性
        searchView.setQueryHint("请输入网址");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(listener);


        return super.onCreateOptionsMenu(menu);
    }

    /*
    为主菜单设置监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.item_menu_history:
                showHistory();

                break;
            case R.id.item_menu_local:
                showLocalImages();

                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 当选择本地图片时，需要将activity中的各种状态改为非编辑状态。
     * 并从指定的文件夹中获取已经下载的图片并进行展示
     */
    private void showLocalImages() {

        //将状态修改为本地状态
        Constants.state = Constants.S_LOCAL;

        imageBeanList = getLocalImages();

        //根据是本地以及联网状态进行初始化
        initState();

        adapter.setList(imageBeanList);
        adapter.notifyDataSetChanged();
    }

    /*
    从指定的文件夹中获取已经下载的图片
     */
    private List<ImageBean> getLocalImages() {
        List<ImageBean> list = new ArrayList<>();
        File file = new File(Constants.downloadPath);//loadToLocalImage
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                ImageBean imageBean = new ImageBean(files[i].getAbsolutePath());
                list.add(imageBean);
            }
        }
        return list;
    }

    private List<HistoryUrl> historyUrlList;

    /**
     * 通过对话框展示历史记录，并设置各个条目的监听，通过监听可以抓取所选条目网站的图片
     * 历史记录的数据来自于以下途径：
     * 1、上一个activity传过来的url
     * 2、主菜单中输入的url
     */
    private void showHistory() {
        historyDAO = new HistoryDAO();
        historyUrlList = historyDAO.getAll();
        final String[] items = new String[historyUrlList.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = historyUrlList.get(i).getUrl();
        }
        new AlertDialog.Builder(this)
                .setTitle("显示历史记录")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getHttpImages(items[which]);
                    }
                })
                .show();
    }

    /*
    初始化数据
     */
    private void initData() {

        //加载控件对象
        tv_web_url = (TextView) findViewById(R.id.tv_web_url);
        cb_picture_ischecked = (CheckBox) findViewById(R.id.cb_picture_ischecked);
        iv_picture_load = (ImageView) findViewById(R.id.iv_picture_load);
        btn_picture_cancelload = (Button) findViewById(R.id.btn_picture_cancelload);
        pb_picture_loading = (ProgressBar) findViewById(R.id.pb_picture_loading);
        gv_web_list = (GridView) findViewById(R.id.gv_web_list);

        //设置适配器(现在数据为空，后面设置数据后，再调用notify方法)
        adapter = new ImageAdapter(this);
        gv_web_list.setAdapter(adapter);

        //联网下载数据：html
        //1、获取一个前界面发送过来的网站url
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

       /* //针对不同的情况，进行不同的处理
        Constants.state = intent.getIntExtra("state", 1);
        if(Constants.state==Constants.S_WEB){
            showHistory();
        }else if(Constants.state==Constants.S_LOCAL){
            showLocalImages();
        }*/

        Log.e("TAG", "url" + url);

        //对url进行检查，是否符合规范
        url = checkUrlPre(url);

        //2、联网操作，获取url对应的网站，返回html,解析后并封装到对象中，并联网显示在list中
        getHttpImages(url);

    }

    private ProgressDialog dialog;
    //用来显示所有数据的集合
    private List<ImageBean> imageBeanList = new ArrayList<>();
    //用来过滤相同数据的集合
    private Set<ImageBean> imageBeanSet = new HashSet<>();

    /**
     * 联网操作，获取url对应的网站，返回html,解析后并封装到对象中，并联网显示在list中
     *
     * @param url
     */
    private void getHttpImages(final String url) {
        //将状态保存为联网状态
        Constants.state = Constants.S_WEB;

        //添加到数据库，并进行过滤
        addToHistory(url);

        //使用searchview查询其他网址时，需要先初始化
        initState();
        this.url = url;

        //显示一个progressdialog
        showProgressDialog("正在抓取" + url + "网站的图片", false);

        //联网下载，使用xutils
        x.http().get(new RequestParams(url), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String html) {
                imageBeanList.clear();
                imageBeanSet.clear();
                //将html代码解析得到图片的url，并将此url装配到一个Imagebean中，并将Imagebean添加到集合中
                showImageFromHtml(html, url);
                dialog.dismiss();
                //对话框消失后，提示进行深度抓取
                deepSearch(html);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(WebPictureActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    /*
    根据是本地状态以及联网状态，进行不同的初始化，将activity的状态为非编辑状态
     */
    private void initState() {
        if (Constants.state == Constants.S_WEB) {
            Log.e("TAG", "Constants.S_WEB");
            tv_web_url.setText("请在搜索框中输入网站网址");
            iv_picture_load.setImageResource(R.drawable.icon_s_download_press);//将图片进行更换
        }
        if (Constants.state == Constants.S_LOCAL) {
            Log.e("TAG", "Constants.S_LOCAL");
            tv_web_url.setText("在下载文件夹中共有" + imageBeanList.size() + "张图片");
            iv_picture_load.setImageResource(R.drawable.op_del_press);//将图片进行更换
        }
        isEdit = false;
        selectCount = 0;
        cb_picture_ischecked.setChecked(false);
        cb_picture_ischecked.setVisibility(View.GONE);
        iv_picture_load.setVisibility(View.GONE);
    }

    private void addToHistory(String url) {
        //将url封装到HistotyUrl对象中
        historyUrl = new HistoryUrl(-1, url);
        //判断historyUrlList中是否包含historyUrl
        historyDAO = new HistoryDAO();
        historyUrlList = historyDAO.getAll();
        if (!historyUrlList.contains(historyUrl)) {
            //将封装的url对象添加到数据库中
            historyDAO.add(historyUrl);
        }
    }

    /*
    深入抓取二级网站的图片
     */
    private void deepSearch(final String html) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请确认");
        builder.setMessage(url + "的首页已经抓取完毕,是否进行深度抓取?(请确认是否有Wifi环境)");
        builder.setPositiveButton("深度抓取", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进度条和取消按钮状态可见
                pb_picture_loading.setVisibility(View.VISIBLE);
                btn_picture_cancelload.setVisibility(View.VISIBLE);

                List<String> list = parseHtmlToUrl(html);
                //设置进度条的总长度
                pb_picture_loading.setMax(list.size());


                for (int i = 0; i < list.size(); i++) {
                    RequestParams params = new RequestParams(list.get(i));
                    x.http().get(params, new MyCacheCallBack<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (flag) {
                                return;
                            }
                            //对得到result进行解析
                            parseHtml(result, html);

                            adapter.setList(imageBeanList);//设置适配器
                            adapter.notifyDataSetChanged();//通知适配器更新
                            updateProgress();


                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            if (flag) {
                                return;
                            }
                            updateProgress();

                        }
                    });
                }


            }
        });
        builder.setNegativeButton("下回吧", null);
        builder.show();
    }

    /*
    深度抓取时，更新进度条
     */
    private void updateProgress() {
        pb_picture_loading.incrementProgressBy(1);
        tv_web_url.setText("在" + url + "中总共抓到" + imageBeanList.size() + "张图片!");
        if (pb_picture_loading.getProgress() == pb_picture_loading.getMax()) {
            pb_picture_loading.setVisibility(View.GONE);
            tv_web_url.setText("全部抓取完成, 共抓到" + imageBeanList.size() + "张图片");
            btn_picture_cancelload.setVisibility(View.GONE);
        }
    }

    /*
    解析html得到二级网站的url
     */
    private List<String> parseHtmlToUrl(String html) {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("a[href]");
        List<String> urlList = getUseableLinks(links);

        return urlList;
    }

    /*
    遍历二级网站的Url集合，过滤掉相同的网址，保存到集合中
     */
    private List<String> getUseableLinks(Elements links) {
        //用于过滤重复url的集合
        HashSet<String> set = new HashSet<String>();
        //用于保存有效url的集合
        List<String> list = new ArrayList<String>();

        //遍历所有links,过滤,保存有效链接
        for (Element link : links) {
            String href = link.attr("href");// abs:href, "http://"
            //Log.i("spl","过滤前,链接:"+href);
            // 设置过滤条件
            if (href.equals("")) {
                continue;// 跳过
            }
            if (href.equals(url)) {
                continue;// 跳过
            }
            if (href.startsWith("javascript")) {
                continue;// 跳过
            }

            if (href.startsWith("/")) {
                href = url + href;
            }
            if (!set.contains(href)) {
                set.add(href);// 将有效链接保存至哈希表中
                list.add(href);
            }

//            Log.i("spl", "有效链接:" + href);
        }
        return list;

    }

    /*
    解析网址，得到图片url集合，调用xutils框架下载图片并在GridView中显示
     */
    private void showImageFromHtml(String html, String url) {

        parseHtml(html, url);//调用jsoup解析html文件，并得到包含url的集合imageBeanList
        adapter.setList(imageBeanList);//设置适配器
        adapter.notifyDataSetChanged();//通知适配器更新
    }

    /*
    解析html，并得到不重复的url集合
     */
    private void parseHtml(String html, String url) {
        Document doc = Jsoup.parse(html);
        List<Element> imgs = doc.getElementsByTag("img");
        for (Element img : imgs) {
            String src = img.attr("src");
            if (src.toLowerCase().endsWith("jpg") || src.toLowerCase().endsWith("png")) {
                src = checkSrc(url, src);
                Log.i("TAG", "parseHtml---"+src);
                ImageBean imageBean = new ImageBean(src);
                //过滤包含相同的url
                if (!imageBeanSet.contains(imageBean) && src.indexOf("/../") == -1) {
                    imageBeanSet.add(imageBean);
                    imageBeanList.add(imageBean);
                }
            }
        }
    }

    /*
    检查得到的url，保证是绝对路径
     */
    private String checkSrc(String url, String src) {

        if (src.startsWith("http")) {
            url = src;
        } else {
            if (src.startsWith("/")) {
                url = url + src;
            } else {
                url = url + "/" + src;
            }
        }

        return url;

    }

    /*
    进度条的样式设置及展示信息设置
     */
    private void showProgressDialog(String msg, boolean isHorizontal) {
        dialog = new ProgressDialog(this);
        if (isHorizontal) {
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        } else {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        dialog.setTitle("提示信息");
        dialog.setMessage(msg);
        dialog.show();

    }

    /*
    检查url的前缀是否符合规范
     */
    private String checkUrlPre(String url) {
        if (!url.startsWith("http")) {
            Log.i("TAG", "checkUrlPre---"+url);
            url = "http://" + url;
            return url;
        }
        return null;
    }

    private boolean flag = false;

    /*
    button的监听，点击后停止深度抓取
     */
    public void stopsearch(View v) {
        flag = true;
        pb_picture_loading.setVisibility(View.GONE);
        btn_picture_cancelload.setVisibility(View.GONE);
        tv_web_url.setText("抓取完成,共抓到" + imageBeanList.size() + "张图片");
    }


    /*
    为gridview设置长按监听,在编辑状态，如果图片未全部选中，checkbox为非选中状态
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (!isEdit) {//如果在查看状态，则进行以下操作(改变为编辑状态）
            isEdit = true;
            cb_picture_ischecked.setVisibility(View.VISIBLE);
            iv_picture_load.setVisibility(View.VISIBLE);
        }
        boolean ischecked = imageBeanList.get(position).ischecked();
        selectCount = ischecked ? selectCount - 1 : selectCount + 1;
        adapter.checkImage(position, !ischecked);//单击改变图片选中状态
        adapter.notifyDataSetChanged();
        tv_web_url.setText(selectCount + "/" + imageBeanList.size());

        //设置当全选时，checkbox为选中状态，否则checkbox为非选中状态
        if (selectCount == imageBeanList.size()) {
            cb_picture_ischecked.setChecked(true);
        } else {
            cb_picture_ischecked.setChecked(false);
        }
        return true;
    }

    /*
    为gridview设置监听,在编辑状态，如果图片未全部选中，checkbox为非选中状态
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isEdit) {//查看状态
            Intent intent = new Intent(WebPictureActivity.this, DragImageActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("imageBeanList", (ArrayList) imageBeanList);
            startActivity(intent);
        } else {//编辑状态
            boolean ischecked = imageBeanList.get(position).ischecked();
            selectCount = ischecked ? selectCount - 1 : selectCount + 1;
            adapter.checkImage(position, !ischecked);
            adapter.notifyDataSetChanged();
            tv_web_url.setText(selectCount + "/" + imageBeanList.size());

            //设置当全选时，checkbox为选中状态，否则checkbox为非选中状态
            if (selectCount == imageBeanList.size()) {
                cb_picture_ischecked.setChecked(true);
            } else {
                cb_picture_ischecked.setChecked(false);
            }

        }

    }

    /*
    设置checkedbox的状态,对图片进行全选中或者全不选中
     */
    public void checkimages(View v) {
        if (cb_picture_ischecked.isChecked()) {
            setAllImagesState(true);
            selectCount = imageBeanList.size();
            tv_web_url.setText(selectCount + "/" + imageBeanList.size());
        } else {
            setAllImagesState(false);
            selectCount = 0;
            tv_web_url.setText(selectCount + "/" + imageBeanList.size());

        }

    }

    private void setAllImagesState(boolean check) {
        for (int i = 0; i < imageBeanList.size(); i++) {
            adapter.checkImage(i, check);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isEdit = false;
    }

    /*
        为下载图标设置监听
         */
    public void download(View v) {
        if (Constants.state == Constants.S_WEB) {
            Log.e("TAG", "download+Constants.S_WEB");
            showProgressDialog("正在批量下载图片", true);
            dialog.setTitle("正在下载。。。");
            dialog.setMax(selectCount);
            downloadImageToLocal();
            initState();
        } else if (Constants.state == Constants.S_LOCAL) {
            Log.e("TAG", "download+Constants.S_LOCAL");
            deleteImageFromLocal();
            Toast.makeText(WebPictureActivity.this, "所有图片删除完毕", Toast.LENGTH_SHORT).show();
            initState();
        }
        setAllImagesState(false);//包含跟新adapter


    }

    /*
    删除选中的本地图片
     */
    private void deleteImageFromLocal() {

        for (int i = 0; i < imageBeanList.size(); i++) {
            if (imageBeanList.get(i).ischecked()) {
                File file = new File(imageBeanList.get(i).getUrl());
                if (file.exists()) {
                    file.delete();
                }
                imageBeanList.remove(i);
                i--;//当把集合中某项删除时，后一项会自动挪到此位置，为了遍历到所有的项，需要加此代码
            }
        }

    }

    /*
    下载选中的图片到本地
     */

    private void downloadImageToLocal() {
        for (int i = 0; i < imageBeanList.size(); i++) {
            if (imageBeanList.get(i).ischecked()) {
                downloadImage(i);
            }
        }
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
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(5000);
        final String filePath = Constants.downloadPath + System.currentTimeMillis() + AppUtils.cutImagePath(url);
        Log.e("TAG", "filePath" + filePath);
        x.http().get(params, new MyCacheCallBack<File>() {//利用自定义的类，显式的回调以下方法
            @Override
            public boolean onCache(File result) {
                Log.e("TAG", "onCache() " + result.getAbsolutePath());
                FileUtil.copy(result.getAbsolutePath(), filePath);
                updateDownImageProgress();//更新进度条

                return true;//返回为true意味着将图片同事保存到一级缓存中
            }

            @Override
            public void onSuccess(File result) {

                Log.e("TAG", "onSuccess" + result.getAbsolutePath());
                FileUtil.copy(result.getAbsolutePath(), filePath);
                updateDownImageProgress();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG", "onError() " + ex.getMessage());
                Toast.makeText(WebPictureActivity.this, "图片" + url + "下载失败", Toast.LENGTH_SHORT).show();
                updateDownImageProgress();
            }
        });
    }

    /*
    更新进度条
     */
    private void updateDownImageProgress() {
        dialog.incrementProgressBy(1);
        if (dialog.getProgress() == dialog.getMax()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isEdit) {
            super.onBackPressed();
        } else {
            initState();
            setAllImagesState(false);
        }
    }
}
