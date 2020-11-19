package com.htstar.ovms.job.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/9/4
 * Company: 航通星空
 * Modified By:
 */
@Mapper
public interface UserApplyJoinExpireMapper {
    void expireUserApplyJoinByDate(@Param("now") LocalDate now);
}
