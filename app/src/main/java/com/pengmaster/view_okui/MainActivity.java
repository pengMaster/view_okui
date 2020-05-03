package com.pengmaster.view_okui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

import com.pengmaster.view_okui.view.CameraAnimationView;
import com.pengmaster.view_okui.view.KeyframeAnimationView;

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
    }
}
