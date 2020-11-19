package com.htstar.ovms.device.api.constant;

/**
 * Description: RocketMQ 常量
 * Author: flr
 * Date: Created in 2020/6/18
 * Company: 航通星空
 * Modified By:
 */
public class RocketMQConstant {

    public interface TOPIC{
        /**
         * 设备指令主题
         */
        String DEVICE_COMMAND_TOPIC = "DEVICE-COMMAND-TOPIC";
    }

    public interface TAG{
        /**
         * OBD命令标签
         */
        String OBD_COMMAND_TAG = "OBD-COMMAND-TAG";
    }
}
