package com.pengmaster.view_okui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-03 11:42
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CircleView extends View {

    private static float RADIUS = Utils.px2dp(100);
    private static float MARGIN = Utils.px2dp(20);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float width = (RADIUS + MARGIN) * 2;
        float height = (RADIUS + MARGIN) * 2;


        setMeasuredDimension((int)width,(int)height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.RED);

        canvas.drawCircle(RADIUS + MARGIN , RADIUS + MARGIN, RADIUS , paint);
    }
}
