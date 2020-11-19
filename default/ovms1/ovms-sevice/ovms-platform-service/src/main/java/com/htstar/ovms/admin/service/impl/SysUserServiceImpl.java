package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.dto.UserDTO;
import com.htstar.ovms.admin.api.dto.UserInfo;
import com.htstar.ovms.admin.api.entity.*;
import com.htstar.ovms.admin.api.req.PageSimpleUserReq;
import com.htstar.ovms.admin.api.req.UpdateProfileReq;
import com.htstar.ovms.admin.api.req.UpdatePwdSmsReq;
import com.htstar.ovms.admin.api.vo.MenuVO;
import com.htstar.ovms.admin.api.vo.SimpleUserVO;
import com.htstar.ovms.admin.api.vo.UserMsgVO;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.admin.mapper.SysUserMapper;
import com.htstar.ovms.admin.service.*;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmListUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.feign.CarDriverInfoFeign;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ovms
 * @date 2017/10/31
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final SysMenuService sysMenuService;
    private final SysRoleService sysRoleService;
    private final SysDeptService sysDeptService;
    private final SysUserRoleService sysUserRoleService;
    private final SysDeptRelationService sysDeptRelationService;
    private final RedisTemplate redisTemplate;
    private final SmsService smsService;
    @Autowired
    private CarDriverInfoFeign carDriverInfoFeign;

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        //用户名和手机号一致，现在写入
        sysUser.setPhone(userDto.getUsername());
        baseMapper.insert(sysUser);

        Integer etpId = TenantContextHolder.getEtpId();
        if (null == etpId){
            etpId = SecurityUtils.getUser().getEtpId();
        }

        int driverRoleId = sysRoleService.getRoleIdByCode(CommonConstants.ROLE_DRIVER, etpId);
        Integer finalEtpId = etpId;
        List<SysUserRole> userRoleList = userDto.getRole()
                .stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getUserId());
                    userRole.setRoleId(roleId);
                    //管理员添加司机 同步到司机表
                    if (driverRoleId == roleId) {
                        carDriverInfoFeign.saveDriverByUserId(sysUser.getUserId(), finalEtpId);
                    }
                    return userRole;
                }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(userRoleList);
    }

    /**
     * 通过查用户的全部信息
     *
     * @param sysUser 用户
     * @return
     */
    @Override
    public UserInfo findUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表  （ID）
        List<Integer> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId())
                .stream()
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        //设置角色列表  （CODE）
        List<String> roleCodes = sysRoleService.findRolesByUserId(sysUser.getUserId())
                .stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
        userInfo.setRoleCodes(ArrayUtil.toArray(roleCodes, String.class));


        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menuVo -> StrUtil.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.removeById(sysUser.getUserId());
       carDriverInfoFeign.delDriverByUserId(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R<Boolean> updateUserInfo(UserDTO userDto) {
        UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());
        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getPassword())
                && StrUtil.isNotBlank(userDto.getNewpassword1())) {
            if (ENCODER.matches(userDto.getPassword(), userVO.getPassword())) {
                sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
            } else {
                log.info("原密码错误，修改密码失败:{}", userDto.getUsername());
                return R.failed("原密码错误，修改失败");
            }
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVO.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        sysUser.setNickName(userDto.getNickName());
        sysUser.setSex(userDto.getSex());
        sysUser.setEmail(userDto.getEmail());
        sysUser.setLockFlag(userDto.getLockFlag());
        return R.ok(this.updateById(sysUser));
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());

        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        }
        this.updateById(sysUser);

        sysUserRoleService.remove(Wrappers.<SysUserRole>update().lambda()
                .eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return Boolean.TRUE;
    }

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsers(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUsername, username));

        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Integer parentId = sysDept.getParentId();
        return this.list(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getDeptId, parentId));
    }

    /**
     * 获取当前用户的子部门信息
     *
     * @return 子部门列表
     */
    private List<Integer> getChildDepts() {
        Integer deptId = SecurityUtils.getUser().getDeptId();
        //获取当前部门的子部门
        return sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda()
                        .eq(SysDeptRelation::getAncestor, deptId))
                .stream()
                .map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());
    }


    /**
     * 登录
     *
     * @param username
     * @return
     */
    @Override
    public SysUser getUserForLogin(String username) {
        return baseMapper.getUserForLogin(username);
    }


    /**
     * 短信验证码修改用户密码
     *
     * @param req
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#req.username")
    public R updatePwdBySms(UpdatePwdSmsReq req) {
        //1.验证合法性
        SysUser user = getUserForLogin(req.getUsername());
        if (user == null) {
            return R.failed(null, String.format("用户不存在 %s", req.getUsername()));
        }

        if (!smsService.checkSmsCode(req.getUsername(), req.getCode())) {
            return R.failed(null, "短信验证码错误");
        }


        String pwd = req.getPassword();

        if (StrUtil.isBlank(pwd)) {
            return R.failed(null, "密码为空");
        }

        if (pwd.length() < 6 || pwd.length() > 20) {
            return R.failed(null, "密码大于6位小于20位");
        }

        //2.正式修改
        SysUser upUser = new SysUser();
        upUser.setUserId(user.getUserId());
        upUser.setPassword(ENCODER.encode(pwd));

        this.updateById(upUser);
        return R.ok();
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#req.username")
    public R updateNickName(UpdateProfileReq req) {
        String nickName = req.getNickName();
        if (StrUtil.isBlank(nickName)) {
            return R.failed("姓名不能为空");
        }
        //获取用户
        SysUser user = getUserForLogin(req.getUsername());
        if (user.getNickName().equals(nickName)) {
            return R.ok("姓名修改成功");
        }
        user.setNickName(req.getNickName());
        this.updateById(user);
        return R.ok("姓名修改成功");
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#req.username")
    public R updateSex(UpdateProfileReq req) {
        Integer sex = req.getSex();
        if (sex == null) {
            return R.failed("性别不能为空");
        }
        //获取用户
        SysUser user = getUserForLogin(req.getUsername());
        if (sex.equals(user.getSex())) {
            return R.ok("性别修改成功");
        }
        user.setSex(sex);
        this.updateById(user);
        return R.ok("性别修改成功");
    }

    /**
     * 绑定邮箱
     *
     * @param req
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#req.username")
    public R bindingEmail(UpdateProfileReq req) {
        String username = req.getUsername();
        SysUser user = getUserForLogin(username);
        String code = req.getCode();
        String email = req.getEmail();
        if (!email.contains("@")){
            return R.failed("邮箱格式错误");
        }
        if (StrUtil.isBlank(code)) {
            return R.failed("验证码不能为空");
        }
        if (user.getEmail().equals(email)){
            return R.ok("邮箱绑定成功");
        }
        //邮箱号是否已绑定
        Integer count = baseMapper.selectCount(new QueryWrapper<SysUser>().eq("email", email)
                .eq("del_flag", 0));
        if (count > 0) {
            return R.failed("该邮箱号已绑定账号");
        }
        if (!smsService.checkSmsCode(username, code)) {
            return R.failed("短信验证码错误");
        }
        user.setEmail(email);
        this.updateById(user);
        return R.ok("邮箱绑定成功");
    }

    /**
     * Description: 简化分页（常量方式）
     * Author: flr
     * Date: 2020/7/10 15:56
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<Page<SimpleUserVO>> simplePage(PageSimpleUserReq req) {
        OvmsUser user = SecurityUtils.getUser();
        req.setEtpId(user.getEtpId());
        if (null != req.getApplyType()){
            Map<String,String> map = baseMapper.getApplyVerifyUserStr(user.getEtpId(),req.getApplyType());

            if (map == null){
                req.setRoleCode(CommonConstants.ROLE_ADMIN);
            }else if (StrUtil.isNotBlank(map.get("userListStr"))){
                req.setApplyVerifyUserStr(map.get("userListStr"));
            }else{
                req.setRoleCode(CommonConstants.ROLE_ADMIN);
            }
        }


        Page<SimpleUserVO> page = baseMapper.simplePage(req);

        if (StrUtil.isNotBlank(req.getVerifyUserList())){
            List<Integer> userList = OvmListUtil.paseStrList(req.getVerifyUserList().split(","));
            if (null != page && !page.getRecords().isEmpty()){
                for (SimpleUserVO vo : page.getRecords()){
                    if (userList.contains(vo.getUserId())){
                        vo.setSelectStatus(1);
                    }else {
                        vo.setSelectStatus(0);
                    }
                }
            }
        }

        return R.ok(page);
    }


    @Override
    public List<UserMsgVO> getUserMsgVOListByRoleId(Integer roleId) {
        return baseMapper.getUserMsgVOListByRoleId(roleId);
    }

    @Override
    public List<UserMsgVO> getUserMsgVOListByIds(List<Integer> userIdList) {
        return baseMapper.getUserMsgVOListByIds(StrUtil.join(",",userIdList));
    }

}
