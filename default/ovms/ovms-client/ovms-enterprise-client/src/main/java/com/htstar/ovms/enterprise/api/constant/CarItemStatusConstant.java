package com.htstar.ovms.enterprise.api.constant;/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/8
 * Company: 航通星空
 * Modified By:
 */

import org.omg.CORBA.INTERNAL;

import java.util.PrimitiveIterator;

/**
 * @Description:
 * @Author: lw
 * @CreateDate: 2020/7/8 15:18  
 */
public interface CarItemStatusConstant {
    //项目状态 已经
    // 0:待提交
    //1:待审核
    //2:已存档
    //3：退回
   int WAIT_SUBMIT= 0;

   int WAIT_CHECK=1;

   int  ARCHIVED=2;

   int WITHDRAW=3;
}
