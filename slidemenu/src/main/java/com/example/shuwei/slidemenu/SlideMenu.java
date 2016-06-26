package com.example.shuwei.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by shuwei on 2016/6/26.
 */
public class SlideMenu extends FrameLayout {
    private View menuView;
    private int menuItemWidth,menuHeight;
    private Scroller scroller;

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        menuView=getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        menuItemWidth=menuView.getMeasuredWidth();
        menuHeight=menuView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(-menuItemWidth,0,0,menuHeight);
    }

    /*
    getScrollX()得到的是组件移动后的坐标，左正右负。
    event.getRawX()得到的是事件源相对于左上角坐标原点的坐标
    自定义的dX是事件源移动的距离，组件响应事件源，也要相应的移动，但是需要个组件的移动限制范围。
     */
    private int lastX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int eventRawX= (int) event.getRawX();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                lastX=eventRawX;
                break;
            case MotionEvent.ACTION_MOVE:
                //获取横坐标的移动量
                int dX=eventRawX-lastX;
                Log.e("TAG", "lastX="+lastX);
                Log.e("TAG", "eventRawX"+eventRawX);
                int scrollX=getScrollX() - dX;

                Log.e("TAG", "getScrollX()="+getScrollX());
                if(scrollX<=-menuItemWidth){
                    scrollX=-menuItemWidth;
                }else if(scrollX>0){
                    scrollX=0;
                }
                scrollTo(scrollX, getScrollY());//向左（右）移动时，原来移动的坐标减去（加上）现在移动的距离
                lastX=eventRawX;
                break;
            case MotionEvent.ACTION_UP:
                int totalX=getScrollX();//须知，此处省略了this,应该为this.getScrollX(),其中this为要移动的组件，本处为SlideMenu，如果是他的子组件的话，就应当用子组件的对象.getScrollX().
                if(totalX<=-menuItemWidth/2){
                    openMenu();//totalX的范围在[-menuItemWidth,-menuItemWidth/2]之间时，菜单打开
                }else{
                    closeMenu();//totalX的范围在(-menuItemWidth/2,0]之间时，菜单关闭
                }
                break;
        }


        return true;

    }

    private boolean isOpen=true;

    private void closeMenu() {
        scroller.startScroll(getScrollX(),getScrollY(),-getScrollX(),-getScrollY(),1000);
        invalidate();
        isOpen=true;
        Log.e("TAG", "closeMenu="+isOpen);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    private void openMenu() {
        scroller.startScroll(getScrollX(),getScrollY(),-(menuItemWidth+getScrollX()),-getScrollY(),1000);
        invalidate();
        isOpen=false;
        Log.e("TAG", "openMenu="+isOpen);
        
    }
    public void swithStatus(){
        if(isOpen){
            openMenu();
        }else {
            closeMenu();
        }
    }

}
