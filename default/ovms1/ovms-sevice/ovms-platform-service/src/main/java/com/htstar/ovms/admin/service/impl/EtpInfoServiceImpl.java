package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.dto.DeptTree;
import com.htstar.ovms.admin.api.dto.MenuTree;
import com.htstar.ovms.admin.api.dto.TreeNode;
import com.htstar.ovms.admin.api.entity.*;
import com.htstar.ovms.admin.api.req.DistributeEtpAdminReq;
import com.htstar.ovms.admin.api.req.RegisterEtpReq;
import com.htstar.ovms.admin.api.vo.*;
import com.htstar.ovms.admin.mapper.EtpInfoMapper;
import com.htstar.ovms.admin.service.*;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.feign.CarInfoFeign;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
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
    private SysDeptRelationService deptRelationService;
    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SmsService smsService;

    @Autowired
    private CarInfoFeign carInfoFeign;

    @Value("${etp.default.dept.name:企业默认机构}")
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
     *
     * @param etpInfo 企业实体
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.TENANT_DETAILS)
    @Override
    public R saveEtpInfo(EtpInfo etpInfo) {
        OvmsUser user = SecurityUtils.getUser();
        //查询企业级别
        QueryWrapper<EtpInfo> etp = new QueryWrapper<>();
        etp.lambda().eq(EtpInfo::getId, user.getEtpId());//根据当前企业id查询当前企业级别
//        EtpInfo etpInfo1 = baseMapper.selectOne(etp);
//        if (user.getEtpId() == CommonConstants.ETP_ID_1) etpInfo.setEtpLevel(1);
//        if (etpInfo1.getEtpLevel() == 1) etpInfo.setEtpLevel(2);
//        if (etpInfo1.getEtpLevel() == 2) etpInfo.setEtpLevel(3);
//        if (etpInfo1.getEtpLevel() == 3) return R.failed("省级下机构，无法添加企业！");
        long count = this.baseMapper.exsitThisByName(etpInfo);
        if (count > 0) {
            return R.failed("该租户已存在！");
        }
        SysUser ex = userService.getUserForLogin(etpInfo.getContact());
        if (null != ex) {
            return R.failed("手机号已存在");
        }
        etpInfo.setStaTime(OvmDateUtil.getCstNowDate());
        etpInfo.setDistributeStatus(2);
        etpInfo.setParentId(user.getEtpId());//父级企业
        etpInfo.setEtpType(2);
        etpInfo.setEndTime(LocalDate.parse("2030-12-29"));//默认到期时间
        this.save(etpInfo);
        //随机5位企业代码
        Integer etpId = etpInfo.getId();
        //baseMapper.selectCount(new QueryWrapper<EtpInfo>().eq("etp_no", ))
        String randStr = RandomUtil.randomNumbers(4);

        String etpNo = (etpId + "" + randStr).substring(0, 5);


        // 保证插入租户为新的租户
        TenantContextHolder.setEtpId(etpId);

        // 构造查勘员角色
        SysRole driver = new SysRole();
        driver.setRoleCode(CommonConstants.ROLE_DRIVER);
        driver.setRoleDesc("内置角色，不可删除与修改");
        driver.setRoleName(CommonConstants.ROLE_DRIVER_NAME);

        // 构造员工角色
//        SysRole staff = new SysRole();
//        staff.setRoleCode(CommonConstants.ROLE_STAFF);
//        staff.setRoleDesc("内置角色，不可删除与修改");
//        staff.setRoleName(CommonConstants.ROLE_STAFF_NAME);

//        //TODO 为这些默认角色添加菜单权限
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


//        List<SysRole> list = new ArrayList<>();
//        list.add(driver);
////        list.add(staff);
//
//        roleService.saveBatch(list);
        this.baseMapper.updateEtpNo(etpId, etpNo);
        return R.ok();
    }

    /**
     * 跟传入的分级获取当前级以及所有上级，并且获取当前级选中的值
     *
     * @param category
     * @return
     */
    @Override
    public Map<Integer, List<EtpInfoSVo>> getCurrentAndParents(EtpInfoSVo category) {
        return null;
    }

    /**
     * 根据当前级获取上级的id以及当前级选中的id
     *
     * @param category
     * @param ids
     * @return
     */
    private Map<Integer, Integer> getParents(EtpInfoSVo category, Map<Integer, Integer> ids) {
        return ids;
    }


    /**
     * 以树形分级的形式获取列表
     *
     * @return
     */
    @Override
    public List<EtpInfoSVo> makeTree() {
        OvmsUser user = SecurityUtils.getUser();
        List<Integer> i = new ArrayList<>();
        List<EtpInfoSVo> tree = new ArrayList<EtpInfoSVo>();
        List<EtpInfoSVo> root = null;
        Integer etp = 0;
        List<Integer> ids = new ArrayList<Integer>();
        if (user.getEtpId() == CommonConstants.ETP_ID_1) {
            root = getRootCategory();
        } else {
            root = getRootCategorys(etp);
        }
        for (EtpInfoSVo category : root) {
            category = getChildrenTree(category, i, user.getEtpId(), ids);
            tree.add(category);
        }
        return tree;
    }

    /**
     * 以树形分级的形式获取列表
     *
     * @return
     */
    @Override
    public List<EtpInfoSVo> makeTrees(Integer etpId) {
        OvmsUser user = SecurityUtils.getUser();
        List<Integer> i = new ArrayList<>();
        List<EtpInfoSVo> tree = new ArrayList<EtpInfoSVo>();
        List<EtpInfoSVo> root = null;
        List<Integer> ids = new ArrayList<Integer>();
        if (etpId == null || etpId == 0) {
            etpId = user.getEtpId();
        }
        if (etpId == CommonConstants.ETP_ID_1) {
            root = getRootCategory();
        } else {
            root = getRootCategorys(etpId);
        }
        for (EtpInfoSVo category : root) {
            category = getChildrenTree(category, i, user.getEtpId(), ids);
            tree.add(category);
        }
        return tree;
    }

    /**
     * 获取顶级分类
     *
     * @return
     */
    private List<EtpInfoSVo> getRootCategory() {
        List<EtpInfoSVo> etpTree = baseMapper.getEtpTree();//查询总企业
        List<EtpInfoSVo> root = new ArrayList<EtpInfoSVo>();
        for (EtpInfoSVo category : etpTree) {
            if (category.getParentId().intValue() == 0) {
                root.add(category);
            }
        }
        return root;
    }

    /**
     * 获取顶级分类
     *
     * @return
     */
    private List<EtpInfoSVo> getRootCategorys(Integer etpId) {
        OvmsUser user = SecurityUtils.getUser();
        if (etpId == null || etpId == 0) {
            etpId = user.getEtpId();
        }

        List<EtpInfoSVo> etpTree = baseMapper.getEtpTree();//查询总企业
        List<EtpInfoSVo> root = new ArrayList<EtpInfoSVo>();
        for (EtpInfoSVo category : etpTree) {
            if (category.getParentId().intValue() != 0 && category.getId().intValue() == etpId.intValue()) {
                root.add(category);
            }
        }
        return root;
    }

    /**
     * 以树形的形式获取子分类
     *
     * @param parent
     * @return
     */
    private EtpInfoSVo getChildrenTree(EtpInfoSVo parent, List<Integer> i, Integer parentId, List<Integer> ids) {
        OvmsUser user = SecurityUtils.getUser();
        List<EtpInfoSVo> etpTree = baseMapper.getEtpTree();//查询总企业
        List<EtpInfoSVo> children = new ArrayList<EtpInfoSVo>();
//        List<DeptTree> deptTrees = new ArrayList<>();
        for (EtpInfoSVo category : etpTree) {
            if (category.getParentId().intValue() == parent.getId().intValue()) {
                children.add(getChildrenTree(category, i, parentId, ids));
            }
        }
        ids.add(parent.getId());
        parent.setChildren(children);
        for (EtpInfoSVo child : parent.getChildren()) {
            //deptTrees = deptService.selectTreeByEtpIdTree(child.getId());
            //ids.add(child.getId());
            //parent.setIds(ids);
            //child.setDeptTreeList(deptTrees);
        }
        parent.setIds(ids);
        //parent.setDeptTreeList(deptService.selectTreeByEtpIdTree(parent.getId()));
        return parent;
    }

    /**
     * 企业分配管理员
     *
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.TENANT_DETAILS)
    public R distributeEtpAdmin(DistributeEtpAdminReq req) {
        OvmsUser users = SecurityUtils.getUser();
        SysUser ex = userService.getUserForLogin(req.getUsername());
        if (null != ex) {
            return R.failed("该手机号已经存在！");
        }
        Integer roleIdByCode = roleService.getRoleIdByCode(CommonConstants.ROLE_ADMIN, users.getEtpId());
        List<SysRoleMenu> list = roleMenuService.list(Wrappers.<SysRoleMenu>query()
                .lambda().eq(SysRoleMenu::getRoleId, roleIdByCode));

        // 查询企业新增默认赋予的菜单
        //List<SysMenu> menuList = menuService.queryEtpMenuList();
        List<SysMenu> menuLists = menuService.queryEtpMenuLists(users.getEtpId());
        // 保证插入租户为新的租户
        TenantContextHolder.setEtpId(req.getEtpId());
        //通过当前企业查询
        List<SysMenu> menuListst = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : list) {
            for (SysMenu sysMenu : menuLists) {
                if (sysRoleMenu.getMenuId().intValue() == sysMenu.getMenuId().intValue()) {
                    menuListst.add(sysMenu);
                }
            }
        }
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
        if (StrUtil.isBlank(req.getPassword())) {
            user.setPassword(ENCODER.encode(defaultPassword));
        } else {
            user.setPassword(ENCODER.encode(req.getPassword()));
        }
        user.setDeptId(dept.getDeptId());
        if (StrUtil.isNotBlank(req.getNickName())) {
            user.setNickName(req.getNickName());
        }
        userService.save(user);
        // 构造新角色管理员
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
        saveTenantMenu(TreeUtil.buildTree(menuListst, CommonConstants.MENU_TREE_ROOT_ID), CommonConstants.MENU_TREE_ROOT_ID);
        List<SysMenu> newMenuList = menuService.list();

        // 查询全部菜单,构造角色菜单关系
        List<SysRoleMenu> collect = newMenuList.stream().map(menu -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(role.getRoleId());
            roleMenu.setMenuId(menu.getMenuId());
            return roleMenu;
        }).collect(Collectors.toList());
        roleMenuService.saveBatch(collect);

        this.baseMapper.updateDistributeStatus(req.getEtpId(), 1);
        this.saveDrivce(user.getUserId(), menuLists);
        return R.ok();
    }

    public void saveDrivce(Integer userId, List<SysMenu> menuLists) {
        OvmsUser users = SecurityUtils.getUser();
        Integer roleIdByCodes = roleService.getRoleIdByCode(CommonConstants.ROLE_DRIVER, users.getEtpId());
        List<SysRoleMenu> lists = roleMenuService.list(Wrappers.<SysRoleMenu>query()
                .lambda().eq(SysRoleMenu::getRoleId, roleIdByCodes));
        //通过当前企业查询
        List<SysMenu> menuListsts = new ArrayList<>();

        for (SysMenu sysMenu : menuLists) {
            for (SysRoleMenu sysRoleMenu : lists) {
                if (sysRoleMenu.getMenuId().intValue() == sysMenu.getMenuId().intValue()) {
                    menuListsts.add(sysMenu);
                }
            }
        }
        // 构造新角色查勘员
        SysRole roles = new SysRole();
        roles.setRoleCode(CommonConstants.ROLE_DRIVER);
        roles.setRoleDesc("内置角色，不可删除与修改");
        roles.setRoleName(CommonConstants.ROLE_DRIVER_NAME);
        roleService.save(roles);
        // 用户角色关系
        SysUserRole userRoles = new SysUserRole();
        userRoles.setUserId(userId);
        userRoles.setRoleId(roles.getRoleId());
        userRoleService.save(userRoles);
        // 插入新的菜单
        //saveTenantMenu(TreeUtil.buildTree(menuListsts, CommonConstants.MENU_TREE_ROOT_ID), CommonConstants.MENU_TREE_ROOT_ID);
        List<SysMenu> newMenuList = menuService.list();
        // 查询全部菜单,构造角色菜单关系
        List<SysMenu> menuList = new ArrayList<>();
        for (SysMenu menuListst : newMenuList) {
            for (SysMenu sysMenu : menuListsts) {
                if (Objects.equals(menuListst.getName(), sysMenu.getName())) {
                    menuList.add(menuListst);
                }
            }
        }
        List<SysRoleMenu> collects = menuList.stream().map(menu -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roles.getRoleId());
            roleMenu.setMenuId(menu.getMenuId());
            return roleMenu;
        }).collect(Collectors.toList());
        roleMenuService.saveBatch(collects);
    }

    @Override
    public R queryPage(Page page, EtpInfo sysTenant) {
        OvmsUser user = SecurityUtils.getUser();
        if (sysTenant.equals(null)) {
            sysTenant.setId(0);
        }
        List<Integer> ids = new ArrayList<>();
        List<EtpInfoSVo> currentAndParents1 = makeTrees(sysTenant.getId());
        currentAndParents1.forEach(etpInfoSVo -> {
            etpInfoSVo.getIds().forEach(integer -> {
                ids.add(integer);
            });
        });

//        if(Objects.equals(ids.size(),0)){
//            System.out.println("进来");
//            ids.add(user.getEtpId());
//        }
        IPage<EtpPageVO> pageVOIPage = this.baseMapper.queryPage(page, sysTenant, ids);
        return R.ok(pageVOIPage);
    }

    /**
     * 查找子企业
     *
     * @param parentId
     * @return
     */
    @Override
    public List<EtpInfoSVo> getEtps(Integer parentId) {
        return baseMapper.getEtpTrees(parentId);
    }

    /**
     * Description: 通过企业编码获取企业信息
     * Author: flr
     * Company: 航通星空
     */
    @Override
    public R<EtpInfoVO> getEtpInfoByCode(String etpNo) {
        EtpInfo etpInfo = this.baseMapper.getNormalEtp(etpNo, OvmDateUtil.getCstNow());
        if (etpInfo == null) {
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
    public R registerEtp(RegisterEtpReq req) {
        if (!smsService.checkSmsCode(req.getAdminMobile(), req.getMobileCode())) {
            return R.failed("验证码错误");
        }
        SysUser ex = userService.getUserForLogin(req.getAdminMobile());
        if (null != ex) {
            return R.failed("手机号已存在");
        }
        EtpInfo etpInfo = new EtpInfo();
        etpInfo.setEtpName(req.getEtpName());
        long count = this.baseMapper.exsitThisByName(etpInfo);
        if (count > 0) {
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
        etpInfo.setContact(req.getAdminMobile());
        this.save(etpInfo);
        //随机5位企业代码
        Integer etpId = etpInfo.getId();
        String randStr = RandomUtil.randomNumbers(4);
        String etpNo = (etpId + "" + randStr).substring(0, 5);
        this.baseMapper.updateEtpNo(etpId, etpNo);
        // 保证插入租户为新的租户
        TenantContextHolder.setEtpId(etpId);
        // 构造司机角色
        SysRole driver = new SysRole();
        driver.setRoleCode(CommonConstants.ROLE_DRIVER);
        driver.setRoleDesc("内置角色，不可删除与修改");
        driver.setRoleName(CommonConstants.ROLE_DRIVER_NAME);
        //todo  不再构造员工角色
        SysRole staff = new SysRole();
//        staff.setRoleCode(CommonConstants.ROLE_STAFF);
//        staff.setRoleDesc("内置角色，不可删除与修改");
//        staff.setRoleName(CommonConstants.ROLE_STAFF_NAME);
        //TODO 为这些默认角色添加菜单权限
        List<SysRole> list = new ArrayList<>();
        list.add(driver);
        //list.add(staff);
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

    /**
     * 车辆定位企业树
     *
     * @return
     */
    @Override
    public List<EtpInfoVO> getEtpMonitoringTree() {
        List<EtpInfoVO> etpInfoVOS = new ArrayList<>();
        OvmsUser user = SecurityUtils.getUser();
        List<EtpInfo> etpInfoList = null;
        if (user != null) {
            if (user.getEtpId() == CommonConstants.ETP_ID_1) {
                etpInfoList = baseMapper.selectList(new QueryWrapper<EtpInfo>());
            } else {
                etpInfoList = baseMapper.selectList(new QueryWrapper<EtpInfo>().eq("id", user.getEtpId()));
            }
        }
        if (CollectionUtils.isNotEmpty(etpInfoList)) {
            for (EtpInfo etpInfo : etpInfoList) {
                EtpInfoVO etpInfoVO = new EtpInfoVO();
                etpInfoVO.setId(etpInfo.getId());
                etpInfoVO.setEtpName(etpInfo.getEtpName());
                etpInfoVO.setEtpNo(etpInfo.getEtpNo());
                //获取企业的部门树
                List<DeptTree> deptTrees = deptService.selectTreeByEtpIdTree(etpInfo.getId());
                if (CollectionUtils.isNotEmpty(deptTrees)) {
                    for (DeptTree deptTree : deptTrees) {
                        //子部门获取车辆信息
                        if (CollectionUtils.isNotEmpty(deptTree.getChildren())) {
                            getChildrenCarInfo(deptTree.getChildren());
                        } else {
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
        etp.setEtpType(2);
        etp.setEndTime(LocalDate.parse("2030-12-29"));//默认到期时间
        EtpInfo ex = this.getById(etp.getId());
        if (null == ex) {
            return R.failed("企业不存在！");
        }
        if (ex.getId() == CommonConstants.ETP_ID_1) {
            return R.failed("顶级企业不允许修改基本信息！");
        }
        this.updateById(etp);
        return R.ok();
    }


    /**
     * 获取车辆信息
     *
     * @param deptNode
     */
    private void getCarInfoVOList(TreeNode deptNode) {
        R carInfoByDeptId = carInfoFeign.getCarInfoByDeptId(deptNode.getParentId());
        deptNode.setCarInfoVOList((List<CarInfoVO>) carInfoByDeptId.getData());
        if (CollectionUtils.isEmpty(deptNode.getChildren())) {
            getCarInfo(deptNode);
        }

    }

    /**
     * 把车辆信息存放到字节中
     *
     * @param deptNode
     */
    private void getCarInfo(TreeNode deptNode) {
        R carInfoByDeptId = carInfoFeign.getCarInfoByDeptId(deptNode.getId());
        TreeNode treeNode = new TreeNode();
        treeNode.setCarInfoVOList((List<CarInfoVO>) carInfoByDeptId.getData());
        List<TreeNode> list = new ArrayList<>();
        list.add(treeNode);
        deptNode.setChildren(list);
    }

    /**
     * 递归部门获取车辆信息
     */
    private void getChildrenCarInfo(List<TreeNode> treeNodeList) {
        for (TreeNode child : treeNodeList) {
            getChildrenCarInfo(child.getChildren());
            getCarInfoVOList(child);
        }
    }


    /**
     * 以树形分级的形式获取列表
     *
     * @return
     */
    @Override
    public int removeMakeTree(Integer id) {
        List<EtpInfoSVo> etpInfoSVos = makeTrees(id);
        List<Integer> ids = new ArrayList<>();
        etpInfoSVos.forEach(etpInfoSVo -> {
            etpInfoSVo.getIds().forEach(integer -> {
                ids.add(integer);
            });
        });
        int i = baseMapper.updateEtps(ids);
        return i;
    }

    /**
     * 以树形的形式获取子分类禁用状态
     *
     * @param
     * @return
     */
    @Override
    public int updateGetChildrenTree(Integer id, Integer disablestatus) {
        List<EtpInfoSVo> etpInfoSVos = makeTrees(id);
        List<Integer> ids = new ArrayList<>();
        etpInfoSVos.forEach(etpInfoSVo -> {
            etpInfoSVo.getIds().forEach(integer -> {
                ids.add(integer);
            });
        });
        int i = 0;
        if (disablestatus == 9) {
            i = baseMapper.disablestatusEtps(ids, 9);
        } else {
            i = baseMapper.disablestatusEtps(ids, 0);
        }

        return i;
    }

    /**
     * hsl
     * -----------------TODO 企业树节点（无部门树）-----------------
     */
    @Override
    public List<EtpInfoPrentVO> getEtpTreeNoDeptHsl(Integer fromEtpId) {
        List<EtpInfo> etpInfoList = baseMapper.selectList(Wrappers.emptyWrapper());
        List<EtpInfoPrentVO> etpInfoPrentVOList = etpInfoList.stream().map(etpInfo -> {
            EtpInfoPrentVO etpInfoPrentVO = new EtpInfoPrentVO();
            etpInfoPrentVO.setId(etpInfo.getId());
            etpInfoPrentVO.setEtpName(etpInfo.getEtpName());
            etpInfoPrentVO.setEtpNo(etpInfo.getEtpNo());
            etpInfoPrentVO.setParentId(etpInfo.getParentId().toString());
            return etpInfoPrentVO;
        }).collect(Collectors.toList());
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();//默认为当前用户，
        if (fromEtpId != null) {
            etpId = fromEtpId;
        }
        EtpInfo currentEtp = baseMapper.selectOne(Wrappers.<EtpInfo>lambdaQuery().eq(EtpInfo::getId, etpId));
        EtpInfoPrentVO etpInfoPrentVO = new EtpInfoPrentVO();
        BeanUtils.copyProperties(currentEtp, etpInfoPrentVO);
        etpInfoPrentVO.setParentId(currentEtp.getParentId().toString());
        //根据当前企业ID过滤出它的子企业List
        Integer finalEtpId = etpId;
        List<EtpInfoPrentVO> child = etpInfoPrentVOList.stream().filter(etpInfo -> etpInfo.getParentId().equals(finalEtpId.toString())).collect(Collectors.toList());
        //递归添加企业子级树
        etpInfoPrentVO.setIds(new ArrayList<>());
        genTreeNodeNoDeptHsl(etpInfoPrentVO, child, etpInfoPrentVOList);
        //完成企业树的创建
        ArrayList<EtpInfoPrentVO> done = new ArrayList<>();
        done.add(etpInfoPrentVO);
        return done;
    }

    //递归方法（无部门树）
    public void genTreeNodeNoDeptHsl(EtpInfoPrentVO parent, List<EtpInfoPrentVO> childs, List<EtpInfoPrentVO> all) {
        List<EtpInfoPrentVO> collect = all.stream().filter(child -> parent.getId() == Integer.valueOf(child.getParentId())).collect(Collectors.toList());
        for (EtpInfoPrentVO etpInfo : collect) {
            etpInfo.setIds(new ArrayList<>());
            parent.getIds().add(etpInfo.getId());//添加单级子集合中的ID到父节点
            genTreeNodeWithDetpTree(etpInfo, collect, all);//递归
            parent.getIds().addAll(etpInfo.getIds());//返回时追加该子层中每一个下属IDS
        }
        parent.setChildren(collect);
    }

    /**
     * hsl
     * ----------------TODO 企业树节点(带部门树与车辆信息)--------------
     */
    public List<EtpInfoPrentVO> getEtpTreeWithDeptTreeHsl() {
        List<EtpInfo> etpInfoList = baseMapper.selectList(Wrappers.emptyWrapper());
        List<EtpInfoPrentVO> etpInfoPrentVOList = etpInfoList.stream().map(etpInfo -> {
            EtpInfoPrentVO etpInfoPrentVO = new EtpInfoPrentVO();
            etpInfoPrentVO.setId(etpInfo.getId());
            etpInfoPrentVO.setEtpName(etpInfo.getEtpName() + "(企业)");
            etpInfoPrentVO.setEtpNo(etpInfo.getEtpNo());
            etpInfoPrentVO.setParentId(etpInfo.getParentId().toString());
            return etpInfoPrentVO;
        }).collect(Collectors.toList());//得到rawList

        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        EtpInfo currentEtp = baseMapper.selectOne(Wrappers.<EtpInfo>lambdaQuery().eq(EtpInfo::getId, etpId));
        EtpInfoPrentVO etpInfoPrentVO = new EtpInfoPrentVO();
        BeanUtils.copyProperties(currentEtp, etpInfoPrentVO);
        etpInfoPrentVO.setParentId(currentEtp.getParentId().toString());
        //根据当前企业ID过滤出它的子企业List
        List<EtpInfoPrentVO> child = etpInfoPrentVOList.stream().filter(etpInfo -> etpInfo.getParentId().equals(etpId.toString())).collect(Collectors.toList());
        //递归添加企业子级树
        etpInfoPrentVO.setIds(new ArrayList<>());
        genTreeNodeWithDetpTree(etpInfoPrentVO, child, etpInfoPrentVOList);
        //完成企业树的创建
        ArrayList<EtpInfoPrentVO> done = new ArrayList<>();
        done.add(etpInfoPrentVO);
        return done;
    }


    //递归方法(有部门树)
    public void genTreeNodeWithDetpTree(EtpInfoPrentVO parent, List<EtpInfoPrentVO> childs, List<EtpInfoPrentVO> all) {
        List<EtpInfoPrentVO> collect = all.stream().filter(child -> parent.getId() == Integer.valueOf(child.getParentId())).collect(Collectors.toList());
        for (EtpInfoPrentVO etpInfo : collect) {
            etpInfo.setIds(new ArrayList<>());
//            Integer deptId = etpInfo.getId();
            //-----构建部门树Head------
            //获取企业的部门树（调用服务）
            List<DeptTree> deptTrees = deptService.selectTreeByEtpIdTree(etpInfo.getId());
            if (CollectionUtils.isNotEmpty(deptTrees)) {
                for (DeptTree deptTree : deptTrees) {
                    //子部门获取车辆信息
                    if (CollectionUtils.isNotEmpty(deptTree.getChildren())) {
                        getChildrenCarInfo(deptTree.getChildren());
                    } else {
                        getCarInfo(deptTree);
                    }
                }
            }
            etpInfo.setDeptTreeList(deptTrees);
            //-----构建部门树Tail------
            parent.getIds().add(etpInfo.getId());//添加单级子集合中的ID到父节点
            genTreeNodeWithDetpTree(etpInfo, collect, all);//递归
            parent.getIds().addAll(etpInfo.getIds());//返回时追加该子层中每一个下属IDS
        }
        parent.setChildren(collect);
    }


    /**
     * 部门树与人员
     *
     * @return
     */
    public List<EtpInfoPrentVO> getDetpTreeAndPeopel() {
        List<EtpInfoPrentVO> etpInfoVOS = new ArrayList<>();
        OvmsUser user = SecurityUtils.getUser();
        List<EtpInfo> etpInfoList = null;
        etpInfoList = baseMapper.selectList(new QueryWrapper<EtpInfo>().eq("id", user.getEtpId()));
        if (CollectionUtils.isNotEmpty(etpInfoList)) {
            for (EtpInfo etpInfo : etpInfoList) {
                EtpInfoPrentVO etpInfoVO = new EtpInfoPrentVO();
                etpInfoVO.setId(etpInfo.getId());
                etpInfoVO.setEtpName(etpInfo.getEtpName());
                etpInfoVO.setEtpNo(etpInfo.getEtpNo());
                //获取企业的部门树带员工
                List<DeptTree> mydeptTrees = deptService.myselectTreeByEtpIdTree(etpInfo.getId());
                etpInfoVO.setDeptTreeList(mydeptTrees);
                etpInfoVOS.add(etpInfoVO);
            }
        }
        return etpInfoVOS;
    }
}
