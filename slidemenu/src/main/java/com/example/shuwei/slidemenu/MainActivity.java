package com.example.shuwei.slidemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SlideMenu sm_main;
    private ImageView iv_top_back;
    private TextView tv_top_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化获取组件duixiang
        sm_main = (SlideMenu)findViewById(R.id.sm_main);
        iv_top_back = (ImageView)findViewById(R.id.iv_top_back);
        tv_top_title = (TextView)findViewById(R.id.tv_top_title);

        iv_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sm_main.swithStatus();
            }
        });
    }
    public void choiceItem(View v) {
        TextView tv= (TextView) v;
        CharSequence text = tv.getText();
        tv_top_title.setText(text);
        sm_main.swithStatus();
    }
}
