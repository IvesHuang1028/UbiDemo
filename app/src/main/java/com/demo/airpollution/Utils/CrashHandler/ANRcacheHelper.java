package com.demo.airpollution.Utils.CrashHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.os.FileObserver;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.demo.airpollution.MyLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.FileObserver.CLOSE_WRITE;

public class ANRcacheHelper {
    //broadcast 貌似只有在4.0版才有
    //另外做一個利用檢查檔案 判斷ANR的機制
    private String TAG = "ANR";
    private final String ACTION_ANR = "android.intent.action.ANR";
    private MyReceiver myReceiver;
    private static ANRcacheHelper mInstance;
    private Context mContext;
    FileObserver fileObserver;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    public static synchronized ANRcacheHelper getInstance() {
        if (null == mInstance) {
            mInstance = new ANRcacheHelper();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        myReceiver = new MyReceiver();
        try {
            mContext.registerReceiver(myReceiver, new IntentFilter(ACTION_ANR));
        }catch (Exception e){
            MyLog.e(TAG, "=================ANR================ \n exception : " + e.getMessage());
        }
        initANRListener();
    }
    private  class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MyLog.i(TAG, "intnet = " + intent.getAction());
            if (intent.getAction().equals(ACTION_ANR)) {
                // to do
                MyLog.e(TAG, "=================ANR================\n 發生ANR");
                System.exit(0);
            }

        }
    }
    void initANRListener(){
        fileObserver = new FileObserver("/data/anr/",CLOSE_WRITE) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                MyLog.i("ANR", "onEvent :"+ path);
                if( path!=null ){
                    String filepath = "/data/anr/" + path;
                    if(filepath.contains("trace")){
                        MyLog.e("ANR", "====================ANR==================\n 發生ANR");;
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                }
            }
        };
        try{
            fileObserver.startWatching();
            MyLog.i("ANR", "start anr monitor!");
        }catch (Throwable e){
            fileObserver = null;
            MyLog.e("ANR", "start anr monitor failed!");
        }
    }
}
