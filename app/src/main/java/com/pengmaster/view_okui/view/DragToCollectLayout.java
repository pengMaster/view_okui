package com.pengmaster.view_okui.view;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pengmaster.view_okui.R;

import androidx.core.view.ViewCompat;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-06 10:08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DragToCollectLayout extends RelativeLayout {

    OnDragListener onDragListener;
    private ImageView imageViewDrag;
    private LinearLayout llTextView;

    public DragToCollectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        onDragListener = new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()){
                    case DragEvent.ACTION_DROP:
                        if ( v instanceof LinearLayout) {
                            llTextView.removeAllViews();
                            CharSequence text = event.getClipData().getItemAt(0).getText();
                            TextView textView = new TextView(getContext());
                            textView.setText(text);
                            textView.setTextColor(Color.BLACK);
                            llTextView.addView(textView);
                        }
                        break;
                }
                return true;
            }
        };
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        imageViewDrag = findViewById(R.id.imageViewDrag);
//        llTextView = findViewById(R.id.llTextView);
        llTextView.setOnDragListener(onDragListener);
        imageViewDrag.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clipData = ClipData.newPlainText("name","测试");
                imageViewDrag.startDragAndDrop(clipData,
                        new DragShadowBuilder(v), null, 0);
                return true;
            }
        });
    }
}
