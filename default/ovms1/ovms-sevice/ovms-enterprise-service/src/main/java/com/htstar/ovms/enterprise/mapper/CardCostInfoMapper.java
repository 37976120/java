package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CardCostInfoReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 卡流水
 *
 * @author lw
 * @date 2020-07-22 14:25:29
 */
@Mapper
public interface CardCostInfoMapper extends BaseMapper<CardCostInfo> {

    /**
     * 明细
     * @param req
     * @return
     */
    IPage<CardCostInfo> queryPage(@Param("req") CardCostInfoReq req);
}
