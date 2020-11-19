package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.entity.SysUserApply;
import com.htstar.ovms.admin.api.req.ApplyJoinReq;
import com.htstar.ovms.admin.api.req.ApprovalJoinReq;
import com.htstar.ovms.admin.api.req.SysUserApplyPageReq;
import com.htstar.ovms.admin.api.vo.EtpPageVO;
import com.htstar.ovms.admin.api.vo.SysUserApplyVO;
import com.htstar.ovms.admin.mapper.SysUserApplyMapper;
import com.htstar.ovms.admin.service.SmsService;
import com.htstar.ovms.admin.service.SysUserApplyService;
import com.htstar.ovms.admin.service.SysUserRoleService;
import com.htstar.ovms.admin.service.SysUserService;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.feign.CarDriverInfoFeign;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业员工申请加入
 *
 * @author flr
 * @date 2020-06-29 10:18:39
 */
@Service
@Slf4j
public class SysUserApplyServiceImpl extends ServiceImpl<SysUserApplyMapper, SysUserApply> implements SysUserApplyService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private SmsService smsService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private CarDriverInfoFeign carDriverInfoFeign;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private MsgAppPushFeign msgAppPushFeign;

    /**
     * 申请加入企业
     * @param req
     * @return
     */
    @Override
    public R applyJoin(ApplyJoinReq req) {
//        log.info("请求参数：{}",req);
        if (!smsService.checkSmsCode(req.getMobile(),req.getCode())){
            return R.failed("验证码错误");
        }
        if (req.getEtpId().intValue() <= 0){
            return R.failed("企业代码有误！");
        }

        SysUser ex = userService.getUserForLogin(req.getMobile());
        if (null != ex){
            return R.failed("手机号已存在");
        }

        SysUserApply apply = this.baseMapper.getApplyByUsername(req.getMobile());
        if (null != apply){
            //审批状态：0=待审批；1=拒绝加入；2=同意加入；
            if (apply.getApplyStatus().intValue() == 0){
                return R.failed("您的申请正在审批！请耐心等待管理员审批，请勿重复操作！");
            }
//            if (apply.getApplyStatus().intValue() == 1){
//                return R.failed("您的申请已被管理员拒绝，请勿重复操作！");
//            }

            if (apply.getApplyStatus().intValue() == 2){
                return R.failed("您已加入该企业，请勿重复操作！");
            }
        }
        SysUserApply inApply = new SysUserApply();
        inApply.setEtpId(req.getEtpId());
        inApply.setPhone(req.getMobile());
        inApply.setUsername(req.getMobile());
        inApply.setPassword(ENCODER.encode(req.getPassword()));
        if (null != req.getDeptId()){
            inApply.setDeptId(req.getDeptId());
        }
        inApply.setNickName(req.getNickName());
        inApply.setDriverStatus(req.getDriverStatus());

        this.save(inApply);
        try{
            //消息推送
            List<Integer> userIds = userRoleService.queryAdminIdList(req.getEtpId());
            List<SingleAppPushReq> reqList = new ArrayList<>();
            for (Integer userId :userIds){
                SingleAppPushReq msgReq = new SingleAppPushReq();
                msgReq.setUserId(userId);
                msgReq.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);
                msgReq.setTitle("成员加入申请");
                msgReq.setRemindType(2);
                msgReq.setContent(req.getNickName() + "申请加入组织，请及时处理！");
                reqList.add(msgReq);
            }

            msgAppPushFeign.batchAppSinglePush(reqList, SecurityConstants.FROM_IN);
        }catch (Exception e){
            log.error("调取消息服务推送消息失败",e);
        }
        return R.ok("申请已提交！请耐心等待。");
    }

    /**
     * 审批员工加入申请
     * @param req
     * @return
     */
    @Override
    @Transactional
    public R approval(ApprovalJoinReq req) {
        //审批状态：0=待审批；1=拒绝加入；2=同意加入；
        SysUserApply ex = this.getById(req.getId());
        if (null == ex || ex.getApplyStatus() == null){
            return R.failed("申请已处理！");
        }
        if (ex.getApplyStatus().intValue() != 0){
            return R.failed("非待审批的请求");
        }
        if (req.getApplyStatus().intValue() == 1){
            //修改为已同意
            SysUserApply up = new SysUserApply();
            up.setId(req.getId());
            up.setApplyStatus(1);
            this.updateById(up);
            return R.ok();
        }

        if (req.getApplyStatus().intValue() == 2){
            SysUser sysUser = new SysUser();
            BeanUtil.copyProperties(ex,sysUser);
            sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
            sysUser.setEtpId(null);//自带了，如果这里再设置就有问题
            userService.save(sysUser);

            if (ex.getDriverStatus() == 1){
                userRoleService.saveOneRoleByCode(CommonConstants.ROLE_DRIVER,sysUser.getUserId(),ex.getEtpId());
                //是否是司机：0=否（默认），1=是
                carDriverInfoFeign.saveDriverByUserId(sysUser.getUserId(),ex.getEtpId());
            }else {
                userRoleService.saveOneRoleByCode(CommonConstants.ROLE_STAFF,sysUser.getUserId(),ex.getEtpId());
            }

            //修改为已同意
            SysUserApply up = new SysUserApply();
            up.setId(req.getId());
            up.setApplyStatus(2);
            this.updateById(up);
        }


        return R.ok();
    }

    @Override
    public R getSysUserApplyPage(SysUserApplyPageReq req) {
        req.setEtpId(SecurityUtils.getUser().getEtpId());
        IPage<SysUserApplyVO> pageVOIPage = this.baseMapper.getSysUserApplyPage(req);
        return R.ok(pageVOIPage);
    }

}
