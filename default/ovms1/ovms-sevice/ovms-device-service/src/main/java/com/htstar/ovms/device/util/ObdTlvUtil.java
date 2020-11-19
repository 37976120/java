package com.htstar.ovms.device.util;

import com.htstar.ovms.common.core.util.ByteDataUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:    OBD-TLV工具
 * @Author:         范利瑞
 */
@Getter
public class ObdTlvUtil {

    /**
     * 特殊数据类型，默认为字符串
     */
    public final static Map<String, Integer> CODE_CASE = new HashMap<String, Integer>() {
        {
            //与协议顺序反写

            //Map<(数组标识), Integer(数据类型)>
            //1 , 长度为1的int类型
            //2 , 字符串（字符串为默认类型不需要指定）
            put("0134", 1);//WIFI开关  0x00：WIFI功能关闭 0x01：WIFI功能开启
            put("0111", 1);//GPS开关  0x00：GPS功能关闭 0x01：GPS功能开启
            put("0211", 3);//GPS采集间隔 (0x1102) U2 2个字节
        }

    };

    /**
     * @param keyData : ByteDataUtil.bytesToHexString(标识数组)
     * @param valueData :数据数组
     * @Description:    获取
     * @Author:         范利瑞
     * @CreateDate:     2019/11/21 11:11
     */
    public static String getValue(String keyData,byte[] valueData){
        if (null == keyData || valueData == null) return null;
        Integer key = CODE_CASE.get(keyData);
        if(null == key) key = 2;//默认字符串
        switch (key){
            case 1: return ByteDataUtil.byteToHexString(valueData[0]);
            case 2: return ByteDataUtil.bytesToString(valueData).trim();
            default: return null;
        }
    }



    public static byte[] getByte(String keyData,String valueData){
        if (null == keyData || valueData == null) return null;
        Integer key = CODE_CASE.get(keyData);
        if(null == key) key = 2;//默认字符串
        switch (key){
            case 1: return new byte[]{(byte) Integer.parseInt(valueData, 16)} ;
            case 2: return ByteDataUtil.completionByte(valueData.length(), valueData);
            case 3: return ByteDataUtil.shortToBytesLittle(Short.parseShort(valueData));
            default: return null;
        }
    }
}
