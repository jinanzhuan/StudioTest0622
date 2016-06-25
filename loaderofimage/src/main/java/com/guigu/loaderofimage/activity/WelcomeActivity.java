package com.guigu.loaderofimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.shuwei.loaderofimage.R;

public class WelcomeActivity extends Activity {
    RelativeLayout rl_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏显示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        rl_welcome = (RelativeLayout)findViewById(R.id.rl_welcome);

        //增加动画
        AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(2000);
        rl_welcome.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
