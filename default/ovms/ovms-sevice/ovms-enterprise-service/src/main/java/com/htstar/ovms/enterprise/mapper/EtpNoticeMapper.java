package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.EtpNotice;
import com.htstar.ovms.enterprise.api.req.NoticeReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业公告
 *
 * @author lw
 * @date 2020-08-10 11:38:31
 */
@Mapper
public interface EtpNoticeMapper extends BaseMapper<EtpNotice> {

    /**
     * 删除公告
     * @param id
     */
    void delNotice(@Param("id")Integer id);

    /**
     * 分页
     * @param req
     * @return
     */
    @SqlParser(filter = true)
    IPage<EtpNotice> queryPage(@Param("req") NoticeReq req);
}
