package com.demo.airpollution.Utils.FileCommand;

public class FileIO_Common_Variable {
	
	public final static String fileSeparator = System.getProperty("file.separator");
	public final static String external_storage = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String Android_Data_subPath = "Android" + fileSeparator + "data" + fileSeparator;
	
	public final static String Hard_Code_Paths[] = { 
		fileSeparator + "mnt" + fileSeparator + "extSdCard", 
		external_storage + fileSeparator + "sd", 
		external_storage + fileSeparator + "external_sd",
		fileSeparator + "storage" + fileSeparator + "sdcard1", 
		fileSeparator + "sdcard2", 
		fileSeparator + "sdcard-ext", fileSeparator + "extsd",
		fileSeparator + "mnt" + fileSeparator + "external_sd", 
		external_storage + fileSeparator + "ext_sd", 
		fileSeparator + "Removable" + fileSeparator + "MicroSD", 
		fileSeparator + "sdcard",
		external_storage };
}
