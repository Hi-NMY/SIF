package com.example.sif.Lei.MyToolClass;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateClass {

    public static int showYear(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

    public static int showMonth(){
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        return month+1;
    }

    public static int showMonthDay(){
        Calendar c = Calendar.getInstance();
        int monthDay = c.get(Calendar.MONDAY);
        return monthDay;
    }

    public static int showMaxNowMonth(){
        Calendar c = Calendar.getInstance();
        int maxMonth = c.getActualMaximum(Calendar.DATE);
        return maxMonth;
    }

    public static String showNowDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = simpleDateFormat.format(System.currentTimeMillis());
        return nowTime;
    }

    public static String showNowDate2(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime = simpleDateFormat.format(System.currentTimeMillis());
        return nowTime;
    }

    public static String showYearMonthDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = simpleDateFormat.format(System.currentTimeMillis());
        return nowTime;
    }

    public static String showYearMonthDayAddOneDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        String nowTime = simpleDateFormat.format(c.getTime());
        return nowTime;
    }

    public static String showWeekTable(String date,int f){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(simpleDateFormat.parse(date));
            String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            if (f == 0){
                if ("1".equals(mWay)) {
                    mWay = "天";
                } else if ("2".equals(mWay)) {
                    mWay = "一";
                } else if ("3".equals(mWay)) {
                    mWay = "二";
                } else if ("4".equals(mWay)) {
                    mWay = "三";
                } else if ("5".equals(mWay)) {
                    mWay = "四";
                } else if ("6".equals(mWay)) {
                    mWay = "五";
                } else if ("7".equals(mWay)) {
                    mWay = "六";
                }
            }
            return mWay;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String showDateClass(String thisDate){
        Timestamp timestamp = new Timestamp(stringToDate(thisDate).getTime());
        Date now = new Date();
        long times = now.getTime() - timestamp.getTime();
        long month = times/((24 * 60 * 60 * 1000) * 20);
        long day = times/(24 * 60 * 60 * 1000);
        long hour = (times/(60 * 60 * 1000) - day * 24);
        long min = ((times/(60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (times/1000-day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        StringBuffer stringBuffer = new StringBuffer();
        if (month>0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            stringBuffer.append(simpleDateFormat.format(timestamp));
        }else if (day > 0){
            if (day == 1){
                stringBuffer.append("1天前");
            }else if (day == 2){
                stringBuffer.append("2天前");
            }else if (day == 3){
                stringBuffer.append("3天前");
            }else {
                stringBuffer.append(day+"天前");
            }
        }else if (hour>0){
            stringBuffer.append(hour+"小时前");
        }else if (min>0){
            stringBuffer.append(min+"分钟前");
        }else {
            stringBuffer.append("刚刚");
        }
        String aTime = stringBuffer.toString();
        return aTime;
    }

    public static Date stringToDate(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String sendMath(int i){
        float a = (float) i / 1000;
        String num = null;
        if (a > 1){
            String result = String.format("%.1f",a);
            if (result.endsWith("0")){
                num = String.valueOf(Integer.valueOf(result)) + "k";
            }else {
                num = result + "k";
            }
        }else {
            num = String.valueOf(i);
        }
        return num;
    }
}
