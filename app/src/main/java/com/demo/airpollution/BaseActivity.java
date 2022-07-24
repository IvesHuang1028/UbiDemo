package com.demo.airpollution;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.demo.airpollution.Constant.AppConstant;

import java.lang.reflect.InvocationTargetException;
/**
 * Project : air demo
 * Class : BaseActivity
 * Create by Ives 2022
 */
public class BaseActivity extends AppCompatActivity {
    String TAG = "BaseActivity";
    public boolean isReszie = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //繼承這邊 可做一些統一的特殊處理

        //全螢幕顯示
        hideBottomUIMenu();
    }
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the side
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        }

        return size;
    }

    public void setScreenParam(){
        Point p_NavigationBar = getNavigationBarSize(this);
        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        AppConstant.widthScale = (float) ((dm.widthPixels/getResources().getDisplayMetrics().density)/ AppConstant.ori_Width );
        com.demo.airpollution.Constant.AppConstant.density= getResources().getDisplayMetrics().density;
        //以height為基礎
        // 改為參考density 來做比例調整 以防density不一樣比例會跑掉
        if(p_NavigationBar.x > p_NavigationBar.y) {
            if(dm.heightPixels < dm.widthPixels)
                AppConstant.heightScale = (float) (dm.heightPixels / getResources().getDisplayMetrics().density / AppConstant.ort_Height);
            else
                AppConstant.heightScale = (float) (dm.widthPixels/ getResources().getDisplayMetrics().density / AppConstant.ort_Height);
        }else {
            if(dm.heightPixels < dm.widthPixels)
                AppConstant.heightScale = (float) (dm.heightPixels / getResources().getDisplayMetrics().density / AppConstant.ort_Height);
            else
                AppConstant.heightScale = (float) (dm.widthPixels / getResources().getDisplayMetrics().density / AppConstant.ort_Height);
        }
        if(p_NavigationBar.x > p_NavigationBar.y){
            if((p_NavigationBar.y + dm.heightPixels) == AppConstant.ort_Height ||
                    (p_NavigationBar.y + dm.heightPixels) == AppConstant.ort_Height)
                AppConstant.heightScale = AppConstant.ort_Height/getResources().getDisplayMetrics().density/AppConstant.ort_Height;
        }else{
            if((p_NavigationBar.x + dm.heightPixels) == AppConstant.ort_Height ||
                    (p_NavigationBar.x + dm.heightPixels) == AppConstant.ort_Height)
                AppConstant.heightScale = AppConstant.ort_Height/getResources().getDisplayMetrics().density/AppConstant.ort_Height;
        }
        AppConstant.widthScale = AppConstant.heightScale;
        AppConstant.textScale = AppConstant.heightScale; //字大小調整

        MyLog.d(TAG,"====原解析=:( W:" + dm.widthPixels + ",H:" + dm.heightPixels + " )");
        MyLog.d(TAG,"====原XML dp=:( W:1280 px ,H:800 px )");
        MyLog.d(TAG,"NavigationBar Size x= " + p_NavigationBar.x);
        MyLog.d(TAG,"NavigationBar Size y= " + p_NavigationBar.y);
        MyLog.d(TAG,"====比例調整=:( density:" + AppConstant.density +" )");
        MyLog.d(TAG,"====比例調整=:( W scale:" + AppConstant.widthScale + ",H scale:" +  AppConstant.heightScale + " )");
        MyLog.d(TAG,"====新解析=:( W:" +  1280 * AppConstant.widthScale + ",H:" +  800 * AppConstant.widthScale + " )");
    }


    /**
     * 以下用於AutoScale
     */
    public void reScaleView(View vN) {
        inView(vN);
        if (vN instanceof RelativeLayout) {
            inRelativeLayout(vN);
        } else if (vN instanceof LinearLayout) {
            inLinearLayout(vN);
        } else if (vN instanceof ConstraintLayout) {
            inConstraintLayout(vN);
        } else if (vN instanceof TabHost) {
            //  vN.postInvalidate();
        } else if (vN instanceof ViewPager) {
            //  vN.postInvalidate();
        } else if (vN instanceof FrameLayout){
            inFrameLayout(vN);
        } else if (vN instanceof GridLayout) {
            inGridLayout(vN);
        }
    }
    public void inTabLayout(View vN) {
        for (int vv = 0; vv < ((TabLayout) vN).getChildCount(); vv++) {
            View vs = ((TabLayout) vN).getChildAt(vv);
            reScaleView(vs);
        }
    }
    public void inConstraintLayout(View vN) {
        for (int vv = 0; vv < ((ConstraintLayout) vN).getChildCount(); vv++) {
            View vs = ((ConstraintLayout) vN).getChildAt(vv);
            reScaleView(vs);
        }
    }

    public void inRelativeLayout(View vN) {
        for (int vv = 0; vv < ((RelativeLayout) vN).getChildCount(); vv++) {
            View vs = ((RelativeLayout) vN).getChildAt(vv);
            reScaleView(vs);
        }
    }
    public void inFrameLayout(View vN) {
        for (int vv = 0; vv < ((FrameLayout) vN).getChildCount(); vv++) {
            View vs = ((FrameLayout) vN).getChildAt(vv);
            reScaleView(vs);
        }
    }
    public void inGridLayout(View vN) {
        for (int vv = 0; vv < ((GridLayout) vN).getChildCount(); vv++) {
            View vs = ((GridLayout) vN).getChildAt(vv);
            reScaleView(vs);
        }
    }
    public void inLinearLayout(View vN) {
        for (int vv = 0; vv < ((LinearLayout) vN).getChildCount(); vv++) {
            View vs = ((LinearLayout) vN).getChildAt(vv);
            reScaleView(vs);
        }
    }
    public void inLayoutView(View vN) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Point p_NavigationBar = getNavigationBarSize(this);
        if (vN.getParent() instanceof HorizontalScrollView) {

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.heightScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);

            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof RelativeLayout) {

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);

            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);

            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            if(dm.widthPixels > dm.heightPixels) {
                if (layoutParams.leftMargin == 0 && layoutParams.width < dm.widthPixels) {
                    if(p_NavigationBar.x > p_NavigationBar.y)
                        layoutParams.leftMargin = (int)((dm.widthPixels + p_NavigationBar.y - layoutParams.width) / 2)  ;
                    else
                        layoutParams.leftMargin = (int)((dm.widthPixels + p_NavigationBar.x - layoutParams.width) / 2)  ;
                }
            }else{
                if (layoutParams.leftMargin == 0 && layoutParams.width < dm.heightPixels) {
                    if(p_NavigationBar.x > p_NavigationBar.y)
                        layoutParams.leftMargin = (int)((dm.heightPixels + p_NavigationBar.y - layoutParams.width) / 2)  ;
                    else
                        layoutParams.leftMargin = (int)((dm.heightPixels + p_NavigationBar.x - layoutParams.width) / 2)  ;
                }
            }
            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof TabHost) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);

            vN.setLayoutParams(layoutParams);
        }else if (vN.getParent() instanceof ConstraintLayout) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);

            vN.setLayoutParams(layoutParams);
        }else if (vN.getParent() instanceof GridLayout) {
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            if(layoutParams.leftMargin > 0)
                layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            if(layoutParams.rightMargin > 0)
                layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            if(layoutParams.topMargin > 0)
                layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            if(layoutParams.bottomMargin > 0)
                layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);

            vN.setLayoutParams(layoutParams);
        }
        //  vN.getPaddingStart()
        vN.setPadding(
                (int) (vN.getPaddingLeft()* AppConstant.widthScale),
                (int) (vN.getPaddingRight()* AppConstant.widthScale),
                (int) (vN.getPaddingTop()* AppConstant.heightScale),
                (int) (vN.getPaddingBottom()* AppConstant.heightScale)
        );
        if (vN instanceof TextView) {
            TextView tv = (TextView)vN;
            tv.setTextSize(tv.getTextSize()* AppConstant.textScale);
            tv.setPadding((int) (vN.getPaddingLeft()* AppConstant.widthScale *2),
                    (int) (vN.getPaddingRight()* AppConstant.widthScale *2),
                    (int) (vN.getPaddingTop()* AppConstant.heightScale *2),
                    (int) (vN.getPaddingBottom()* AppConstant.heightScale *2));
        }

        if (vN instanceof RadioButton){
            RadioButton RdioBtn = (RadioButton) vN;
            RdioBtn.setTextSize( (int) (RdioBtn.getTextSize()* AppConstant.textScale));
        }
        vN.postInvalidate();
    }
    public void inView(View vN) {
        if (vN.getParent() instanceof HorizontalScrollView) {

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.heightScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);

            if (vN instanceof TextView) {
                if(layoutParams.width == 0)
                    layoutParams.width = (int)((TextView) vN).getTextSize();
                if(layoutParams.height == 0)
                    layoutParams.height = (int)((TextView) vN).getHeight();
            }

            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof RelativeLayout) {

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        } else if (vN.getParent() instanceof TabHost) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        }else if (vN.getParent() instanceof ConstraintLayout) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        }else if (vN.getParent() instanceof GridLayout) {
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) vN.getLayoutParams();
            layoutParams.width = (int) (IntToFloat(layoutParams.width) * AppConstant.widthScale);
            layoutParams.height = (int) (IntToFloat(layoutParams.height) * AppConstant.heightScale);
            if(layoutParams.leftMargin > 0)
                layoutParams.leftMargin = (int) (IntToFloat(layoutParams.leftMargin) * AppConstant.widthScale);
            if(layoutParams.rightMargin > 0)
                layoutParams.rightMargin = (int) (IntToFloat(layoutParams.rightMargin) * AppConstant.widthScale);
            if(layoutParams.topMargin > 0)
                layoutParams.topMargin = (int) (IntToFloat(layoutParams.topMargin) * AppConstant.heightScale);
            if(layoutParams.bottomMargin > 0)
                layoutParams.bottomMargin = (int) (IntToFloat(layoutParams.bottomMargin) * AppConstant.heightScale);
            vN.setLayoutParams(layoutParams);
        }
        //  vN.getPaddingStart()
        vN.setPadding(
                (int) (vN.getPaddingLeft()* AppConstant.widthScale),
                (int) (vN.getPaddingRight()* AppConstant.widthScale),
                (int) (vN.getPaddingTop()* AppConstant.heightScale),
                (int) (vN.getPaddingBottom()* AppConstant.heightScale)
        );
        if (vN instanceof TextView) {
            TextView tv = (TextView)vN;
            tv.setTextSize(tv.getTextSize() * AppConstant.textScale / AppConstant.density);
            tv.setPadding((int) (vN.getPaddingLeft()* AppConstant.widthScale *2),
                    (int) (vN.getPaddingRight()* AppConstant.widthScale *2),
                    (int) (vN.getPaddingTop()* AppConstant.heightScale *2),
                    (int) (vN.getPaddingBottom()* AppConstant.heightScale *2));

        }

        if (vN instanceof RadioButton){
            RadioButton RdioBtn = (RadioButton) vN;
            RdioBtn.setTextSize( (int) (RdioBtn.getTextSize()* AppConstant.textScale));
        }
        vN.postInvalidate();
    }
    public float IntToFloat(int dp) {
        return (float) dp;
    }
}
