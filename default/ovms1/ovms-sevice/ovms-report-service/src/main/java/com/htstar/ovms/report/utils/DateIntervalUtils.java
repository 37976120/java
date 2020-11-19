package com.htstar.ovms.report.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateIntervalUtils {
    /**
     *  对车辆统计日期间隔工具类
     * @param staTime
     * @param endTime
     * @return
     */
    public static int dateInterval(String staTime , String endTime){
      ThreadLocal<SimpleDateFormat>  df = new ThreadLocal<SimpleDateFormat>(){
          @Override
          protected SimpleDateFormat initialValue() {
              return new SimpleDateFormat("yyyy-MM-dd");
          }
       };
        Date firstDate=null;
        Date secondDate=null;
        try {
            firstDate = df.get().parse(staTime);
            secondDate=df.get().parse(endTime);
        }
        catch(Exception e) {
            // 日期型字符串格式错误
        }

        int nDay=(int)((secondDate.getTime()-firstDate.getTime())/(24*60*60*1000));
        return  nDay;
    }
}
