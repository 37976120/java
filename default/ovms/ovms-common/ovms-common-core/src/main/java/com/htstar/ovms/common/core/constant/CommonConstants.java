package com.htstar.ovms.common.core.constant;

public interface CommonConstants {
	/**
	 * header 中企业ID
	 */
	String ETP_ID = "ETP-ID";

	/**
	 * header 中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * 顶级企业（航通，必须存在）
	 */
	int ETP_ID_1 = 1;

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";
	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单树根节点
	 */
	Integer MENU_TREE_ROOT_ID = -1;

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "ovms-ui";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "ovms";

	/**
	 * 公共参数
	 */
	String OVMS_PUBLIC_PARAM_KEY = "OVMS_PUBLIC_PARAM_KEY";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;
	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * 默认存储bucket
	 */
	String BUCKET_NAME = "ovms";

	/**
	 * 司机角色的role_code
	 */
	String ROLE_DRIVER = "ROLE_DRIVER";

	/**
	 * 司机角色的role_name
	 */
	String ROLE_DRIVER_NAME = "司机";


	/**
	 * 员工角色的role_code
	 */
	String ROLE_STAFF = "ROLE_STAFF";

	/**
	 * 员工角色的role_name
	 */
	String ROLE_STAFF_NAME = "员工";

	/**
	 * 管理员角色的role_code
	 */
	String ROLE_ADMIN = "ROLE_ADMIN";

	/**
	 * 管理员角色的role_name
	 */
	String ROLE_ADMIN_NAME = "管理员";

    /**
     * app首页菜单名字
     */
	String  APP_MENU_NAME="APP首页菜单";

}
