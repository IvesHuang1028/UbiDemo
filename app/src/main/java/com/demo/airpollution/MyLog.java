package com.demo.airpollution;

import android.util.Log;
import android.util.TimeUtils;

import com.demo.airpollution.Utils.FileCommand.FileData;
import com.demo.airpollution.Utils.FileCommand.FileUtils;
import com.demo.airpollution.Utils.Time.DateTimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.demo.airpollution.BuildConfig.DEBUG;

/**
 * log資料 統一管理
 * 
*/
public class MyLog {
    // log 規則 d : 只除錯用 不寫進log
    //          i : 使用紀錄用 需要寫進log檔
    //          e : 預期會進的錯誤 需寫進log
    public static String LogFilePath = "/data/data/com.mkh.inventory/files/";
    public static long LogSaveDays = 20*24*60*60*1000;   //暫定20天
    public static void e(String tag,String msg) {
        Log.e(tag, msg);
        LogTracker(tag,"error",msg);
    }

    public static void d(String tag, String msg) {
        if(DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag,String msg) {
        Log.i(tag, msg);
        if(DEBUG)
            LogTracker(tag,"info", msg);
    }

    private static void LogTracker(String tag,String type, String msg) {
        //預設路徑 LogFilePath
        //檔名 = Data + .txt
        String fileName = DateTimeUtils.getFormatDateTimeFile() + ".txt";
        File logDir = new File(LogFilePath);
        File logfile = new File(LogFilePath + fileName);
        boolean isAppend = false;
        try {
            //確保有資料夾
            if(!logDir.exists()){
                logDir.mkdir();
            }
            //判斷寫檔
            if (logfile.exists()) {   //複寫
                isAppend = true;
            } else {  //產生新的
                logfile.createNewFile();
                //檢查其他檔案列表 超過存放日期就砍檔
                checkLogFiles();
            }
            FileOutputStream output = null;
            byte[] strBuff = new byte[]{};
            //寫入時間 、type 、tag、msg
            strBuff = (DateTimeUtils.getFormatDateTime() +" " + type + "/" +tag + ": " + msg + "\n").getBytes();
            output = new FileOutputStream(logfile,isAppend);
            output.write(strBuff);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void checkLogFiles(){
        ArrayList<FileData> filelist = FileUtils.getFileList(LogFilePath);
        String nowString = DateTimeUtils.getFormatDateTime();
        Date nowDate = DateTimeUtils.getFormatStringToTime(nowString);
        for (int i = 0; i < filelist.size(); i++) {
            String filename = filelist.get(i).getName();
            String timeString = filename.split("\\.")[0];
            Date FileDate  = DateTimeUtils.getFormatStringToTime(timeString);
            if( Math.abs(nowDate.getTime() - FileDate.getTime()) > LogSaveDays){
                File file = new File(filelist.get(i).getPath());
                if(file.exists()) {
                    file.delete();
                }
            }
        }
    }
}