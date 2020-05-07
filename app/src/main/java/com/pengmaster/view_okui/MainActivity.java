package com.pengmaster.view_okui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dalvik.system.BaseDexClassLoader;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.animation.DecelerateInterpolator;

import com.pengmaster.view_okui.utils.Utils;
import com.pengmaster.view_okui.view.CameraAnimationView;
import com.pengmaster.view_okui.view.KeyframeAnimationView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //    CameraAnimationView cameraAnimationView ;
//    KeyframeAnimationView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        view = findViewById(R.id.cameraView);

//        cameraAnimationView = findViewById(R.id.cameraView);
//
//        //属性动画
//        ObjectAnimator rotateBottom = ObjectAnimator.ofFloat(cameraAnimationView, "rotateBottom", 45);
//
//        ObjectAnimator rotateTop = ObjectAnimator.ofFloat(cameraAnimationView, "rotateTop", -45);
//
//        ObjectAnimator rotateCamera = ObjectAnimator.ofFloat(cameraAnimationView, "rotateCamera", 270);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playSequentially(rotateBottom,rotateCamera,rotateTop);
//        set.setDuration(1500);
//        set.setStartDelay(1000);
//        set.start();

        //PropertyValuesHolder 模式
//        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("rotateBottom", 45);
//        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("rotateTop", -45);
//        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("rotateCamera", 270);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(cameraAnimationView, holder1, holder3, holder2);
//        objectAnimator.setDuration(1500);
//        objectAnimator.setStartDelay(1000);
//        objectAnimator.start();


        //keyFrame 动画演示
        //参数 进度 目标值
//        Keyframe keyframe = Keyframe.ofFloat(0.1f, 0 * 600f);
//        Keyframe keyframe2 = Keyframe.ofFloat(0.8f, 0.8f * 600f);
//        Keyframe keyframe3 = Keyframe.ofFloat(1.5f, 1.2f * 600f);
//        Keyframe keyframe4 = Keyframe.ofFloat(0.8f, 0.8f * 600f);
//        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("translationX", keyframe, keyframe2, keyframe3, keyframe4);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, holder);
//        objectAnimator.setDuration(2000);
//        objectAnimator.setStartDelay(1000);
//        objectAnimator.start();

//        reflect3();

    }

    //方法public修饰的
    private void reflect1(){
        //反射
        Class classes = Utils.class;
        try {
            //创建对象
            Object objClass = classes.newInstance();
            //拿方法
            Method getTextByReflect = classes.getDeclaredMethod("getTextByReflect");
            //方法调用
            getTextByReflect.invoke(objClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //方法非public修饰的
    private void reflect2() {
        try {
            //反射
            Class classes = Class.forName("com.pengmaster.view_okui.utils.UtilsReflect");
            Constructor declaredConstructors = classes.getDeclaredConstructors()[0];
            //开启访问权限
            declaredConstructors.setAccessible(true);
            //创建对象
            Object objClass = declaredConstructors.newInstance();
            //拿方法
            Method getTextByReflect = classes.getDeclaredMethod("getTextByReflect");
            //开启权限
            getTextByReflect.setAccessible(true);
            //方法调用
            getTextByReflect.invoke(objClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void reflect3() {
        try {
            Class<?> classes = getClassLoader().loadClass("com.pengmaster.view_okui.utils.UtilsReflect");
            //反射
            Constructor declaredConstructors = classes.getDeclaredConstructors()[0];
            //开启访问权限
            declaredConstructors.setAccessible(true);
            //创建对象
            Object objClass = declaredConstructors.newInstance();
            //拿内部字段
            Field nameField = classes.getDeclaredField("name");
            nameField.setAccessible(true);
            Object nameObj = nameField.get(objClass);
            System.out.println("nameObj:"+nameObj);
            //重新赋值
            nameField.set(objClass,"新的name");
            Object nameObjNew = nameField.get(objClass);
            System.out.println("nameObjNew:"+nameObjNew);
            //拿方法
            Method getTextByReflect = classes.getDeclaredMethod("getTextByReflect");
            //开启权限
            getTextByReflect.setAccessible(true);
            //方法调用
            getTextByReflect.invoke(objClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void refrect4(){
//        File apk = new File(getCacheDir() + "/hotfix.dex");
//        if (apk.exists()) {
//            try {
//                ClassLoader classLoader = getClassLoader();
//                Class loaderClass = BaseDexClassLoader.class;
//                Field pathListField = loaderClass.getDeclaredField("pathList");
//                pathListField.setAccessible(true);
//                Object pathListObject = pathListField.get(classLoader);
//                Class pathListClass = pathListObject.getClass();
//                Field dexElementsField = pathListClass.getDeclaredField("dexElements");
//                dexElementsField.setAccessible(true);
//                Object dexElementsObject = dexElementsField.get(pathListObject);
//
//                // classLoader.pathList.dexElements = ???;
//                PathClassLoader newClassLoader = new PathClassLoader(apk.getPath(), null);
//                Object newPathListObject = pathListField.get(newClassLoader);
//                Object newDexElementsObject = dexElementsField.get(newPathListObject);
//
//                int oldLength = Array.getLength(dexElementsObject);
//                int newLength = Array.getLength(newDexElementsObject);
//                Object concatDexElementsObject = Array.newInstance(dexElementsObject.getClass().getComponentType(), oldLength + newLength);
//                for (int i = 0; i < newLength; i++) {
//                    Array.set(concatDexElementsObject, i, Array.get(newDexElementsObject, i));
//                }
//                for (int i = 0; i < oldLength; i++) {
//                    Array.set(concatDexElementsObject, newLength + i, Array.get(dexElementsObject, i));
//                }
//
//                dexElementsField.set(pathListObject, concatDexElementsObject);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
