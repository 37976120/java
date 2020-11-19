package com.htstar.ovms.admin.service;

import com.htstar.ovms.common.core.util.R;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/28
 * Company: 航通星空
 * Modified By:
 */
public interface SmsService {
    /**
     * 发送短信验证码（无需登/注册..使用）
     * @param mobile
     * @return
     */
    R<Boolean> sendSmsCode(String mobile);

    /**
     * 发送阿里短信验证码
     * @param phoneNumber
     * @param content
     * @return
     */
    boolean sendAliSms(String phoneNumber, String content);


    /**
     * 校验短信验证码
     * @param phone
     * @param code
     * @return
     */
    boolean checkSmsCode(String phone,String code);

}
