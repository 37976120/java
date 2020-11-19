package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.EtpInfo;
import com.htstar.ovms.admin.api.req.AppCreateEtpReq;
import com.htstar.ovms.admin.api.req.DistributeEtpAdminReq;
import com.htstar.ovms.admin.api.req.RegisterEtpReq;
import com.htstar.ovms.admin.api.vo.EtpInfoVO;
import com.htstar.ovms.common.core.util.R;

import java.util.List;

/**
 * 企业表
 *
 * @author htxk
 * @date 2020-06-05 09:58:53
 */
public interface EtpInfoService extends IService<EtpInfo> {

    /**
     * 获取正常的租户
     *
     * @return
     */
    List<EtpInfo> getNormalTenant();

    /**
     * 保存租户
     *
     * @param etpInfo
     * @return
     */
    R saveEtpInfo(EtpInfo etpInfo);

    R distributeEtpAdmin(DistributeEtpAdminReq req);

    R queryPage(Page page, EtpInfo sysTenant);

    /**
     * 通过企业编码获取企业信息
     * @param etpNo
     * @return
     */
    R<EtpInfoVO> getEtpInfoByCode(String etpNo);


    /**
     * 获取企业数与车辆信息
     * @return
     */
    List<EtpInfoVO> getEtpMonitoringTree();

    /**
     * Author: flr
     * Date: 2020/7/13 10:46
     * Company: 航通星空
     * Modified By:
     */
    R updateEtp(EtpInfo etp);

    R registerEtp(RegisterEtpReq req);
}
