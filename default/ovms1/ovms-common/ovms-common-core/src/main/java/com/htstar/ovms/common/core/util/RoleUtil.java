package com.htstar.ovms.common.core.util;

import com.htstar.ovms.common.core.constant.CommonConstants;
import lombok.experimental.UtilityClass;

/**
 * Description: 角色工具
 * Author: flr
 * Date: Created in 2020/7/13
 * Company: 航通星空
 * Modified By:
 */
@UtilityClass
public class RoleUtil {
    public boolean judeDefaltRole(String roleCode){
        String code = roleCode.trim();
        if (code.equals(CommonConstants.ROLE_DRIVER)||
            code.equals(CommonConstants.ROLE_ADMIN)||
            code.equals(CommonConstants.ROLE_STAFF)){
            return true;
        }else {
            return false;
        }
    }

}
