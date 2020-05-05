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
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MultiTouchSingleFingerView extends View {

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float offsetX;
    private float offsetY;
    private float downX;
    private float downY;
    private float originalOffsetX;
    private float originalOffsetY;

    public MultiTouchSingleFingerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = Utils.getAvatar(getResources(), 600);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = originalOffsetX + event.getX() - downX;
                offsetY = originalOffsetY + event.getY() - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
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
