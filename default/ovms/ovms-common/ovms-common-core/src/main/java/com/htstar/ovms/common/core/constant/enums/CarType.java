package com.htstar.ovms.common.core.constant.enums;

/**
 * @author HanGuJi
 * @Description: 车辆类型
 * @date 2020/6/2211:52
 */
public enum CarType {
    SALOON_CAR("轿车", 0), SUV_CAR("SUV", 1), MPV_CAR("MPV", 2), SPORTS_CAR("跑车", 3),
    MINIBUS_CAR("面包车", 4),PICK_CAR("皮卡车", 5),CARRIAGE_CAR("客车", 6);

    CarType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    private String name;
    private int index;

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    /**
     * 获取枚举类型
     * @param index
     * @return
     */
    public static CarType fromMsgType(int index) {
        for (CarType type : CarType.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取枚举类型描述
     * @param code
     * @return
     */
    public static String handleMsg(Integer code) {
        String name = "未知车型";
        //消息类型
        CarType msgType = CarType.fromMsgType(code);
        switch (code) {
            case 0:
                name = msgType.getName();
                break;
            case 1:
                name = msgType.getName();
                break;
            case 2:
                name = msgType.getName();
                break;
            case 3:
                name = msgType.getName();
                break;
            case 4:
                name = msgType.getName();
                break;
            case 5:
                name = msgType.getName();
                break;
            case 6:
                name = msgType.getName();
                break;
        }
        return name;
    }

}
