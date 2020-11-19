package com.htstar.ovms.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.msg.api.entity.MsgPushUserCid;
import com.htstar.ovms.msg.api.vo.MsgPushUserCidVO;
import com.htstar.ovms.msg.api.vo.MsgUserEtpIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JinZhu
 * @Description: cid和用户绑定Mapper层
 * @date 2020/7/319:29
 */
@Mapper
public interface MsgPushUserCidMapper extends BaseMapper<MsgPushUserCid> {
    /**
     * 获取用户账号号进行推送
     * @param cid
     * @return
     */
    MsgPushUserCidVO getMagPushUserCidByUserId(@Param("cid") String cid,@Param("userId")Integer userId);

    /**
     * 根据用户id修改cid
     * @param cid
     * @return
     */
    int updateMagPushUserCidByUserId(@Param("cid") String cid,@Param("userId")Integer userId);


    /**
     *
     */
    List<MsgUserEtpIdVO> selectByDeptIdUser(@Param("etpId")Integer etpId);

    MsgPushUserCid selectByUserId(@Param("userId") Integer userId);
}
