package com.htstar.ovms.common.core.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/4
 * Company: 航通星空
 * Modified By:
 */
@UtilityClass
public class OvmListUtil {
    public List<Integer> paseStrList(String[] split) {
        List<Integer> list = new ArrayList<>();
        for (String s : split){
            list.add(Integer.parseInt(s));
        }
        return list;
    }


    public List<Integer> splitStrList(String str){
        List<Integer> list = new ArrayList<>();
        String[] strings = str.split(",");
        for (String s : strings){
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
