package com.htstar.ovms.enterprise.api.constant;

import org.omg.CORBA.INTERNAL;

/**
 * /**
 * 费用类型
 *
 * @Description:
 * @Author: lw
 * @CreateDate: 2020/7/14 15:25
 */
public interface ItemTypeConstant {
    //台账相关的费用类型 1:停车费 2:违章罚款 3:洗车美容 4:汽车用品 5:其他费用 6:通行费费用 7:加油费用 8:保险费用 9:保养费用 10:年检费用 11:维修费用 12:事故费用
    int STOP_CAR = 1;
    int TICKET = 2;
    int WASH_CAR = 3;
    int SUPPLIES = 4;
    int OTHER = 5;
    int ETC = 6;
    int FUEL=7;
    int INS=8;
    int MAI=9;
    int MOT=10;
    int REPAIR=11;
    int ACCID=12;

}
