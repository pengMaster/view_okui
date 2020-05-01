package com.pengmaster.view_okui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-01 16:53
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AvatarView extends View {

    private static float WIDTH = Utils.px2dp(100);
    private static float BITMAP_WIDTH = Utils.px2dp(250);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap avatar;
    private Xfermode xfermode;
    private RectF rectF;


    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        avatar = Utils.getAvatar(getResources(),(int)BITMAP_WIDTH);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(getWidth() / 2f - WIDTH,getHeight() / 2f - WIDTH,
                getWidth() / 2f + WIDTH,getHeight() / 2f + WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画一个圆
        //离屏缓冲
        int id = canvas.saveLayer(rectF, paint);
        canvas.drawOval(rectF, paint);
        //设置成为蒙版
        paint.setXfermode(xfermode);
        if (!avatar.isRecycled()) {
            //画真是图形
            canvas.drawBitmap(avatar, getWidth() / 2f - BITMAP_WIDTH / 2,
                    getHeight() / 2f - BITMAP_WIDTH / 2, paint );
            avatar.recycle();
            paint.setXfermode(null);
            canvas.restoreToCount(id);
        }

    }
}
