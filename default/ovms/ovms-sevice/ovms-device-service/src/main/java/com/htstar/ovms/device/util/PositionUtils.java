package com.htstar.ovms.device.util;

import java.text.MessageFormat;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/20
 * Company: 航通星空
 * Modified By:
 */
public class PositionUtils {
    //方向
    private static final String[] angleDetailArr = {
            "正北",
            "正东",
            "正南",
            "正西",
            "北偏东{0}度",
            "东偏南{0}度",
            "南偏西{0}度",
            "西偏北{0}度"
    };

    // 格式化方向
    public static String getDeretion(double direction) {
        String angle = "--";
        if(direction == 0) return angle;
        if (direction < 0) {
            angle = angleDetailArr[0];
        } else {
            direction = direction % 360;
            Double tem = direction % 90;
            if (tem == 0) {
                angle = angleDetailArr[(int)Math.floor(direction / 90)];
            } else {
                angle = angleDetailArr[(int)Math.floor(direction / 90) + 4];
                angle = MessageFormat.format(angle, (Math.round(tem * 10)+0.0) / 10);
            }
        }
        return angle;
    }

}
