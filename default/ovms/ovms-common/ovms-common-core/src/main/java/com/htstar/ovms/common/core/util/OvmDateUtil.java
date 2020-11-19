package com.htstar.ovms.common.core.util;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/12
 * Company: 航通星空
 * Modified By:
 */
@UtilityClass
public class OvmDateUtil extends cn.hutool.core.date.DateUtil {
    /**
     * Description: 获取北京当前时间
     * Author: flr
     */
    public LocalDateTime getCstNow(){
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
    }

    /**
     * Description: 获取北京当前时间
     * Author: flr
     */
    public LocalDate getCstNowDate(){
        return LocalDate.now(Clock.system(ZoneId.of("Asia/Shanghai")));
    }

    /**
     * Description: UTC时间转北京时间
     * Author: flr
     * Company: 航通星空
     */
    public LocalDateTime castUtcToCst(Date revUtcTime) {
        Instant instant = revUtcTime.toInstant();
        ZoneId zoneId = ZoneId.of("UTC");
        return instant.atZone(zoneId).toLocalDateTime().plusHours(8);
    }

    /**
     * Description: 获取某月的最后一天
     * Author: flr
     * Date: 2020/7/30 16:36
     * Company: 航通星空
     * Modified By:
     */
    public String getMonthLastDay(int year,int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        return format.format(calendar.getTime());
    }

    public int getNowMoth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return Integer.parseInt(LocalDateTime.now().format(formatter));
    }
    /**
     *  LocalDateTime 转字符串  -- 当前时间转换成字符串，指定格式
     * @return
     */
    public String format(){
        // 获得 localDateTime
        LocalDateTime localDateTime =getCstNow();
        // 指定模式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将 LocalDateTime 格式化为字符串
        String format = localDateTime.format(dateTimeFormatter);
        return  format;
    }
}
