package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.SysUserRoleFeign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleDTO;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleNoPageDTO;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverPageVO;
import com.htstar.ovms.enterprise.api.vo.DriverVO;
import com.htstar.ovms.enterprise.mapper.CarDriverInfoMapper;
import com.htstar.ovms.enterprise.service.CarDriverInfoService;
import com.htstar.ovms.enterprise.service.CarInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 司机
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Service
@Slf4j
public class CarDriverInfoServiceImpl extends ServiceImpl<CarDriverInfoMapper, CarDriverInfo> implements CarDriverInfoService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private SysUserRoleFeign sysUserRoleFeign;

    /**
     * 新增
     *
     * @param carDriverInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveInfo(CarDriverInfo carDriverInfo) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        Integer userId = carDriverInfo.getUserId();
        if (userId==null){
            return R.failed("请选择用户");
        }
        Integer count = baseMapper.selectCount(new QueryWrapper<CarDriverInfo>()
                .eq("user_id", userId)
                .eq("etp_id", etpId)
                .eq("del_flag", 0));
        if (count > 0) {
            return R.failed("该用户已经是司机，添加失败");
        }
        carDriverInfo.setEtpId(etpId);
        baseMapper.insert(carDriverInfo);
        sysUserRoleFeign.saveUserDriver(carDriverInfo.getUserId(), etpId, SecurityConstants.FROM_IN);
        return R.ok("添加成功");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R removeByIds(String ids) {
        Integer etpId = TenantContextHolder.getEtpId();
        if (StrUtil.isBlank(ids)) {
            return R.failed("请选择要删除的记录");
        }
        String[] split = ids.replace("\"", "").split(",");
        for (String s : split) {
            Integer id = Integer.valueOf(s);
            CarDriverInfo carDriverInfo = baseMapper.selectOne(new QueryWrapper<CarDriverInfo>().eq("id", id));
            carDriverInfo.setDelFlag(1);
            baseMapper.updateById(carDriverInfo);
            sysUserRoleFeign.removeUserDriver(carDriverInfo.getUserId(), etpId, SecurityConstants.FROM_IN);
        }
        return R.ok("删除成功");
    }

    /**
     * 修改
     *
     * @param carDriverInfo
     * @return
     */
    @Override
    public R updateDriverById(CarDriverInfo carDriverInfo) {
        log.info("修改{}",carDriverInfo );
        Integer etpId = SecurityUtils.getUser().getEtpId();
        CarDriverInfo oldDriver = baseMapper.selectOne(new QueryWrapper<CarDriverInfo>()
                .eq("id", carDriverInfo.getId())
                .eq("del_flag", 0));
        //新旧司机用户不同
        if (!oldDriver.getUserId().equals(carDriverInfo.getUserId())) {
            //新司机是否绑定
            Integer count = baseMapper.selectCount(new QueryWrapper<CarDriverInfo>()
                    .eq("user_id", carDriverInfo.getUserId())
                    .eq("del_flag", 0));
            if (count > 0) {
                return R.failed("该司机已经绑定,无法再次绑定");
            }
            //未绑定的新司机
            else {
                sysUserRoleFeign.removeUserDriver(carDriverInfo.getUserId(), etpId, SecurityConstants.FROM_IN);
                baseMapper.updateById(carDriverInfo);
                sysUserRoleFeign.saveUserDriver(carDriverInfo.getUserId(), etpId, SecurityConstants.FROM_IN);

            }
        } else {
            baseMapper.updateById(carDriverInfo);
        }
        return R.ok("修改成功");
    }

    @Override
    public Boolean saveDriverByUserId(Integer userId, Integer etpId) {
        CarDriverInfo carDriverInfo = new CarDriverInfo();
        carDriverInfo.setEtpId(etpId);
        carDriverInfo.setUserId(userId);
        return this.save(carDriverInfo);

    }

    /**
     * 删除用户时 删除司机
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delDriverByUserId(Integer userId) {
        CarDriverInfo carDriverInfo = baseMapper.selectOne(new QueryWrapper<CarDriverInfo>().eq("user_id", userId)
                .eq("del_flag",0 ));
        if (carDriverInfo!=null){
            carDriverInfo.setDelFlag(1);
            baseMapper.updateById(carDriverInfo);
            carInfoService.untieCarByDriverId(carDriverInfo.getId());
        }
        return true;
    }

    /**
     * 分页
     *
     * @param carFileManageReq
     * @return
     */
    @Override
    public IPage<CarDriverPageVO> queryPage(CarFileManageReq carFileManageReq) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        carFileManageReq.setEtpId(etpId);
        IPage<CarDriverPageVO> page = baseMapper.queryPage(carFileManageReq);
        List<CarDriverPageVO> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)){
            //正在使用的司机
            List<Integer> driverUserIds = baseMapper.getUsingDriverUserIds(etpId);
            if (CollUtil.isNotEmpty(driverUserIds)){
                List<CarDriverPageVO> carDriverPageVOS = new ArrayList<>();
                for (CarDriverPageVO driverPageVO : records) {
                    if (driverUserIds.contains(driverPageVO.getUserId())){
                        driverPageVO.setOrderStatus(0);
                    }
                    carDriverPageVOS.add(driverPageVO);
                }
                page.setRecords(carDriverPageVOS);
            }
        }
        return page;
    }

    /**
     * 导出
     *
     * @param req
     */
    @Override
    public void exportExcel(CarFileManageReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        req.setEtpId(etpId);
        req.setSize(Long.MAX_VALUE);
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //需要导出的数据
        List<CarDriverPageVO> carDriverPageVos = baseMapper.exportExcel(req);
        if (carDriverPageVos.size() > 0) {
            for (CarDriverPageVO carDriverPageVo : carDriverPageVos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("姓名", carDriverPageVo.getUsername());
                map.put("手机号", carDriverPageVo.getPhone());
                String sex=carDriverPageVo.getSex()==0?"女":"男";
                map.put("性别",sex );
                //驾照类型
                String licenseType = "";
                /*驾照类型 0:A1 1:A2 2:A3 3:B1 4:B2 5:c1*/
                Integer type = carDriverPageVo.getLicenseType()==null?7:carDriverPageVo.getLicenseType();
                if (type == 0) {
                    licenseType = "A1";
                } else if (type == 1) {
                    licenseType = "A2";
                } else if (type == 2) {
                    licenseType = "A3";
                } else if (type == 3) {
                    licenseType = "B1";
                } else if (type == 4) {
                    licenseType = "B2";
                } else if (type == 5) {
                    licenseType = "c1";
                }
                map.put("驾照类型", licenseType);
                map.put("拿证日期", carDriverPageVo.getGetLicenseTime());
                String driverStatus = "";
                Integer status = carDriverPageVo.getDriverStatus();
                if (status == 0) {
                    driverStatus = "正常";
                } else if (status == 1) {
                    driverStatus = "请假";
                }
                map.put("司机状态", driverStatus);
                rows.add(map);
            }
        }
        carInfoService.carExportUtil(rows, "司机信息");
    }

    /**
     * Description: 获取企业所有的司机
     * Author: flr
     * Date: 2020/7/4 14:24
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public List<Integer> queryAllDriver(Integer etpId) {
        return baseMapper.getIdList(etpId);
    }

    /**
     * Description: 获取司机VO
     * Author: flr
     * Date: 2020/7/7 17:10
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public DriverVO getDriverVO(Integer driveUserId) {
        return baseMapper.getDriverVO(driveUserId);
    }

    @Override
    public Page<ApplyCarOrderAndDriverVO> selectDriverInfoPage(CarDriverScheduleDTO carDriverScheduleDTO) {
        return baseMapper.selectDriverInfoPage(carDriverScheduleDTO);
    }

    @Override
    public Page<ApplyCarOrderAndDriverVO> selectNoDriverInfoPage(CarDriverScheduleDTO carDriverScheduleDTO) {
        return baseMapper.selectNoDriverInfoPage(carDriverScheduleDTO);
    }



    @Override
    public Integer selectNoDriverInfoPageTotal(Integer etpId,String licCodeOrriverName, Integer scheduleStatus) {
        return baseMapper.selectNoDriverInfoPageTotal(etpId,licCodeOrriverName,scheduleStatus);
    }
    @Override
    public String getNickName(Integer userId) {
        return baseMapper.getNickName(userId);
    }

    /**
     * 不可排班司机,不分页
     * JinZhu
     * @param carDriverScheduleNoPageDTO
     * @return
     */
    @Override
    public List<ApplyCarOrderAndDriverVO> selectNoDriverInfoList(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO) {
        return baseMapper.selectNoDriverInfoList(carDriverScheduleNoPageDTO);
    }
}
