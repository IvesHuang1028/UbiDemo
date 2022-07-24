package com.demo.airpollution.Utils.Time;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateUtils {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sdf.parse(strDate, pos);
        return strtodate;
    }

    public static String ToUtcTimeStr(String strDate,String formate)
    {
        String UtcTime = "";
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat sformate = new SimpleDateFormat(formate);  //"yyyy/MM/dd  HH:mm:ss"
        Date strtodate1 = sformate.parse(strDate, pos);
        sformate.setTimeZone(TimeZone.getTimeZone("UTC"));
        //Date strtodate = sformate.parse(strDate, pos);
        UtcTime = sformate.format(strtodate1);

        return UtcTime;

    }

    public static String DateToStr(Date date) {
        return sdf.format(date);
    }


    public static void setDateFormat(String formatString)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
    }

    //diff less then one year
    public static long getDaysDiff(Date date, Date mydate) {

        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    //diff less then one year
    public static long getDaysDiff(String date1, String date2) {

        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;

        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = sdf.parse(date1);
            mydate = sdf.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }


    public static String DateStrAddDay(String strDate,int addDays) {
        Date date = strToDate(strDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, addDays);
        date = c.getTime();
        String strAddDay = DateToStr(date);
        return strAddDay;
    }

    public static Date DateAddDays(Date date,int addDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, addDays);
        date = c.getTime();
        return date;
    }


    public static long getHoursDiff(Date date, Date mydate) {
        Date now = null;

        if(mydate == null)
            now =  new Date();
        else
            now = mydate;
        long Hours = (date.getTime() - now.getTime()) / (60  * 60 * 1000);
        return Hours;
    }
    //diff less then one year
    public static long getHoursDiff(String date1, String date2) {

        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;

        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = sdf.parse(date1);
            mydate = sdf.parse(date2);
        } catch (Exception e) {
        }
        long hours = (date.getTime() - mydate.getTime()) / (60 * 60 * 1000);
        return hours;
    }
}
