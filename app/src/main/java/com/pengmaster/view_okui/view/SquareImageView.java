package com.pengmaster.view_okui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-03 10:57
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@SuppressLint("AppCompatCustomView")
public class SquareImageView extends ImageView {

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int max = Math.max(measuredHeight, measuredWidth);
        setMeasuredDimension(max,max);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }
}
