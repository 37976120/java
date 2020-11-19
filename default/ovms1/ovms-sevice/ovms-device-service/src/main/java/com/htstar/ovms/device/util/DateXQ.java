package com.htstar.ovms.device.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateXQ {
    /**
     * 获取当前星期
     */
    public static String XQ(){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            String[] weekDays = { "7","1", "2", "3", "4", "5","6" };
            Calendar cal = Calendar.getInstance();
            Date date;
            try {
                cal.setTime(new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //一周的第几天
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
           return  weekDays[w];
    }
    /**
     * 获取当前时分秒
     */
    public static String sfm(){
        Date t = new Date();
        java.sql.Time sqlt = new java.sql.Time(t.getTime());
        return   sqlt.toString();
    }
}
