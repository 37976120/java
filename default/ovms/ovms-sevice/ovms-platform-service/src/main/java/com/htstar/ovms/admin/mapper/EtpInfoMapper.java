package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.EtpInfo;
import com.htstar.ovms.admin.api.vo.EtpPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业表
 *
 * @author htxk
 * @date 2020-06-05 09:58:53
 */
@Mapper
public interface EtpInfoMapper extends BaseMapper<EtpInfo> {

    /**
     * Description: 按企业名称查看企业是否存在
     * Author: flr
     */
    long exsitThisByName(EtpInfo etpInfo);


    /**
     * Description: 修改企业代码
     * Author: flr
     */
    void updateEtpNo(@Param("etpId") Integer etpId, @Param("etpNo")String etpNo);


    /**
     * Description: 修改分配管理员
     * Author: flr
     */
    void updateDistributeStatus(@Param("etpId")Integer etpId, @Param("distributeStatus")int distributeStatus);

    IPage<EtpPageVO> queryPage(@Param("page") Page page, @Param("etpInfo") EtpInfo etpInfo);

    /**
     * 获取正常的企业信息
     * @param etpNo
     * @return
     */
    EtpInfo getNormalEtp(@Param("etpNo") String etpNo, @Param("nowCst")LocalDateTime nowCst);

    List<EtpInfo> queryNormalTenant(@Param("nowCst")LocalDateTime nowCst);
}
