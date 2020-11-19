package com.htstar.ovms.msg.controller;


import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.common.core.util.R;

import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;
import com.htstar.ovms.msg.api.req.BatchPushByUserIdsReq;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;

import com.htstar.ovms.msg.service.MsgPushUserCidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author JinZhu
 * @Description: 推送消息到APP端
 * @date 2020/7/2811:08
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/appwithtk")
@Api(value = "msgpush", tags = "推送消息")
public class    AppPushController {

    //消息发送Service层
    private final MsgPushUserCidService msgPushUserCidService;


    /**
     * Description: 用户登录和注册时获取cid ||判断 根据用户cid获取cid
     *
     * @param
     * @return R
     */
    @ApiOperation(value = "用户登录/注册时获取cid和用户id ||判断cid进行更新或者添加", notes = "用户登录/注册时获取cid和用户id ||判断cid进行更新或者添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "用户cid"),
            @ApiImplicitParam(name = "userId", value = "用户id")
    })
    @GetMapping("/updateCid/{cid}/{userId}")
    public R updateCid(@PathVariable("cid") String cid, @PathVariable("userId") Integer userId) {
        if (StrUtil.isBlank(cid) || cid ==""  || cid =="null") {
            return R.failed("cid不能为空,客户端注意--服务端获取了null的cid");
        }
        return R.ok(msgPushUserCidService.updateTosingledemo(cid, userId));
    }

    /**
     * 推送消息，向该app所有用户群推
     *
     * @param msgPushStyle
     * @return R
     */
//    @ApiOperation(value = "向所有用户推送", notes = "向所有用户推送")
//    @PostMapping("/toappdemo")
//    public R toappdemo(@RequestBody MsgPushStyleDTO msgPushStyle) {
//        return R.ok(msgPushUserCidService.toappdemo(msgPushStyle));
//    }

    /**
     * Description:推送消息，单推
     *
     * @param
     * @return R
     */
    @ApiOperation(value = "向一个用户推送消息", notes = "向一个用户推送消息")
    @PostMapping("/appSinglePush")
    public R appSinglePush(@Valid @RequestBody SingleAppPushReq req) {
        return R.ok(msgPushUserCidService.toSingdemo(req));
    }


    /**
     * Description: app 企业广播推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @ApiOperation(value = "向企业下所有用户推送同一条消息", notes = "向企业下所有用户推送同一条消息")
    @PostMapping("/broadAppPushByEtp")
    public R broadAppPushByEtp(@Valid @RequestBody BroadAppPushByEtpReq req) {
        return R.ok(msgPushUserCidService.toSingdemos(req));
    }

    /**
     * Description: app 批次单点推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @ApiOperation(value = "向不同用户批次单点推送不同消息", notes = "向不同用户批次单点推送不同消息")
    @PostMapping("/batchAppSinglePush")
    public R batchAppSinglePush(@Valid @RequestBody List<SingleAppPushReq> req) {

        return R.ok(msgPushUserCidService.batchAppSinglePush(req));
    }


    /**
     * Description: app 批量推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @ApiOperation(value = "向不同用户推送同一条消息", notes = "向不同用户推送同一条消息")
    @PostMapping("/batchPushByUserIds")
    public R batchPushByUserIds(@Valid @RequestBody BatchPushByUserIdsReq req) {

        return R.ok(msgPushUserCidService.batchPushByUserIds(req));
    }
    /**
     * Description: app 用户注销清除该用户cid
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @ApiOperation(value = "用户注销清除该用户cid", notes = "用户注销清除该用户cid")
    @PostMapping("/deletePushByUserId/{userId}")
    public void deletePushByUserId(@PathVariable("userId")Integer userId) {
         msgPushUserCidService.deletePushByUserId(userId);
    }

}
