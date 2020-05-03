package com.pengmaster.view_okui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.pengmaster.view_okui.utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-02 17:23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MaterialEditTextView extends AppCompatEditText {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final float TEXT_SIZE = Utils.px2dp(12);
    private static final float TEXT_MARGIN = Utils.px2dp(8);
    private static final float TEXT_H_OFFSEt = Utils.px2dp(5);
    private static final float TEXT_V_OFFSEt = Utils.px2dp(20);
    private float alpha = 0f;
    private boolean isHintShow;

    public MaterialEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        setPadding(getPaddingLeft(),getPaddingTop() + (int)TEXT_SIZE + (int)TEXT_MARGIN,
                getPaddingRight(),getPaddingBottom());

        paint.setTextSize(TEXT_SIZE);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0 && isHintShow){
                    isHintShow = false;
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(MaterialEditTextView.this, "alpha", 0);

                    alpha.start();
                }else if (!isHintShow && charSequence.length()!=0 ){
                    isHintShow = true;
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(MaterialEditTextView.this, "alpha", 1);

                    alpha.start();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setAlpha(0xff * (int)alpha);
        float offset = (1 - alpha) * Utils.px2dp(16);
        canvas.drawText(getHint().toString(),TEXT_H_OFFSEt, TEXT_V_OFFSEt + offset,paint);
    }
}
