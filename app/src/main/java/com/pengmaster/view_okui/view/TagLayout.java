package com.pengmaster.view_okui.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-03 14:25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TagLayout extends ViewGroup {

    List<Rect> lists = new ArrayList<>();

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthUsed = 0;
        int heightUsed = 0;

        int lineWidthUsed = 0;//已经使用宽度
        int lineHeightUsed = 0;//已经使用高度

        int size = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            //子view测量
            measureChildWithMargins(childAt,widthMeasureSpec,0,heightMeasureSpec,heightUsed);

            //超出范围
            if(widthUsed + childAt.getMeasuredWidth() > size){
                widthUsed = 0;
                heightUsed = heightUsed + lineHeightUsed;
                measureChildWithMargins(childAt,widthMeasureSpec,0,heightMeasureSpec,heightUsed);
            }
            Rect childBound;
            if (lists.size() <= i) {
                childBound = new Rect();
                lists.add(childBound);
            } else {
                childBound = lists.get(i);
            }
            childBound.set(widthUsed,heightUsed,widthUsed + childAt.getMeasuredWidth()
                    ,heightUsed + childAt.getMeasuredHeight());

            widthUsed = widthUsed + childAt.getMeasuredWidth();

            //自己的宽高
            lineHeightUsed = Math.max(lineHeightUsed,childAt.getMeasuredHeight());
            lineWidthUsed = Math.max(lineWidthUsed,widthUsed);//widthUsed为0时用之前的

        }

        setMeasuredDimension(lineWidthUsed,lineHeightUsed);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        for (int n = 0; n < getChildCount(); n++) {
            View childAt = getChildAt(n);
            Rect rect = lists.get(n);
            childAt.layout(rect.left,rect.top,rect.right,rect.bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
