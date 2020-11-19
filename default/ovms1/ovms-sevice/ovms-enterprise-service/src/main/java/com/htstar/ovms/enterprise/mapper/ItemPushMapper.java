package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.vo.ItemPushVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@Mapper
public interface ItemPushMapper extends BaseMapper<T> {

    /**
     * 年检到期提醒
     * @return
     */
    List<ItemPushVO> getMotItemExpirePush();

    /**
     * 保险过期提醒
     * @return
     */
    List<ItemPushVO> getInsItemExpirePush();
}
