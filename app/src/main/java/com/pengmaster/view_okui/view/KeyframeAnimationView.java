package com.pengmaster.view_okui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-02 14:30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class KeyframeAnimationView extends View {

    private Bitmap bitmap;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public KeyframeAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        bitmap = Utils.getAvatar(getResources(),600);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,0,0,paint);
    }
}
