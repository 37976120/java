package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.EtpAdvert;
import com.htstar.ovms.enterprise.api.req.NoticeReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业广告
 *
 * @author lw
 * @date 2020-08-10 17:29:10
 */
@Mapper
public interface EtpAdvertMapper extends BaseMapper<EtpAdvert> {

    /**
     * 删除广告
     * @param id
     * @param clickCount
     */
    void delEtpAdvert(@Param("id") Integer id, @Param("clickCount") Integer clickCount);

    /**
     * 查询分页
     * @param req
     * @return
     */
    @SqlParser(filter = true)
    IPage<EtpAdvert> queryPage(@Param("req") NoticeReq req);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    EtpAdvert getEtpAdvertById(Integer id);

    List<String> getList();

    /**
     * 更新点击次数
     * @param id
     * @param clickCount
     */
    void updateClickCount(@Param("id") Integer id, @Param("click") int clickCount);
}
