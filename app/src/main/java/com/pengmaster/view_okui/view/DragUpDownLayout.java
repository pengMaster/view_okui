package com.pengmaster.view_okui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.pengmaster.view_okui.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-06 14:33
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DragUpDownLayout extends FrameLayout {

    ViewDragHelper viewDragHelper;
    private ViewConfiguration configuration;
    private View view;
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == view;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int minYVel = configuration.getScaledMinimumFlingVelocity();
            int finalBottom = getHeight() - releasedChild.getHeight();
            if(Math.abs(yvel) > minYVel){//是否达到惯性滑动条件 - 达到
                if (yvel > 0) {//向下滑动
                    //要放置的位置 不是偏移位置
                    viewDragHelper.settleCapturedViewAt(0, finalBottom);
                } else {//向上滑动
                    viewDragHelper.settleCapturedViewAt(0, 0);
                }
            }else{
                //对比滑块所处的位置是否大于高度的一半
                boolean isToBottom = releasedChild.getBottom() > getHeight() / 2;
                if(isToBottom){
                    viewDragHelper.settleCapturedViewAt(0, finalBottom);
                }else{
                    viewDragHelper.settleCapturedViewAt(0, 0);
                }
            }
            postInvalidateOnAnimation();
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }
    };

    public DragUpDownLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        viewDragHelper = ViewDragHelper.create(this, callback);
        configuration = ViewConfiguration.get(getContext());
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        view = findViewById(R.id.view);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            postInvalidateOnAnimation();
        }
    }
}
