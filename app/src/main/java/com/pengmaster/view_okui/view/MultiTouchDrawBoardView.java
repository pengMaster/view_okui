package com.pengmaster.view_okui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.SparseArray;
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
public class MultiTouchDrawBoardView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Path> paths = new SparseArray<>();


    public MultiTouchDrawBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);//线头圆滑
        paint.setStrokeJoin(Paint.Join.ROUND);//拐角圆滑
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Path path = new Path();
                path.reset();
                path.moveTo(event.getX(), event.getY());
                paths.append(event.getPointerId(event.getActionIndex()),path);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i <event.getPointerCount(); i++) {
                    int pointerId = event.getPointerId(i);
                    path = paths.get(pointerId);
                    path.lineTo(event.getX(i),event.getY(i));
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                paths.remove(event.getPointerId(event.getActionIndex()));
                invalidate();
                break;

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.valueAt(i);
            canvas.drawPath(path, paint);
        }
    }
}
