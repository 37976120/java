package com.htstar.ovms.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.EtpInfo;
import com.htstar.ovms.admin.api.req.DistributeEtpAdminReq;
import com.htstar.ovms.admin.api.req.RegisterEtpReq;
import com.htstar.ovms.admin.api.vo.EtpInfoPrentVO;
import com.htstar.ovms.admin.api.vo.EtpInfoSVo;
import com.htstar.ovms.admin.api.vo.EtpInfoVO;
import com.htstar.ovms.admin.service.EtpInfoService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/etp")
@Api(value = "etp", tags = "企业管理")
public class EtpInfoController {

    private final EtpInfoService etpInfoService;

    @Inner(value = false)
    @GetMapping("/getInfo/{etpNo}")
    @ApiOperation(value = "通过企业编码获取企业信息", notes = "通过企业编码获取企业信息")
    public R<EtpInfoVO> getEtpInfoByCode(@PathVariable("etpNo") String etpNo) {
        return etpInfoService.getEtpInfoByCode(etpNo);
    }

    /**
     * 分配企业管理员
     *
     * @return R
     */
    @ApiOperation(value = "分配企业管理员", notes = "分配企业管理员")
    @SysLog("分配企业管理员")
    @PostMapping("/distributeEtpAdmin")
    public R distributeEtpAdmin(@RequestBody DistributeEtpAdminReq req) {
        return etpInfoService.distributeEtpAdmin(req);
    }


    /**
     * 分页查询
     *
     * @param page      分页对象
     * @param sysTenant 企业
     * @return
     */
    @ApiOperation(value = "分页", notes = "分页")
    @GetMapping("/page")
    public R getSysTenantPage(Page page, EtpInfo sysTenant) {

        return etpInfoService.queryPage(page, sysTenant);
    }


    /**
     * 通过id查询租户
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过ID查询租户", notes = "通过ID查询租户")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(etpInfoService.getById(id));
    }

    /**
     * 新增企业
     *
     * @param etpInfo 企业
     * @return R
     */
    @ApiOperation(value = "新增企业", notes = "新增企业")
    @SysLog("新增企业")
    @PostMapping
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R save(@RequestBody EtpInfo etpInfo) {

        return etpInfoService.saveEtpInfo(etpInfo);
    }


    @ApiOperation(value = "注册企业", notes = "注册企业")
    @SysLog("注册企业")
    @PostMapping("/register")
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R registerEtp(@RequestBody RegisterEtpReq req) {
        return etpInfoService.registerEtp(req);
    }

    /**
     * 修改企业
     *
     * @param etp 企业
     * @return R
     */
    @ApiOperation(value = "修改企业", notes = "修改企业")
    @SysLog("修改企业")
    @PutMapping
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R updateById(@RequestBody EtpInfo etp) {
        return etpInfoService.updateEtp(etp);
    }

    /**
     * 通过id删除租户
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "删除企业", notes = "删除企业")
    @SysLog("删除企业")
    @DeleteMapping("/{id}")
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R removeById(@PathVariable Integer id) {
        List<EtpInfo> etpInfos = new ArrayList<>();
        if (id == null) {
            return R.failed("请选择要删除的企业！");
        }
        if (id != null && id == CommonConstants.ETP_ID_1) {
            return R.failed("顶级企业不允许删除！");
        }
        EtpInfo ex = etpInfoService.getById(id);
        if (ex.getDelFlag().intValue() == Integer.parseInt(CommonConstants.STATUS_DEL)) {
            return R.ok();
        } else {
            EtpInfo etpInfo = new EtpInfo();
            etpInfo.setId(id);
            etpInfo.setDelFlag(Integer.parseInt(CommonConstants.STATUS_DEL));
            boolean b = etpInfoService.updateById(etpInfo);
//            if (b) {
//                //删除了总企业，那么他下面的所有子企业也要删除
//                List<EtpInfoSVo> etps = etpInfoService.getEtps(etpInfo.getId());
//                etps.forEach(etpInfoSVo -> {
//                    EtpInfo fo = new EtpInfo();
//                    fo.setDelFlag(Integer.parseInt(CommonConstants.STATUS_DEL));
//                    fo.setId(etpInfoSVo.getId());
//                    etpInfos.add(fo);
//                });
//                boolean b1 = etpInfoService.updateBatchById(etpInfos);
//                if(b1){
//                    etps.forEach(etpInfoSVo -> {
//                        List<EtpInfoSVo> etps1 = etpInfoService.getEtps(etpInfoSVo.getId());
//                        if(etps1.size() >0){
//                        for (EtpInfoSVo infoSVo : etps1) {
//                            EtpInfo fo = new EtpInfo();
//                            fo.setDelFlag(Integer.parseInt(CommonConstants.STATUS_DEL));
//                            fo.setId(infoSVo.getId());
//                            etpInfos.add(fo);
//                        }
//                      }
//                        boolean b2 = etpInfoService.updateBatchById(etpInfos);
//                    });
//                }
//            }
            return R.ok(b);
        }

    }

    /**
     * 查询全部有效的租户
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询全部有效的租户", notes = "查询全部有效的租户")
    public R list() {
        OvmsUser user = SecurityUtils.getUser();
        List<EtpInfo> tenants;
        if (user.getEtpId().intValue() == CommonConstants.ETP_ID_1) {
            tenants = etpInfoService.getNormalTenant();
        } else {
            tenants = etpInfoService.getNormalTenant()
                    .stream()
                    .filter(tenant -> tenant.getId().intValue() == user.getEtpId().intValue())
                    .collect(Collectors.toList());
        }
        return R.ok(tenants);
    }


    /**
     * 获取企业树与车辆信息
     */
    @ApiOperation(value = "获取企业树与车辆信息", notes = "获取企业树与车辆信息")
    @GetMapping("getEtpMonitoringTree")
    public R<List<EtpInfoPrentVO>> getEtpMonitoringTree() {
        List<EtpInfoPrentVO> etpTreeWithDeptTreeHsl = etpInfoService.getEtpTreeWithDeptTreeHsl();
        return R.ok(etpTreeWithDeptTreeHsl);
//        return R.ok(etpInfoService.getEtpMonitoringTree()); //旧接口
    }

    /**
     * 查询当前企业和子企业和子企业机构
     */
    @ApiOperation(value = "查询当前企业和子企业和子企业机构", notes = "查询当前企业和子企业和子企业机构")
    @PostMapping("getEtpTree")
    public R<Map<Integer, List<EtpInfoSVo>>> getCurrentAndParents(@RequestBody EtpInfoSVo etpInfo) {
        return R.ok(etpInfoService.getCurrentAndParents(etpInfo));
    }

    /**
     * 查询当前企业和子企业和子企业机构
     */
    @ApiOperation(value = "查询当前企业和子企业和子企业机构,不带参数", notes = "查询当前企业和子企业和子企业机构，不带参数")
    @GetMapping("getEtpTrees")
    public R getCurrentAndParents() {
        return R.ok(etpInfoService.makeTree());
    }

    /**
     * 内部查询当前企业和子企业和子企业机构
     */
    @ApiOperation(value = "查询当前企业和子企业和子企业机构,带参数", notes = "查询当前企业和子企业和子企业机构，带参数")
    @PostMapping("getEtpTrees1/{etpId}")
    public List<EtpInfoSVo> getCurrentAndParents1(@PathVariable("etpId") Integer etpId) {
        return etpInfoService.makeTrees(etpId);
    }

    @ApiOperation(value = "递归删除企业", notes = "递归删除企业")
    @DeleteMapping("removeByid/{id}")
    public R removeGurrentAndParents(@PathVariable("id") Integer id) {
        OvmsUser user = SecurityUtils.getUser();
        if (id != null && id == user.getEtpId()) {
            return R.failed("顶级企业不允许删除！");
        }
        int b = etpInfoService.removeMakeTree(id);
        if (b > 0) {
            return R.ok("删除成功");
        } else {
            return R.ok("删除失败");
        }
    }


    @ApiOperation(value = "递归禁用企业", notes = "递归禁用企业")
    @GetMapping("disableByid/{id}/{disablestatus}")
    public R disableGurrentAndParents(@PathVariable("id") Integer id, @PathVariable("disablestatus") Integer disablestatus) {
        OvmsUser user = SecurityUtils.getUser();
        if (id != null && id == user.getEtpId()) {
            return R.failed("顶级企业不允许禁用！");
        }
        int b = etpInfoService.updateGetChildrenTree(id, disablestatus);
        if (b > 0) {
            return R.ok("禁用成功");
        } else {
            return R.ok("禁用失败");
        }
    }

    @ApiOperation(value = "企业部门树(带部门车辆)", notes = "企业部门树（带部门车辆）")
    @GetMapping("/etpTreeWithDeptTree/{etpId}")
    public R getEtpTreeWithDeptTreeHsl(@PathVariable("etpId") Integer etpId) {
        List<EtpInfoPrentVO> etpTreeNoDept = etpInfoService.getEtpTreeWithDeptTreeHsl();
        return R.ok(etpTreeNoDept);
    }

    @ApiOperation(value = "企业部门树(无部门车辆)", notes = "企业部门树（无部门车辆）")
    @GetMapping("/etpTreeNoDept/{etpId}")
    public R getEtpTreeNoDeptHsl(@PathVariable("etpId") Integer etpId) {
        List<EtpInfoPrentVO> etpTreeNoDept = etpInfoService.getEtpTreeNoDeptHsl(etpId);
        return R.ok(etpTreeNoDept);
    }

    @ApiOperation(value = "部门树和人员", notes = "部门树和人员")
    @GetMapping("/getDetpTreeAndPeopel")
    public R getDetpTreeAndPeopel() {
        List<EtpInfoPrentVO> rs = etpInfoService.getDetpTreeAndPeopel();
        return R.ok(rs);
    }
}
