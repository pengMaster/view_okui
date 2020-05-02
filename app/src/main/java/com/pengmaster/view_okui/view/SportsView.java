package com.pengmaster.view_okui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-01 18:29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SportsView extends View {

    private RectF rectF = new RectF();
    private static float RADIUS = Utils.px2dp(100);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static String TEXT = "测试Text";
    private Rect rectFOffset = new Rect();
    private Paint.FontMetrics fontMetrics = new Paint.FontMetrics();

    public SportsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.px2dp(8));
        paint.setColor(Color.LTGRAY);
        paint.setTextSize(Utils.px2dp(30));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float widthHalf = getWidth() / 2f;
        float heightHalf = getHeight() / 2f;
        rectF.set(widthHalf - RADIUS, heightHalf - RADIUS,
                widthHalf + RADIUS, heightHalf + RADIUS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制环
        canvas.drawOval(rectF, paint);

        //画弧形进度条
        paint.setColor(Color.MAGENTA);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF,60,200,false,paint);

        //写文字
        paint.setStyle(Paint.Style.FILL);
        // 文字上下绝对偏移
//        paint.getTextBounds(TEXT,0,TEXT.length(),rectFOffset);
//        int offset = (rectFOffset.top + rectFOffset.bottom) / 2;
        //不上下跳动的偏移
         paint.getFontMetrics(this.fontMetrics);
        float offset = (fontMetrics.ascent + fontMetrics.descent) / 2;
        //多个文字字体不同时会产生左边空隙情况
        paint.getTextBounds(TEXT,0,TEXT.length(),rectFOffset);
        canvas.drawText(TEXT,getWidth() / 2f + rectFOffset.left , getHeight() / 2f - offset ,paint);
    }
}
