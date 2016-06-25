package com.example.shuwei.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义监听器的几个步骤：
 * 1、定义监听器接口(视图类的内部的), 并定义回调方法(抽象的)
 * 2、定义接口类型的成员变量对象
 * 3、定义成员变量的set方法, 给成员变量赋值（此方法一般在Activity调用，在调用时, 指定了接口的实现类对象, 实现了回调方法）
 * 4、当设计的事件发生时, 就会调用接口成员对象的回调方法
 * Created by shuwei on 2016/6/24.
 */
public class QuickIndex extends View {
    private float width;
    private float height;
    private float itemWidth;
    private float itemHeight;
    private OnIndexChangedListener onIndexChangesListener;
    private Paint paint;
    private String[] arrs = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N", "O"
            , "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public QuickIndex(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = width = this.getMeasuredWidth();
        height = this.getMeasuredHeight();
        itemHeight = height / arrs.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < arrs.length; i++) {

            if(i==currentIndex){
                paint.setColor(Color.RED);
                paint.setTextSize(40);
            }else{
                paint.setColor(Color.WHITE);
                paint.setTextSize(30);
            }
            Rect bounds = new Rect();
            paint.getTextBounds(arrs[i], 0, arrs[i].length(), bounds);
            float x = itemWidth / 2 - bounds.width() / 2;
            float y = itemHeight / 2 + bounds.height() / 2 + i * itemHeight;
            canvas.drawText(arrs[i], x, y, paint);
        }
    }

    private int currentIndex=-1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int index= (int) (y/itemHeight);
                Log.e("TAG", "preindex="+index);
                if(index<0){
                    index=0;
                }else if(index>=arrs.length){
                    index=arrs.length-1;
                }
                Log.e("TAG", "index="+index);
                if(currentIndex!=index){
                    currentIndex=index;
                    invalidate();
                }
                if(onIndexChangesListener!=null){
                    onIndexChangesListener.onIndexChanged(arrs[index]);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "ACTION_UP");
                currentIndex=-1;
                invalidate();
                if(onIndexChangesListener!=null){
                    onIndexChangesListener.onUp();
                }
                break;
        }


        return true;
    }


    public void setOnIndexChangesListener(OnIndexChangedListener onIndexChangesListener) {
        this.onIndexChangesListener = onIndexChangesListener;
    }

    interface OnIndexChangedListener {
        public void onIndexChanged(String text);
        public void onUp();
    }
}
