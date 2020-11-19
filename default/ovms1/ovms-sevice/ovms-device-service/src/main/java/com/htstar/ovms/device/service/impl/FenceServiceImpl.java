package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.dto.DeviceTripDTO;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.Fence;
import com.htstar.ovms.device.api.vo.FencePageVO;
import com.htstar.ovms.device.api.vo.FenceVO;
import com.htstar.ovms.device.mapper.FenceMapper;
import com.htstar.ovms.device.service.FenceService;
import com.htstar.ovms.device.service.GpsService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 围栏
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Service
public class FenceServiceImpl extends ServiceImpl<FenceMapper, Fence> implements FenceService {

    @Autowired
    private GpsService gpsService;

    @Override
    public List<FenceVO> queryFenceList(String deviceSn) {
        return this.baseMapper.queryFenceList(deviceSn);
    }

    @Override
    public R saveFence(Fence fence) {
        if (!checkName(fence)) {
            return R.failed("围栏名称已存在");
        }
        OvmsUser user = SecurityUtils.getUser();
        fence.setCreateUser(user.getId());
        fence.setEtpId(user.getEtpId());
        return R.ok(baseMapper.insert(fence));
    }

    @Override
    public R updateFence(Fence fence) {
        if (!checkName(fence)) {
            return R.failed("围栏名称已存在");
        }
        return R.ok(baseMapper.updateById(fence));
    }

    /**
     * 检查名称是否重复
     *
     * @param fence
     * @return
     */
    private boolean checkName(Fence fence) {
        OvmsUser user = SecurityUtils.getUser();
        if(user == null){
            return false;
        }
        Integer count = baseMapper.selectCount(new QueryWrapper<Fence>().eq("etp_id", user.getEtpId())
                .eq("fence_name", fence.getFenceName()));
        if (fence.getId() != null) {
            count = baseMapper.selectCount(new QueryWrapper<Fence>().eq("etp_id", user.getEtpId())
                    .eq("fence_name", fence.getFenceName()).notIn("id", fence.getId()));
        }
        if (count != 0) {
            return false;
        }
        fence.setCreateUser(user.getId());
        return true;
    }

    @Override
    public Page<FencePageVO> getFencePage(FenceDTO fenceDTO) {
        getEtpId(fenceDTO);
        Page<FencePageVO> fencePage = baseMapper.getFencePage(fenceDTO);
        for (FencePageVO record : fencePage.getRecords()) {
            if (record.getLat() != null && record.getLng() != null) {
                record.setCenterAddr(gpsService.getGpsAddr(record.getLat(), record.getLng()));
            }
        }
        return fencePage;
    }


    @Override
    public Page<FencePageVO> getNotAddFenceByCarInfoPage(FenceDTO fenceDTO) {
        getEtpId(fenceDTO);
        Page<FencePageVO> fencePage = baseMapper.getNotAddFenceByCarInfoPage(fenceDTO);
        for (FencePageVO record : fencePage.getRecords()) {
            if (record.getLat() != null && record.getLng() != null) {
                record.setCenterAddr(gpsService.getGpsAddr(record.getLat(), record.getLng()));
            }
        }
        return fencePage;
    }

    @Override
    public R removeFence(Integer id, boolean exit) {
        if (exit) {
            return R.failed("此围栏存在绑定关系，请先解除绑定");
        }
        return R.ok(baseMapper.deleteById(id));
    }

    /**
     * 获取企业id
     * @param deviceDTO
     */
    private void getEtpId(FenceDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }

}
