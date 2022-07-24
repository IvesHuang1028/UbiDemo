package com.demo.airpollution.Utils.FileCommand;

import java.io.File;
import java.util.ArrayList;

/**
 * Project : air demo
 * Class : FileListUtils
 * Create by Ives 2022
 * 用來拿取檔案相關資料
 */
public class FileUtils {
    public static ArrayList<FileData> getFileList(String path){
        ArrayList<FileData> filelist = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for(int i = 0 ; i < files.length ; i++){
            FileData file = new FileData();
            file.setName(files[i].getName());
            file.setPath(files[i].getPath());
            filelist.add(file);
            files[i] = null;
        }
        return filelist;
    }
}
