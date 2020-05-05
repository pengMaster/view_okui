package com.pengmaster.view_okui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-05 08:54
 *     desc   : 配合型
 *     version: 1.0
 * </pre>
 */
public class MultiTouchTogetherView extends View {

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float offsetX;
    private float offsetY;
    private float downX;
    private float downY;
    private float originalOffsetX;
    private float originalOffsetY;
    private float focusX;
    private float focusY;


    public MultiTouchTogetherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = Utils.getAvatar(getResources(), 600);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int totalX = 0;
        int totalY = 0;
        int pointerCount = event.getPointerCount();
        //当第二个手指抬起的时候，只有到MOVE的的时候才实际会少一根手指
        boolean isPointerUp = event.getActionMasked() == MotionEvent.ACTION_POINTER_UP;

        for (int i = 0; i < pointerCount; i++) {
            if(!isPointerUp && event.getActionIndex() == i){
                float x = event.getX(i);
                float y = event.getY(i);
                totalX += x;
                totalY += y;
            }
        }
        if(isPointerUp){
            pointerCount -= 1;
        }
        //多个手指中心点
        focusX = totalX / pointerCount;
        focusY = totalY / pointerCount;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP://当第二个手指抬起的时候，只有到MOVE的的时候才实际会少一根手指
                downX = focusX;
                downY = focusY;
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = originalOffsetX + focusX - downX;
                offsetY = originalOffsetY + focusY - downY;
                invalidate();
                break;

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }
}
