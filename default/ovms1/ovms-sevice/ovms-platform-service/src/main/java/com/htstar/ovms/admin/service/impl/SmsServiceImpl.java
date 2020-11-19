package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.htstar.ovms.admin.service.SmsService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.enums.LoginTypeEnum;
import com.htstar.ovms.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/28
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
@Service
@RefreshScope
public class SmsServiceImpl implements SmsService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 短信签名-可在短信控制台中找到
     */
    @Value("${sms.aliyun.signName}")
    private String signName;

    @Value("${sms.test.status:false}")
    private boolean testStatus;

    /**
     * 短信模板-可在短信控制台中找到
     */
    @Value("${sms.aliyun.templateCode}")
    private String templateCode;

    @Value("${sms.aliyun.accessKey}")
    private String accessKey;

    @Value("${sms.aliyun.accessSecret}")
    private String accessSecret;



    // 产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    // 产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        Object codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + mobile);

        if (codeObj != null) {
            log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
            return R.ok(Boolean.FALSE, "验证码发送过于频繁，请稍后。");
        }

        String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));

        if (sendAliSms(mobile, code)){
            redisTemplate.opsForValue().set(
                    CacheConstants.DEFAULT_CODE_KEY + mobile
                    , code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
            return R.ok(Boolean.TRUE, code);
        }else {
            log.error("StrUtil.join(",",list)，手机号:" + mobile);
            return R.ok(Boolean.FALSE, "短信通道异常");
        }
    }


    /**
     * 阿里云短信验证码
     * @param phoneNumber
     * @param content
     * @return
     */
    @Override
    public boolean sendAliSms(String phoneNumber, String content){
        if (testStatus){
            //测试环境直接返回成功
            return true;
        }
        //https://help.aliyun.com/document_detail/55284.html?spm=a2c4g.11186623.6.667.6b211886BSgzzd

        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");

        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, accessSecret);

        DefaultProfile.addEndpoint("cn-hangzhou", product, domain);

        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为

        request.setTemplateParam("{\"code\":\"" + content + "\"}");

        // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)

        // request.setSmsUpExtendCode("90997");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者

        // request.setOutId("yourOutId");

        // hint 此处可能会抛出异常，注意catch

        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        if (null == sendSmsResponse){
            return false;
        }else {
            if (sendSmsResponse.getCode().equals("OK")){
                return true;
            }else {
                return false;
            }
        }
    }


    @Override
    public boolean checkSmsCode(String phone, String code){
        if (StrUtil.isBlank(phone) || StrUtil.isBlank(code)){
            return false;
        }
        String key = CacheConstants.DEFAULT_CODE_KEY + phone;
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        if (!redisTemplate.hasKey(key)) {
            return false;
        }

        Object codeObj = redisTemplate.opsForValue().get(key);

        if (codeObj == null) {
            return false;
        }

        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisTemplate.delete(key);
            return false;
        }

        if (!StrUtil.equals(saveCode, code)) {
            redisTemplate.delete(key);
            return false;
        }

        return true;
    }
}
