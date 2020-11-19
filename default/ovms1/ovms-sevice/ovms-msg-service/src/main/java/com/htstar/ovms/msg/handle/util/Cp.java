package com.htstar.ovms.msg.handle.util;

import org.springframework.beans.BeanUtils;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/9/917:36
 */
public class Cp<T1,T2> {

    public static <T1,T2> T2 cp(T1 t1, T2 t2){
        BeanUtils.copyProperties(t1,t2);
        return t2;
    }
}
