package com.pengmaster.view_okui.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-08 15:09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class VersionColorUtils {

    public static void setVersion(Activity activity){
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();

        TextView view = new TextView(activity);
        view.setBackgroundColor(Color.BLUE);

        decorView.addView(view,200,200);    }
}
