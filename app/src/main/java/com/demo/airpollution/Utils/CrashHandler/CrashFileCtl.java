package com.demo.airpollution.Utils.CrashHandler;

import android.os.Environment;
import android.util.Log;


import com.demo.airpollution.MyLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class CrashFileCtl {
    private static CrashFileCtl mInstance;
    String filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    String fileName = "crash.txt";

    public static synchronized CrashFileCtl getInstance() {
        if (null == mInstance) {
            mInstance = new CrashFileCtl();
        }
        return mInstance;
    }

    public void savetoFile() {
        File file = new File(filepath,fileName);
        if(!file.exists()){
            MyLog.i("CRASH", "path = " + file.getPath());
            try
            {
                // 第二個參數為是否 append
                // 若為 true，則新加入的文字會接續寫在文字檔的最後
                OutputStream os = new FileOutputStream(file, false);
                //String string = MachineProperty.errorMsg +";"+ MachineProperty.errorTimeStemp + ";" + MachineProperty.CrashReset;
                //os.write(string.getBytes());
                os.close();
            } catch (IOException e) {
                MyLog.e("CRASH","ExternalStorage" + "Error writing " + file);
            }
        }
    }
    public String readFile(){
        StringBuilder text = new StringBuilder();
        File file = new File(filepath,fileName);
        if(file.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
            } catch (IOException e) {
                return "";
            }
            file.delete();
            return text.toString();
        }else {
            return "";
        }
    }
}
