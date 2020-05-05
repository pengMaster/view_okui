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
 *     desc   : 接力型
 *     version: 1.0
 * </pre>
 */
public class MultiTouchRelayView extends View {

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float offsetX;
    private float offsetY;
    private float downX;
    private float downY;
    private float originalOffsetX;
    private float originalOffsetY;
    private int trackingPointerId;

    public MultiTouchRelayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = Utils.getAvatar(getResources(), 600);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int actionIndex;
                trackingPointerId = event.getPointerId(0);
                downX = event.getX(0);
                downY = event.getY(0);
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(trackingPointerId);
                offsetX = originalOffsetX + event.getX(pointerIndex) - downX;
                offsetY = originalOffsetY + event.getY(pointerIndex) - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                actionIndex = event.getActionIndex();
                trackingPointerId = event.getPointerId(actionIndex);
                downX = event.getX(actionIndex);
                downY = event.getY(actionIndex);
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionIndex = event.getActionIndex();
                int newId = event.getPointerId(actionIndex);
                if (newId == trackingPointerId) {//是否是活动中的手指抬起
                  int newIndex;
                  if(actionIndex == event.getPointerCount() - 1){//最大的手指抬起
                      newIndex =  event.getPointerCount() - 2;
                  }else{
                      newIndex =  event.getPointerCount() - 1;
                  }
                  trackingPointerId = event.getPointerId(newIndex);
                    downX = event.getX(actionIndex);
                    downY = event.getY(actionIndex);
                    originalOffsetX = offsetX;
                    originalOffsetY = offsetY;
                }
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
