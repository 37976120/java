package com.htstar.ovms.admin.controller;

import com.htstar.ovms.admin.service.SmsService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/sms")
@Api(value = "短信验证码", tags = "短信验证码")
public class SmsController {

    private final SmsService sendSmsCode;

    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @GetMapping("/sendCode/{mobile}")
    public R sendSmsCode(@PathVariable("mobile") String mobile) {
        return sendSmsCode.sendSmsCode(mobile);
    }

    @ApiOperation(value = "校验短信验证码", notes = "校验短信验证码")
    @GetMapping("/checkSmsCode/{phone}/{code}")
    @Inner(value = false)
    R checkSmsCode(@PathVariable("phone")String phone,@PathVariable("code")String code){
        if (sendSmsCode.checkSmsCode(phone,code)){
            return R.ok();
        }else {
            return R.failed("短信验证码错误！");
        }
    }
}
