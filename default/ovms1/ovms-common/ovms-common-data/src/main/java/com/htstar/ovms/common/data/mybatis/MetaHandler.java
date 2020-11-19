package com.htstar.ovms.common.data.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/12
 * Company: 航通星空
 * Modified By:
 */
public class MetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName("createTime",metaObject);
        if (createTime == null){
            LocalDateTime now = OvmDateUtil.getCstNow();
            setFieldValByName("createTime", now, metaObject);
            Object updateTime = getFieldValByName("updateTime",metaObject);
            if (updateTime == null){
                setFieldValByName("updateTime", now, metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime",metaObject);
        if (updateTime == null){
            LocalDateTime now = OvmDateUtil.getCstNow();
            setFieldValByName("updateTime", now, metaObject);
        }
    }
}
