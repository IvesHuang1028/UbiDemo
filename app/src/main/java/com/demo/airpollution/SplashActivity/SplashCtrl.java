package com.demo.airpollution.SplashActivity;


import android.animation.Animator;
import android.app.Activity;

import com.airbnb.lottie.LottieAnimationView;
import com.demo.airpollution.Interface.ISplash;
import com.demo.airpollution.MyLog;

/**
 * Project : MKH inventory management
 * Class : SplashModelImpl
 * Create by Ives 2020
 * 程式邏輯處理
 * 開機畫面 處理 權限取得問題 處理完成後進入login畫面
 */
public class SplashCtrl implements Animator.AnimatorListener{
    String TAG = "SplashModelImpl";
    Activity activtiy;
    LottieAnimationView LottieView;
    ISplash ModelCallback;

    public SplashCtrl(Activity act){
        activtiy = act;
    }
    public void setLottieView(LottieAnimationView lottie){
        LottieView = lottie;
        LottieView.addAnimatorListener(this);
    }
    public void setModelCallback(ISplash callback){
        ModelCallback = callback;
    }
    @Override
    public void onAnimationStart(Animator animator) {
        MyLog.d(TAG,"SplashAnimation Start");
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        MyLog.d(TAG,"SplashAnimation End");
        ModelCallback.LottieFinish();
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        MyLog.d(TAG,"SplashAnimation Cancel");
    }

    @Override
    public void onAnimationRepeat(Animator animator) {
        MyLog.d(TAG,"SplashAnimation Repeat");
    }

}
