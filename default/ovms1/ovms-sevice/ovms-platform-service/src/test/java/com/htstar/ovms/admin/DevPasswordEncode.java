package com.htstar.ovms.admin;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Description: POSTMAN密码加密
 * Author: flr
 * Date: Created in 2020/6/4
 * Company: 航通星空
 * Modified By:
 */
public class DevPasswordEncode {
    private static final String KEY_ALGORITHM = "AES";

    public static void main(String[] args) {
        //前后端约定字符串
        String encodeKey = "htxkovmshtxkovms";
        // 构建前端对应解密AES 因子
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,
                new SecretKeySpec(encodeKey.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(encodeKey.getBytes()));
        System.out.println(aes.encryptBase64("000000".getBytes(StandardCharsets.UTF_8)));
    }
}
