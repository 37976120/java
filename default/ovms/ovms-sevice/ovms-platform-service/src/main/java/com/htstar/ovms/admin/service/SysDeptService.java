package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.dto.DeptTree;
import com.htstar.ovms.admin.api.entity.SysDept;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author ovms
 * @since 2018-01-20
 */
public interface SysDeptService extends IService<SysDept> {

	/**
	 * 查询部门树菜单
	 * @return 树
	 */
	List<DeptTree> selectTree();

	/**
	 * 添加信息部门
	 * @param sysDept
	 * @return
	 */
	Boolean saveDept(SysDept sysDept);

	/**
	 * 删除部门
	 * @param id 部门 ID
	 * @return 成功、失败
	 */
	Boolean removeDeptById(Integer id);

	/**
	 * 更新部门
	 * @param sysDept 部门信息
	 * @return 成功、失败
	 */
	Boolean updateDeptById(SysDept sysDept);

	List<DeptTree> selectTreeByEtpId(Integer etpId);

	/**
	 * 企业树
	 * @Author Hanguji
	 * @param etpId
	 * @return
	 */
	public List<DeptTree> selectTreeByEtpIdTree(Integer etpId);
}
