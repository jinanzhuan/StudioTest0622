package com.example.shuwei.cyclebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by shuwei on 2016/6/24.
 */
public class CycleBar extends View {

    private float width;//当前视图的宽度
    private float height;//当前视图的高度
    private int progress = 30;// 当前进度
    private int max = 100;// 最大进度
    private int roundColor = Color.GRAY;// 圆环的颜色
    private int roundProgressColor = Color.RED;// 圆环进度的颜色
    private int roundWidth = 10;// 圆环的宽度
    private int textSize = 60;// 文字的大小
    private int textColor = Color.GREEN;// 文字的颜色

    private Paint paint;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CycleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint=new Paint();//一般在构造器中进行初始化
        paint.setAntiAlias(true);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public int getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(int roundWidth) {
        this.roundWidth = roundWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("TAG", "onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("TAG", "onAttachedToWindow");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.e("TAG", "onMeasure");

        width = this.getMeasuredWidth();
        height = this.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("TAG", "onLayout");
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        Log.e("TAG", "layout");
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e("TAG", "draw");
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e("TAG", "onDraw");

        //1、画圆环
        paint.setColor(roundColor);
        paint.setStrokeWidth(roundWidth);
        paint.setStyle(Paint.Style.STROKE);

        float x=width/2;
        float y=height/2;
        float r=x-roundWidth/2;
        canvas.drawCircle(x, y, r, paint);

        //2、画圆弧
        paint.setColor(roundProgressColor);

        RectF oval=new RectF(roundWidth/2,roundWidth/2,width-roundWidth/2,height-roundWidth/2);
        canvas.drawArc(oval,0,progress*360/max,false,paint);

        //3、画文字
        if(AppUtils.isfinished){
            text = "下载完成！";
        }else{
            text = getProgress()*100/max+"%";
        }
        paint.setColor(textColor);
        paint.setStrokeWidth(0);
        paint.setTextSize(textSize);

        Rect bounds=new Rect();//创建字体的边界
        paint.getTextBounds(text,0, text.length(),bounds);
        float fx=x-bounds.width()/2;
        float fy=y+bounds.height()/2;
        canvas.drawText(text,fx,fy,paint);

        AppUtils.isfinished=false;

    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e("TAG", "onDetachedFromWindow");
        super.onDetachedFromWindow();

    }
}
