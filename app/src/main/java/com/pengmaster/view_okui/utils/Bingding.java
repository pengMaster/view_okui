package com.pengmaster.view_okui.utils;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-08 11:06
 *     desc   : 反射bingding
 *     version: 1.0
 * </pre>
 */
public class Bingding {

    public static void bind(Activity activity){
        for (Field field : activity.getClass().getDeclaredFields()) {
            try {
                BindView bindView = field.getAnnotation(BindView.class);
                if (null != bindView) {
                    field.setAccessible(true);
                    field.set(activity,activity.findViewById(bindView.value()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
