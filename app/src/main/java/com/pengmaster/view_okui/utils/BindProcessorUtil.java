package com.pengmaster.view_okui.utils;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <pre>
 *     author : Wp
 *     e-mail : 1101313414@qq.com
 *     time   : 2020-05-08 13:12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BindProcessorUtil {

    public static void Bind(Activity activity){

        try {
            // new MainActivityBinding(activity);
            Class bindingClass = Class.forName(activity.getClass().getCanonicalName() + "$Binding");
            Class activityClass = Class.forName(activity.getClass().getCanonicalName());
            Constructor constructor = bindingClass.getDeclaredConstructor(activityClass);
            constructor.newInstance(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
