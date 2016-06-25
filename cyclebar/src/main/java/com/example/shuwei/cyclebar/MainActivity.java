package com.example.shuwei.cyclebar;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CycleBar cb_main_loading;
    private boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("TAG", "onCreate");

        cb_main_loading = (CycleBar)findViewById(R.id.cb_main_loading);
    }
    
    public void download(View v) {
        Log.e("TAG", "download");
        new Thread(){
            public void run(){
                if(flag){
                    return;
                }
                flag=true;
                int count=100;
                cb_main_loading.setProgress(0);
                cb_main_loading.setMax(count);
                cb_main_loading.setText(cb_main_loading.getProgress() * 100 / cb_main_loading.getMax() + "%");
                for(int i = 0; i < count; i++) {
                    AppUtils.isfinished=false;
                    Log.e("TAG", "AppUtils.isfinished="+AppUtils.isfinished);
                    cb_main_loading.setProgress(cb_main_loading.getProgress()+1);
                    if(cb_main_loading.getProgress()==cb_main_loading.getMax()){
                        AppUtils.isfinished=true;
                        Log.e("TAG", "AppUtils.isfinished="+AppUtils.isfinished);
                    }
                    cb_main_loading.postInvalidate();
                    SystemClock.sleep(50);
                }

                flag=false;
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TAG", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("TAG", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TAG", "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.e("TAG", "onDestroy");
        super.onDestroy();
    }
}
