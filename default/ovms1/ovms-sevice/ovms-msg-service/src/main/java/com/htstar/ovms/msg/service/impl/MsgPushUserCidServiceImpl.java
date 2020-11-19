package com.htstar.ovms.msg.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.SysUserRoleFeign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;

import com.htstar.ovms.msg.api.dto.CidUserNameDTO;
import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;
import com.htstar.ovms.msg.api.entity.MsgPushLog;

import com.htstar.ovms.msg.api.entity.MsgPushUserCid;
import com.htstar.ovms.msg.api.req.BatchPushByUserIdsReq;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import com.htstar.ovms.msg.api.vo.MsgPushUserCidVO;
import com.htstar.ovms.msg.api.vo.MsgUserEtpIdVO;
import com.htstar.ovms.msg.handle.push.PushMessageToAppDemo;
import com.htstar.ovms.msg.handle.push.PushMessageToListDemo;
import com.htstar.ovms.msg.handle.push.PushMessageToSingleDemo;

import com.htstar.ovms.msg.handle.util.Cp;
import com.htstar.ovms.msg.handle.util.MsgPushStyleUtil;

import com.htstar.ovms.msg.mapper.MsgPushUserCidMapper;
import com.htstar.ovms.msg.service.MsgPushLogService;

import com.htstar.ovms.msg.service.MsgPushUserCidService;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author JinZhu
 * @Description: 个推样式Service层
 * @date 2020/7/2816:30
 */
@Service
@Slf4j
public class MsgPushUserCidServiceImpl extends ServiceImpl<MsgPushUserCidMapper, MsgPushUserCid> implements MsgPushUserCidService {

    @Autowired
    MsgPushLogService msgPushLogService;
    @Autowired
    SysUserRoleFeign SysUserRoleFeign;

    /**
     * 获取用户账号号进行推送
     *
     * @param cid
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MsgPushUserCidVO getMagPushUserCidByUserId(String cid, Integer userId) {
        return baseMapper.getMagPushUserCidByUserId(cid, userId);
    }

    /**
     * 根据用户id修改cid
     *
     * @param cid
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMagPushUserCidByUserId(String cid, Integer userId) {

        return baseMapper.updateMagPushUserCidByUserId(cid, userId);
    }

    /**
     * 判断cid和用户，进行注册登录判断（添加和修改）
     *
     * @param
     */
    @Override
    @Async(value = "kingAsyncExecutor")
    @Transactional(rollbackFor = Exception.class)
    public R updateTosingledemo(String cid, Integer userId) {
        MsgPushUserCidVO pushUserCid = new MsgPushUserCidVO();
        QueryWrapper<MsgPushUserCid> msg = new QueryWrapper<>();
        MsgPushUserCid msgPushUserCid = new MsgPushUserCid();
        //根据cid和用户id查询cid和用户账号（手机号）
        pushUserCid = getMagPushUserCidByUserId(cid, userId);
        //如果查询为空，因为cid可能会变动,有可能是新用户id没有数据
        //1.如果用户在一台手机上面切换账号，有可能存在一个账号二个cid,先查询然后删除原来账号cid在添加新账号绑定，不管有多少
        // 个人使用同一个手机，但是只保留该APP当前登录状态的用户
        // 1-1.如果发现是新用户的id那么添加用户id和cid
        // 2.发现已有cid失效了那么更新一下cid
        try {
            if (pushUserCid == null) {
                //1.如果用户在一台手机上面切换账号，有可能存在一个账号二个cid,先查询然后删除原来账号cid在添加新账号绑定
                QueryWrapper<MsgPushUserCid> msg1 = new QueryWrapper<>();
                QueryWrapper<MsgPushUserCid> msg2 = new QueryWrapper<>();

                msg1.eq("cid", cid);
                msg.eq("user_id", userId);
                Integer integer = baseMapper.selectCount(msg1);
                if (baseMapper.selectOne(msg) == null) {
                    //在同一个手机注册二个账号，防止cid重复被二个账号共用

                    if (integer != null && integer > 0) {//先查询然后删除原来账号cid在添加新账号绑定

                        msg2.eq("cid", cid);
                        baseMapper.delete(msg2);
                    }
                    // 1.新用户id没有那么添加cid
                    // 可以先添加cid然后在查询
                    msgPushUserCid.setCid(cid);
                    msgPushUserCid.setUserId(userId);
                    int insert = baseMapper.insert(msgPushUserCid);
                    if (insert < 0) {
                        return R.failed("添加新用户cid失败");
                    }
                } else if (integer != null && integer > 0) {
                    //先查询然后删除原来账号cid在添加新账号绑定,因为用户在手机上面切换二次账号

                    msg2.eq("cid", cid);
                    baseMapper.delete(msg2);
                }
                //2.发现已有cid失效了那么更新一下cid

                int i = updateMagPushUserCidByUserId(cid, userId);
                if (i < 0) {
                    return R.failed("更新cid失败");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {

            return R.ok();
        }
    }

    /**
     * 自定义推送模板 全部用户
     *
     * @param msgPushStyle
     */
    @Async
    public R toappdemo(MsgPushStyleDTO msgPushStyle) {
        return PushMessageToAppDemo.pushMessageToApp(msgPushStyle);
    }

    /**
     * 自定义推送模板 单个用户
     *
     * @param msgPushStyle
     */
    @Async(value = "kingAsyncExecutor")
    public R toSingdemo(SingleAppPushReq msgPushStyle) {
        MsgPushStyleDTO cp = Cp.cp(msgPushStyle, new MsgPushStyleDTO());
        QueryWrapper<MsgPushUserCid> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", msgPushStyle.getUserId());
        MsgPushUserCid msgPushUserCid = baseMapper.selectOne(wrapper);//查询cid
        //消息入库
        MsgPushLog cp1 = Cp.cp(msgPushStyle, new MsgPushLog());
        boolean save = msgPushLogService.save(cp1);
        cp.setId(cp1.getId());//获取当前入库的消息id
        if (!save) {
            R.failed("入库失败");
        }
        if (msgPushUserCid == null) {
            return R.failed("该用户没有绑定cid");
        }

        if (msgPushUserCid.getCid() != null) {
            //根据用户cid和用户id查询用户账号
            MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushStyle.getUserId());
            return PushMessageToSingleDemo.pushToSingle(cp, magPushUserCidByUserId.getUserName(), msgPushUserCid.getCid());
        } else {
            return R.failed("CID 获取失败");
        }

    }

    /**
     * 自定义推送模板 向企业下面得所有用户推送
     *
     * @param msgPushStyle
     */
    @Async(value = "kingAsyncExecutor")
    public R toSingdemos(BroadAppPushByEtpReq msgPushStyle) {
        MsgPushStyleDTO cp = Cp.cp(msgPushStyle, new MsgPushStyleDTO());
        List<MsgPushLog> logList = new ArrayList<>();
        List<MsgUserEtpIdVO> msgUserEtpIdVOS = baseMapper.selectByDeptIdUser(msgPushStyle.getEtpId());
        for (int i = 0; i < msgUserEtpIdVOS.size(); i++) {
            MsgUserEtpIdVO vo = msgUserEtpIdVOS.get(i);
            if (vo.getUserId() != null) {
                MsgPushLog msgPushLog = new MsgPushLog();
                msgPushLog.setContent(msgPushStyle.getContent());
                msgPushLog.setMsgType(msgPushStyle.getMsgType());
                msgPushLog.setTitle(msgPushStyle.getTitle());
                msgPushLog.setUserId(vo.getUserId());
                msgPushLog.setRemindType(msgPushStyle.getRemindType());
                msgPushLog.setAppCarId(msgPushStyle.getAppCarId());
                logList.add(msgPushLog);

            }

        }
        if (!logList.isEmpty()) {
            msgPushLogService.saveBatch(logList);
            for (MsgPushLog msgPushLog : logList) {
                if (msgPushLog.getUserId() != null) {
                    MsgPushUserCid msgPushUserCid = baseMapper.selectByUserId(msgPushLog.getUserId());
                    if (msgPushUserCid != null) {
                        MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushLog.getUserId());
                        cp.setId(msgPushLog.getId());
                        cp.setUserId(msgPushLog.getUserId());
                        PushMessageToSingleDemo.pushToSingle(cp, magPushUserCidByUserId.getUserName(), msgPushUserCid.getCid());
                    } else {
                        log.info("找不到该企业该用户cid信息", msgPushLog.getUserId());
                        continue;
                    }
                }
            }
        }


        return R.ok();
    }

    /**
     * 自定义推送模板 向企业下面得管理员推送
     *
     * @param msgPushStyle
     */
    @Async(value = "kingAsyncExecutor")
    public R EdpIdtoSingdemos(BroadAppPushByEtpReq msgPushStyle) {
        BeanUtils.copyProperties(msgPushStyle, MsgPushStyleUtil.msgPushStyle1);
        List<MsgPushLog> logList = new ArrayList<>();
        List<Integer> list = null;
        try {
            list = SysUserRoleFeign.queryAdminIdListInner(msgPushStyle.getEtpId(), SecurityConstants.FROM_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            Integer vo = list.get(i);
            if (vo != null) {
                MsgPushLog msgPushLog = new MsgPushLog();
                msgPushLog.setContent(msgPushStyle.getContent());
                msgPushLog.setMsgType(msgPushStyle.getMsgType());
                msgPushLog.setTitle(msgPushStyle.getTitle());
                msgPushLog.setUserId(vo);
                msgPushLog.setRemindType(msgPushStyle.getRemindType());
                msgPushLog.setAppCarId(msgPushStyle.getAppCarId());
                logList.add(msgPushLog);
            }

        }
        if (!logList.isEmpty()) {
            msgPushLogService.saveBatch(logList);
            for (MsgPushLog msgPushLog : logList) {
                BeanUtils.copyProperties(msgPushLog, MsgPushStyleUtil.msgPushStyle1);
                if (msgPushLog.getUserId() != null) {
                    MsgPushUserCid msgPushUserCid = baseMapper.selectByUserId(msgPushLog.getUserId());
                    if (msgPushUserCid != null) {
                        if (msgPushUserCid.getCid() != null) {
                            MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushLog.getUserId());
                            PushMessageToSingleDemo.pushToSingle(MsgPushStyleUtil.msgPushStyle1, magPushUserCidByUserId.getUserName(), msgPushUserCid.getCid());
                        }
                    } else {
                        log.info("找不到该企业该用户cid信息", msgPushLog.getUserId());
                        continue;
                    }
                }
            }
        }
        return R.ok();
    }

    /**
     * 对指定向不同用户列表推送不同消息  转码
     *
     * @param req
     * @return
     */
    @Override
    @Async(value = "kingAsyncExecutor")
    public R batchAppSinglePushUncode(List<SingleAppPushReq> req) {
        List<MsgPushLog> logList = new ArrayList<>();
        for (SingleAppPushReq singleAppPushReq : req) {
            MsgPushStyleUtil.msgPushStyle1.setAppCarId(singleAppPushReq.getAppCarId());
            MsgPushLog cp = Cp.cp(singleAppPushReq, new MsgPushLog());
            logList.add(cp);
        }
        if (!logList.isEmpty()) {
            msgPushLogService.saveBatch(logList);
            for (MsgPushLog msgPushLog : logList) {
                QueryWrapper<MsgPushUserCid> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", msgPushLog.getUserId());
                MsgPushUserCid msgPushUserCid = baseMapper.selectOne(wrapper);//查询cid
                if (msgPushUserCid == null) {
                    log.info("该用户没有绑定cid用户ID{}", msgPushUserCid);
                    continue;
                }
                if (msgPushUserCid.getCid() != null) {
                    CidUserNameDTO cids = new CidUserNameDTO();
                    cids.setCid(msgPushUserCid.getCid());
                    //根据用户cid和用户id查询用户账号
                    MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushLog.getUserId());
                    cids.setUserName(magPushUserCidByUserId.getUserName());
                    BeanUtils.copyProperties(msgPushLog, MsgPushStyleUtil.msgPushStyle1);
                    R r = PushMessageToListDemo.pushToList(cids, MsgPushStyleUtil.msgPushStyle1);

                    if (r.getData().equals("NoValidPush")) {
                        log.info("无效cid", msgPushUserCid);
                        continue;
                    }
                } else {
                    log.info("CID 获取失败", msgPushUserCid);
                    continue;
                }
            }
        }

        return R.ok();
    }

    /**
     * 对指定向不同用户列表推送不同消息
     *
     * @param req
     * @return
     */
    @Override
    @Async(value = "kingAsyncExecutor")
    public R batchAppSinglePush(List<SingleAppPushReq> req) {
        List<MsgPushLog> logList = new ArrayList<>();
        for (SingleAppPushReq singleAppPushReq : req) {
            MsgPushLog msgPushLog = Cp.cp(singleAppPushReq, new MsgPushLog());
            MsgPushStyleUtil.msgPushStyle1.setAppCarId(singleAppPushReq.getAppCarId());
            logList.add(msgPushLog);
        }
        if (!logList.isEmpty()) {
            msgPushLogService.saveBatch(logList);
            for (MsgPushLog msgPushLog : logList) {
                QueryWrapper<MsgPushUserCid> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", msgPushLog.getUserId());
                MsgPushUserCid msgPushUserCid = baseMapper.selectOne(wrapper);//查询cid
                if (msgPushUserCid == null) {
                    log.info("该用户没有绑定cid用户ID{}", msgPushUserCid);
                    continue;
                }
                if (msgPushUserCid.getCid() != null) {
                    CidUserNameDTO cids = new CidUserNameDTO();
                    cids.setCid(msgPushUserCid.getCid());
                    //根据用户cid和用户id查询用户账号
                    MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushLog.getUserId());
                    cids.setUserName(magPushUserCidByUserId.getUserName());
                    BeanUtils.copyProperties(msgPushLog, MsgPushStyleUtil.msgPushStyle1);
                    R r = PushMessageToListDemo.pushToList(cids, MsgPushStyleUtil.msgPushStyle1);

                    if (r.getData().equals("NoValidPush")) {
                        log.info("无效cid", msgPushUserCid);
                        continue;
                    }
                } else {
                    log.info("CID 获取失败", msgPushUserCid);
                    continue;
                }
            }
        }


        return R.ok();
    }

    /**
     * Description: app 批量推送 向不同用户推送同一条消息
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @Override
    @Async(value = "kingAsyncExecutor")
    public R batchPushByUserIds(BatchPushByUserIdsReq req) {
        MsgPushStyleDTO cp = Cp.cp(req, new MsgPushStyleDTO());
        List<MsgPushLog> logList = new ArrayList<>();

        for (Integer userId : req.getUserIds()) {
            MsgPushLog msgPushLog = Cp.cp(req, new MsgPushLog());
            msgPushLog.setUserId(userId);
            logList.add(msgPushLog);
        }
        if (!logList.isEmpty()) {
            msgPushLogService.saveBatch(logList);
            for (MsgPushLog msgPushLog : logList) {
                BeanUtils.copyProperties(msgPushLog, cp);
                QueryWrapper<MsgPushUserCid> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", msgPushLog.getUserId());
                MsgPushUserCid msgPushUserCid = baseMapper.selectOne(wrapper);//查询cid
                if (msgPushUserCid == null) {
                    R.failed("该用户没有绑定cid");
                    continue;
                }
                if (msgPushUserCid.getCid() != null) {
                    CidUserNameDTO cids = new CidUserNameDTO();
                    cids.setCid(msgPushUserCid.getCid());
                    //根据用户cid和用户id查询用户账号
                    MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushLog.getUserId());
                    cids.setUserName(magPushUserCidByUserId.getUserName());

                    PushMessageToListDemo.pushToUserList(cids, cp);
                } else {
                    R.failed("CID 获取失败");
                    continue;
                }
            }
        }
        return R.ok();
    }

    /**
     * Description: app 对指定向不同用户列表推送不同消息   字符串存入json 利用JSONObject.parseArray(对象，对象.class)转换成list
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @Async(value = "kingAsyncExecutor")
    public R batchAppSinglePushReq(String reqs) {
        List<SingleAppPushReq> req = JSONObject.parseArray(reqs, SingleAppPushReq.class);
        List<MsgPushLog> logList = new ArrayList<>();
        for (SingleAppPushReq singleAppPushReq : req) {
            MsgPushLog msgPushLog = Cp.cp(singleAppPushReq, new MsgPushLog());
            MsgPushStyleUtil.msgPushStyle1.setAppCarId(singleAppPushReq.getAppCarId());
            logList.add(msgPushLog);
        }
        if (!logList.isEmpty()) {
            msgPushLogService.saveBatch(logList);
            for (MsgPushLog msgPushLog : logList) {
                QueryWrapper<MsgPushUserCid> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", msgPushLog.getUserId());
                MsgPushUserCid msgPushUserCid = baseMapper.selectOne(wrapper);//查询cid
                if (msgPushUserCid == null) {
                    log.info("该用户没有绑定cid用户ID{}", msgPushUserCid);
                    continue;
                }
                if (msgPushUserCid.getCid() != null) {
                    CidUserNameDTO cids = new CidUserNameDTO();
                    cids.setCid(msgPushUserCid.getCid());
                    //根据用户cid和用户id查询用户账号
                    MsgPushUserCidVO magPushUserCidByUserId = getMagPushUserCidByUserId(msgPushUserCid.getCid(), msgPushLog.getUserId());
                    cids.setUserName(magPushUserCidByUserId.getUserName());
                    BeanUtils.copyProperties(msgPushLog, MsgPushStyleUtil.msgPushStyle1);
                    R r = PushMessageToListDemo.pushToList(cids, MsgPushStyleUtil.msgPushStyle1);

                    if (r.getData().equals("NoValidPush")) {
                        log.info("无效cid", msgPushUserCid);
                        continue;
                    }
                } else {
                    log.info("CID 获取失败", msgPushUserCid);
                    continue;
                }
            }
        }

        return R.ok();
    }

    /**
     * @param userId
     * @return
     */
    @Override
    @Async(value = "kingAsyncExecutor")
    public void deletePushByUserId(Integer userId) {
        QueryWrapper<MsgPushUserCid> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        baseMapper.delete(wrapper);
    }
}
