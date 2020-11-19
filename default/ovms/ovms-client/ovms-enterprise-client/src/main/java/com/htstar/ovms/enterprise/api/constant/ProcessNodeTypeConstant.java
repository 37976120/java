package com.htstar.ovms.enterprise.api.constant;

/**
 * Description: 流程节点类型 对应表 `apply_verify_node`.node_type
 * Author: flr
 * Date: Created in 2020/7/2
 * Company: 航通星空
 * Modified By:
 */
public interface ProcessNodeTypeConstant {

    //每个节点预留9个位置

    //节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；

    /**
     * 10=申请
     */
    int APPLY = 10;

    /**
     * 20=审批
     */
    int VERIFY = 20;

    /**
     * 30=公车（交车）,私车（分配司机）
     */
    int GIVE_CAR = 30;

    /**
     * 31=开始用车（私）
     */
    int START_USE = 31;

    /**
     * 40=提车
     */
    int GET_CAR = 40;

    /**
     * 50=还车
     */
    int RETURN_CAR = 50;

    /**
     * 51=结束用车（私）
     */
    int END_USE = 51;

    /**
     * 60=完成
     */
    int FINISH = 60;
}
