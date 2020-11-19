package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.dto.DeptTree;
import com.htstar.ovms.admin.api.dto.MenuTree;
import com.htstar.ovms.admin.api.dto.TreeNode;
import com.htstar.ovms.admin.api.entity.*;
import com.htstar.ovms.admin.api.req.DistributeEtpAdminReq;
import com.htstar.ovms.admin.api.req.RegisterEtpReq;
import com.htstar.ovms.admin.api.vo.CarInfoVO;
import com.htstar.ovms.admin.api.vo.EtpInfoVO;
import com.htstar.ovms.admin.api.vo.EtpPageVO;
import com.htstar.ovms.admin.api.vo.TreeUtil;
import com.htstar.ovms.admin.mapper.EtpInfoMapper;
import com.htstar.ovms.admin.service.*;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantBroker;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.feign.CarInfoFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业表
 *
 * @author htxk
 * @date 2020-06-05 09:58:53
 */
@Service
public class EtpInfoServiceImpl extends ServiceImpl<EtpInfoMapper, EtpInfo> implements EtpInfoService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private  SysDeptRelationService deptRelationService;
    @Autowired
    private  SysRoleMenuService roleMenuService;
    @Autowired
    private  SysRoleService roleService;
    @Autowired
    private  SysMenuService menuService;
    @Autowired
    private  SysDeptService deptService;
    @Autowired
    private  SysUserService userService;
    @Autowired
    private  SysUserRoleService userRoleService;
    @Autowired
    private SmsService smsService;

    @Autowired
    private CarInfoFeign carInfoFeign;

    @Value("${etp.default.dept.name:企业默认部门}")
    private String defaultDeptName;
    @Value("${etp.default.password:123456}")
    private String defaultPassword;



    /**
     * 获取正常状态租户
     * <p>
     * 1. 状态正常
     * 2. 开始时间小于等于当前时间
     * 3. 结束时间大于等于当前时间
     *
     * @return
     */
    @Override
    @Cacheable(value = CacheConstants.TENANT_DETAILS)
    public List<EtpInfo> getNormalTenant() {
        return baseMapper.queryNormalTenant(OvmDateUtil.getCstNow());
    }

    /**
     * 保存企业
     * <p>
     * 1. 保存租户
     * 2. 初始化权限相关表
     * - sys_role
     * - sys_menu
     * - sys_user_role
     * - sys_role_menu
     * - sys_client_details
     * @param etpInfo 企业实体
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.TENANT_DETAILS)
    @Override
    public R saveEtpInfo(EtpInfo etpInfo) {
        long count = this.baseMapper.exsitThisByName(etpInfo);

        if (count > 0){
            return R.failed("该租户已存在！");
        }
        etpInfo.setStaTime(OvmDateUtil.getCstNowDate());
        etpInfo.setDistributeStatus(2);

        this.save(etpInfo);
        //随机5位企业代码
        Integer etpId = etpInfo.getId();
        //baseMapper.selectCount(new QueryWrapper<EtpInfo>().eq("etp_no", ))
        String randStr = RandomUtil.randomNumbers(4);

        String etpNo = (etpId + "" + randStr).substring(0,5);


        // 保证插入租户为新的租户
        TenantContextHolder.setEtpId(etpId);

        // 构造司机角色
        SysRole driver = new SysRole();
        driver.setRoleCode(CommonConstants.ROLE_DRIVER);
        driver.setRoleDesc("内置角色，不可删除与修改");
        driver.setRoleName(CommonConstants.ROLE_DRIVER_NAME);

        // 构造员工角色
        SysRole staff = new SysRole();
        staff.setRoleCode(CommonConstants.ROLE_STAFF);
        staff.setRoleDesc("内置角色，不可删除与修改");
        staff.setRoleName(CommonConstants.ROLE_STAFF_NAME);

        //TODO 为这些默认角色添加菜单权限
//        List<SysMenu> newMenuList = menuService.list();
//        List<SysRoleMenu> menus = new ArrayList<>();
//        for (SysMenu menu : newMenuList){
//            SysRoleMenu roleMenu = new SysRoleMenu();
//            roleMenu.setMenuId();
//            roleMenu.setRoleId();
//
//            menus.add(roleMenu);
//        }
//        roleMenuService.saveBatch(menus);



        List<SysRole> list = new ArrayList<>();
        list.add(driver);
        list.add(staff);

        roleService.saveBatch(list);
        this.baseMapper.updateEtpNo(etpId,etpNo);
        return R.ok();
    }

    /**
     * 企业分配管理员
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.TENANT_DETAILS)
    public R distributeEtpAdmin(DistributeEtpAdminReq req) {

        SysUser ex = userService.getUserForLogin(req.getUsername());
        if (null != ex){
            return R.failed("该手机号已经存在！");
        }

        // 查询企业新增默认赋予的菜单
        List<SysMenu> menuList = menuService.queryEtpMenuList();
        // 保证插入租户为新的租户
        TenantContextHolder.setEtpId(req.getEtpId());

        // 插入部门
        SysDept dept = new SysDept();
        dept.setName(defaultDeptName);
        dept.setParentId(0);
        deptService.save(dept);
        //维护部门关系
        deptRelationService.insertDeptRelation(dept);
        // 构造初始化用户
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPhone(req.getUsername());
        if (StrUtil.isBlank(req.getPassword())){
            user.setPassword(ENCODER.encode(defaultPassword));
        }else {
            user.setPassword(ENCODER.encode(req.getPassword()));
        }

        user.setDeptId(dept.getDeptId());
        if (StrUtil.isNotBlank(req.getNickName())){
            user.setNickName(req.getNickName());
        }
        userService.save(user);
        // 构造新角色
        SysRole role = new SysRole();
        role.setRoleCode(CommonConstants.ROLE_ADMIN);
        role.setRoleDesc("内置角色，不可删除与修改");
        role.setRoleName(CommonConstants.ROLE_ADMIN_NAME);
        roleService.save(role);
        // 用户角色关系
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRoleService.save(userRole);
        // 插入新的菜单
        saveTenantMenu(TreeUtil.buildTree(menuList, CommonConstants.MENU_TREE_ROOT_ID), CommonConstants.MENU_TREE_ROOT_ID);
        List<SysMenu> newMenuList = menuService.list();

        // 查询全部菜单,构造角色菜单关系
        List<SysRoleMenu> collect = newMenuList.stream().map(menu -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(role.getRoleId());
            roleMenu.setMenuId(menu.getMenuId());
            return roleMenu;
        }).collect(Collectors.toList());
        roleMenuService.saveBatch(collect);

        this.baseMapper.updateDistributeStatus(req.getEtpId(),1);
        return R.ok();
    }

    @Override
    public R queryPage(Page page, EtpInfo sysTenant) {
        IPage<EtpPageVO> pageVOIPage = this.baseMapper.queryPage(page, sysTenant);
        return R.ok(pageVOIPage);
    }

    /**
     * Description: 通过企业编码获取企业信息
     * Author: flr
     * Company: 航通星空
     */
    @Override
    public R<EtpInfoVO> getEtpInfoByCode(String etpNo) {
        EtpInfo etpInfo = this.baseMapper.getNormalEtp(etpNo,OvmDateUtil.getCstNow());
        if (etpInfo == null){
            return R.failed("企业不存在!");
        }

        EtpInfoVO etpInfoVO = new EtpInfoVO();
        etpInfoVO.setId(etpInfo.getId());
        etpInfoVO.setEtpName(etpInfo.getEtpName());
        etpInfoVO.setEtpNo(etpNo);

//        TenantContextHolder.setEtpId(etpInfo.getId());
//        List<DeptTree> deptTrees = deptService.selectTree();
//        etpInfoVO.setDeptTreeList(deptTrees);
//        return R.ok(etpInfoVO);
//        return TenantBroker.applyAs(etpInfo.getId(), (id -> {
//            List<DeptTree> deptTrees = deptService.selectTree();
//            etpInfoVO.setDeptTreeList(deptTrees);
//            return R.ok(etpInfoVO);
//        }));



        List<DeptTree> deptTrees = deptService.selectTreeByEtpId(etpInfo.getId());
        etpInfoVO.setDeptTreeList(deptTrees);
        return R.ok(etpInfoVO);

    }


    /**
     * 保存新的租户菜单，维护成新的菜单
     *
     * @param nodeList 节点树
     * @param parent   上级
     */
    private void saveTenantMenu(List<MenuTree> nodeList, Integer parent) {
        for (MenuTree node : nodeList) {
            SysMenu menu = new SysMenu();
            BeanUtils.copyProperties(node, menu, "parentId");
            menu.setParentId(parent);
            menuService.save(menu);
            if (CollUtil.isNotEmpty(node.getChildren())) {
                List<MenuTree> childrenList = node.getChildren().stream()
                        .map(treeNode -> (MenuTree) treeNode).collect(Collectors.toList());
                saveTenantMenu(childrenList, menu.getMenuId());
            }
        }
    }

    /**
     * Description: 注册企业
     * Author: flr
     * Date: 2020/8/8 17:04
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R registerEtp(RegisterEtpReq req){
        if (!smsService.checkSmsCode(req.getAdminMobile(),req.getMobileCode())){
            return R.failed("验证码错误");
        }
        SysUser ex = userService.getUserForLogin(req.getAdminMobile());
        if (null != ex){
            return R.failed("手机号已存在");
        }
        EtpInfo etpInfo = new EtpInfo();
        etpInfo.setEtpName(req.getEtpName());
        long count = this.baseMapper.exsitThisByName(etpInfo);
        if (count > 0){
            return R.failed("该企业已存在！");
        }
        LocalDate cstNowDate = OvmDateUtil.getCstNowDate();
        etpInfo.setStaTime(cstNowDate);
        //试用期一个月
        etpInfo.setEndTime(cstNowDate.plusMonths(1));
        etpInfo.setIndustry(req.getIndustry());
        etpInfo.setAdminName(req.getAdminName());
        etpInfo.setEtpType(req.getEtpType());
        etpInfo.setEtpAddr(req.getEtpAddr());
        this.save(etpInfo);
        //随机5位企业代码
        Integer etpId = etpInfo.getId();
        String randStr = RandomUtil.randomNumbers(4);
        String etpNo = (etpId + "" + randStr).substring(0,5);
        this.baseMapper.updateEtpNo(etpId,etpNo);
        // 保证插入租户为新的租户
        TenantContextHolder.setEtpId(etpId);
        // 构造司机角色
        SysRole driver = new SysRole();
        driver.setRoleCode(CommonConstants.ROLE_DRIVER);
        driver.setRoleDesc("内置角色，不可删除与修改");
        driver.setRoleName(CommonConstants.ROLE_DRIVER_NAME);
        // 构造员工角色
        SysRole staff = new SysRole();
        staff.setRoleCode(CommonConstants.ROLE_STAFF);
        staff.setRoleDesc("内置角色，不可删除与修改");
        staff.setRoleName(CommonConstants.ROLE_STAFF_NAME);
        //TODO 为这些默认角色添加菜单权限
        List<SysRole> list = new ArrayList<>();
        list.add(driver);
        list.add(staff);
        roleService.saveBatch(list);
        //同步为企业配置管理员
        DistributeEtpAdminReq disReq = new DistributeEtpAdminReq();
        disReq.setEtpId(etpId);
        disReq.setPassword(req.getPassword());
        disReq.setUsername(req.getAdminMobile());
        disReq.setNickName(req.getAdminName());
        //企业分配管理员
        return this.distributeEtpAdmin(disReq);
    }

    @Override
    public List<EtpInfoVO> getEtpMonitoringTree() {
        List<EtpInfoVO> etpInfoVOS = new ArrayList<>();
        OvmsUser user = SecurityUtils.getUser();
        List<EtpInfo> etpInfoList = null;
        if(user !=null){
            if(user.getEtpId() == CommonConstants.ETP_ID_1){
                etpInfoList = baseMapper.selectList(new QueryWrapper<EtpInfo>());
            }else{
                etpInfoList = baseMapper.selectList(new QueryWrapper<EtpInfo>().eq("id", user.getEtpId()));
            }
        }
        if(CollectionUtils.isNotEmpty(etpInfoList)){
            for (EtpInfo etpInfo : etpInfoList) {
                EtpInfoVO etpInfoVO = new EtpInfoVO();
                etpInfoVO.setId(etpInfo.getId());
                etpInfoVO.setEtpName(etpInfo.getEtpName());
                etpInfoVO.setEtpNo(etpInfo.getEtpNo());
                //获取企业的部门树
                List<DeptTree> deptTrees = deptService.selectTreeByEtpIdTree(etpInfo.getId());
                if(CollectionUtils.isNotEmpty(deptTrees)){
                    for (DeptTree deptTree : deptTrees) {
                        //子部门获取车辆信息
                        if(CollectionUtils.isNotEmpty(deptTree.getChildren())){
                            getChildrenCarInfo(deptTree.getChildren());
                        }else{
                            getCarInfo(deptTree);
                        }
                    }
                }
                etpInfoVO.setDeptTreeList(deptTrees);
                etpInfoVOS.add(etpInfoVO);
            }
        }
        return etpInfoVOS;
    }

    @Override
    public R updateEtp(EtpInfo etp) {
        EtpInfo ex = this.getById(etp.getId());
        if (null == ex){
            return R.failed("企业不存在！");
        }
        if (ex.getId() == CommonConstants.ETP_ID_1){
            return R.failed("顶级企业不允许修改基本信息！");
        }
        this.updateById(etp);
        return R.ok();
    }


    /**
     * 获取车辆信息
     * @param deptNode
     */
    private  void getCarInfoVOList(TreeNode deptNode){
        R carInfoByDeptId = carInfoFeign.getCarInfoByDeptId(deptNode.getParentId());
        deptNode.setCarInfoVOList((List<CarInfoVO>) carInfoByDeptId.getData());
        if(CollectionUtils.isEmpty(deptNode.getChildren())){
            getCarInfo(deptNode);
        }

    }

    /**
     * 把车辆信息存放到字节中
     * @param deptNode
     */
    private void getCarInfo(TreeNode deptNode) {
        R  carInfoByDeptId = carInfoFeign.getCarInfoByDeptId(deptNode.getId());
        TreeNode treeNode = new TreeNode();
        treeNode.setCarInfoVOList((List<CarInfoVO>) carInfoByDeptId.getData());
        List<TreeNode> list = new ArrayList<>();
        list.add(treeNode);
        deptNode.setChildren(list);
    }

    /**
     * 递归部门获取车辆信息
     */
    private void getChildrenCarInfo(List<TreeNode> treeNodeList){
        for (TreeNode child : treeNodeList) {
            getChildrenCarInfo(child.getChildren());
            getCarInfoVOList(child);
        }
    }
}
