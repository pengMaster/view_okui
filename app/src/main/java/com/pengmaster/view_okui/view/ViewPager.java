package com.pengmaster.view_okui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import java.lang.invoke.VolatileCallSite;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-05 16:39
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ViewPager extends ViewGroup {

    private float downX;
    //偏移位置
    private float offsetX;
    //上次抬起手的位置
    private float originalX;

    private OverScroller overScroller;

    private VelocityTracker velocityTracker;
    private ViewConfiguration viewConfiguration;
    private float maxVelocity;
    private float minVelocity;

    private boolean scrolling;


    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        overScroller = new OverScroller(getContext());
        velocityTracker = VelocityTracker.obtain();
        viewConfiguration = ViewConfiguration.get(getContext());
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(ev);

        boolean result = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                scrolling = false;
                downX = ev.getX();
                originalX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = downX - ev.getX();
                if (!scrolling) {
                    if (Math.abs(dx) > viewConfiguration.getScaledPagingTouchSlop()) {
                        scrolling = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                        result = true;
                    }
                }
                break;
        }
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                originalX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = -(event.getX() - downX - originalX);
                //设置边界
                offsetX = Math.max(offsetX, 0);
                offsetX = Math.min(offsetX, getWidth());
                scrollTo((int) offsetX, 0);
                //计算
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                //计算是翻页还是回退
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float xVelocity = velocityTracker.getXVelocity();
                //速度是否达到翻页标准
                int currentPage = 0;
                if (Math.abs(xVelocity) > minVelocity) {//达到
                    if (scrollX > getWidth() / 2) {
                        currentPage = 1;
                    } else {
                        currentPage = 0;
                    }
                } else {
                    //回退
                    if (xVelocity < 0) {//左滑
                        currentPage = 0;
                    } else {
                        currentPage = 1;
                    }
                }
                int dx = currentPage == 1 ? getWidth() - scrollX : -scrollX;
                overScroller.startScroll(scrollX, 0, dx, 0);
                postInvalidateOnAnimation();

                break;

        }
        return true;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childTop = 0;
        int childRight = getWidth();
        int childBottom = getHeight();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childLeft = childLeft + i * getWidth();
            childRight = childRight + i * getWidth();
            child.layout(childLeft, childTop, childRight, childBottom);
        }
    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }
}
