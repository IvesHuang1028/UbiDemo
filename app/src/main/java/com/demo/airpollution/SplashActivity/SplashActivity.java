package com.demo.airpollution.SplashActivity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.demo.airpollution.BaseActivity;
import com.demo.airpollution.Constant.AppConstant;
import com.demo.airpollution.Interface.ISplash;
import com.demo.airpollution.MyLog;
import com.demo.airpollution.R;
import com.demo.airpollution.activity.AirPollution;
import com.demo.airpollution.activity.AirPollutionActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Project : air demo
 * Class : SplashActivity
 * Create by Ives 2022
 * control view
 */
public class SplashActivity extends BaseActivity {
    String TAG = "SplashActivity";
    LottieAnimationView lottieAnimationView;
    ImageView mIv_Logo;
    SplashCtrl mModel;

    boolean isLottieFinish = false;
    boolean isPermissionPass = false;
    Timer checkPassTimer = null;
    Context ctx;
    RelativeLayout layout;

    //請求權限清單
    private final int PERMISSION_REQUEST = 100;
    private final String[] MANIFEST_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.i(TAG,  "onCreate");
        setContentView(R.layout.activity_splash);
        layout = (RelativeLayout) findViewById(R.id.activity_splash);
        ctx = this;
        initView();
        if(AppConstant.isAutoScale)
            reScaleAllView();
    }
    //自動縮放功能 文字大小尚待處理
    void reScaleAllView(){
        if(!isReszie) {
            isReszie =true;
            inLayoutView(layout);
            for (int vv = 0; vv < layout.getChildCount(); vv++) {
                View vN = layout.getChildAt(vv);
                reScaleView(vN);
            }
        }
    }
    void initView() {
        mModel = new SplashCtrl(this);
        lottieAnimationView = findViewById(R.id.lottie);
        mIv_Logo = findViewById(R.id.ivlogo);
        mModel.setLottieView(lottieAnimationView);
        mModel.setModelCallback(new ISplash() {
            @Override
            public void LottieFinish() {
                isLottieFinish = true;
                lottieAnimationView.setVisibility(View.GONE);
                //動畫完成換成顯示Logo
                //統一動畫 淡入效果
                mIv_Logo.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.alpha));
                mIv_Logo.setVisibility(View.VISIBLE);
                startCheck();
            }
        });
        checkPermission();
        setScreenParam();
    }
    void startCheck(){
        if(checkPassTimer == null){
            checkPassTimer = new Timer();
            checkPassTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isPermissionPass){
                        //go to login
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goLoginActivity();
                            }
                        });
                    }
                }
            },2*1000,1000);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(checkPassTimer!=null){
            checkPassTimer.cancel();
            checkPassTimer.purge();
            checkPassTimer = null;
        }
    }
    public void goLoginActivity(){
        Intent intent = new Intent(this, AirPollutionActivity.class);
        startActivity(intent);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        },800);
    }
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        for (String strPermission : MANIFEST_PERMISSION) {
            int resultCode = ActivityCompat.checkSelfPermission(this, strPermission);
            if (resultCode != PackageManager.PERMISSION_GRANTED) {
                MyLog.i(TAG,"PERMISSION ADD = " + strPermission);
                permissionList.add(strPermission);
            }
        }
        // 尚未同意權限
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
        }else{
            isPermissionPass = true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MyLog.i(TAG,permissions.toString() + " PERMISSION_GRANTED");
            }else{
                MyLog.i(TAG,permissions.toString() + " PERMISSION_DENIED");
            }
            checkPermission();
        }
    }
}
