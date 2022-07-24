package com.demo.airpollution.Utils.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

    //time
    public static String now() {
        String strTime;

        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);

        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        int apm = mCalendar.get(Calendar.AM_PM);
        int second = mCalendar.get(Calendar.SECOND);

        strTime = String.format("%02d:%02d:%02d", hour, minute, second);
        return strTime;
    }

    public static String getFormatDateTimeUI(long now) {
        Date date = new Date(now);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));

        return df.format(date);
    }

    public static String getFormatDateTime(long now) {
        Date date = new Date(now);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));

        return df.format(date);
    }

    public static String getFormatDateTimeFile() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        return df.format(new Date());
    }
    public static String getFormatDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        return df.format(new Date());
    }
    //設定時間的格式
    //@ param1 : String 時間資料
    //@ output :  字串轉換成使用的時間format
    public static Date getFormatStringToTime(String now) {
        java.text.DateFormat df = new SimpleDateFormat("yyyyMMdd" );
        df.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        Date date = null;
        try{
            date = df.parse(now);
        }catch (ParseException e){
            return null;
        }
        return date;
    }
    /**
     * 当地时间 ---> UTC时间
     * @return
     */
    public static String Local2UTC(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
        return sdf.format(new Date());
    }

    /**
     * UTC时间 ---> 当地时间
     * @param utcTime   UTC时间
     * @return
     */
    public static String utc2Local(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
        localFormater.setTimeZone(TimeZone.getDefault());
        return localFormater.format(gpsUTCDate.getTime());
    }
}
