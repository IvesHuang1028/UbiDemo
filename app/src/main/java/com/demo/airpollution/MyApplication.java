package com.demo.airpollution;


import android.app.Application;

import com.demo.airpollution.Utils.CrashHandler.ANRcacheHelper;
import com.demo.airpollution.Utils.CrashHandler.CrashHandler;

import static com.demo.airpollution.BuildConfig.DEBUG;

public class MyApplication extends Application {
    String TAG = "MyApplication";

    protected static MyApplication mInstance;

    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        MyLog.d(TAG,"onCreate");
        super.onCreate();
        if(!DEBUG) {
            //錯誤處理
            CrashHandler.getInstance().init(this);
            //ANR處理
            ANRcacheHelper.getInstance().init(this);
        }
    }
    public static MyApplication getMyApp() {
        if (mInstance != null && mInstance instanceof MyApplication) {
            return (MyApplication) mInstance;
        } else {
            mInstance = new MyApplication();
            mInstance.onCreate();
            return (MyApplication) mInstance;
        }
    }
}
