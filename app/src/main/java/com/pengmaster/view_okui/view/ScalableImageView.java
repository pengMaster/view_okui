package com.pengmaster.view_okui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-04 17:31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ScalableImageView extends View implements GestureDetector.OnGestureListener
        , GestureDetector.OnDoubleTapListener, Runnable {

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float offsetX;
    private float offsetY;
    private float transOffsetX;
    private float transOffsetY;
    private float scale = 1;
    private GestureDetector detector;
    private boolean isBig = false;
    private ObjectAnimator animator;
    private float fraction;
    private OverScroller overScroller;


    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {

        detector = new GestureDetector(getContext(), this);
        overScroller = new OverScroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Utils.getAvatar(getResources(), getWidth());
        //偏移到中心
        offsetX = (getWidth() - bitmap.getWidth()) / 2f;
        offsetY = (getHeight() - bitmap.getHeight()) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(transOffsetX, transOffsetY);
        canvas.scale(scale + (scale * fraction), scale + (scale * fraction), getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (isBig) {
            getAnimator().reverse();
        } else {
            getAnimator().start();
        }
        isBig = !isBig;
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isBig) {
            transOffsetX -= distanceX;
            transOffsetY -= distanceY;
            //边界修正 不能划出边界
            //例如：左滑只能滑动图片超出屏幕的部分
            transOffsetX = Math.min(transOffsetX, (bitmap.getWidth() * 2 - getWidth()) / 2);
            transOffsetX = Math.max(transOffsetX, -(bitmap.getWidth() * 2 - getWidth()) / 2);
            transOffsetY = Math.min(transOffsetY, (bitmap.getHeight() * 2 - getHeight()) / 2);
            transOffsetY = Math.max(transOffsetY, -(bitmap.getHeight() * 2 - getHeight()) / 2);
            invalidate();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isBig) {
            overScroller.fling((int) transOffsetX, (int) transOffsetY, (int) velocityX, (int) velocityY,
                    -(bitmap.getWidth() * 2 - getWidth()) / 2,
                    (bitmap.getWidth() * 2 - getWidth()) / 2,
                    -(bitmap.getHeight() * 2 - getHeight()) / 2,
                    (bitmap.getHeight() * 2 - getHeight()) / 2);
            postOnAnimation(this);
        }
        return false;
    }

    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }

    private ObjectAnimator getAnimator() {
        if (null == animator) {
            animator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
            animator.setDuration(600);
        }
        return animator;
    }

    @Override
    public void run() {
        if (overScroller.computeScrollOffset()) {
            transOffsetY = overScroller.getCurrY();
            transOffsetX = overScroller.getCurrX();
            invalidate();
            postOnAnimation(this);
        }
    }
}
