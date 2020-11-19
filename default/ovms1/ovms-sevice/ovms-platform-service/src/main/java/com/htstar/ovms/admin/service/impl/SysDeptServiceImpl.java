package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.dto.DeptTree;
import com.htstar.ovms.admin.api.entity.SysDept;
import com.htstar.ovms.admin.api.entity.SysDeptRelation;
import com.htstar.ovms.admin.api.vo.TreeUtil;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.admin.mapper.EtpInfoMapper;
import com.htstar.ovms.admin.mapper.SysDeptMapper;
import com.htstar.ovms.admin.service.SysDeptRelationService;
import com.htstar.ovms.admin.service.SysDeptService;
import com.htstar.ovms.common.data.datascope.DataScope;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author ovms
 * @since 2018-01-20
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptRelationService sysDeptRelationService;

    private final SysDeptMapper deptMapper;

    private final EtpInfoMapper etpInfoMapper;

    /**
     * 添加信息部门
     *
     * @param dept 部门
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(SysDept dept) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        this.save(sysDept);
        sysDeptRelationService.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Integer id) {
        // 级联删除部门
        List<Integer> idList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getAncestor, id)).stream()
                .map(SysDeptRelation::getDescendant).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }

        // 删除部门级联关系
        sysDeptRelationService.deleteAllDeptRealtion(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptById(SysDept sysDept) {
        // 更新部门状态
        this.updateById(sysDept);
        // 更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getDeptId());
        sysDeptRelationService.updateDeptRealtion(relation);
        return Boolean.TRUE;
    }

    @Override
    public List<DeptTree> selectTreeByEtpId(Integer etpId) {
        return deptMapper.selectTreeByEtpId(etpId);
    }

    /**
     * 查询全部部门树
     *
     * @return 树
     */
    @Override
    public List<DeptTree> selectTree() {
        // 查询全部部门
        List<SysDept> deptAllList = deptMapper.selectList(Wrappers.emptyWrapper());
        // 查询数据权限内部门
        List<Integer> deptOwnIdList = deptMapper.selectListByScope(Wrappers.emptyWrapper(), new DataScope()).stream()
                .map(SysDept::getDeptId).collect(Collectors.toList());

        // 权限内部门
        List<DeptTree> collect = deptAllList.stream().filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
                .sorted(Comparator.comparingInt(SysDept::getSort)).map(dept -> {
                    DeptTree node = new DeptTree();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());

                    // 有权限不返回标识
                    if (deptOwnIdList.contains(dept.getDeptId())) {
                        node.setIsLock(Boolean.FALSE);
                    }
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(collect, 0);
    }

    @Override
    public List<DeptTree> selectTreeByEtpIdTree(Integer etpId) {
        return getDeptTree(baseMapper.listDeptsByEtpId(etpId));
    }


    /**
     * 构建部门树
     *
     * @param depts 部门
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts) {
        List<DeptTree> treeList = depts.stream()
                .filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
                .sorted(Comparator.comparingInt(SysDept::getSort))
                .map(dept -> {
                    DeptTree node = new DeptTree();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName() + "（部门）");
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, 0);
    }

    //_________my________________
    @Override
    public List<DeptTree> myselectTreeByEtpIdTree(Integer id) {
        return mygetDeptTree(baseMapper.listDeptsByEtpId(id));
    }

    private List<DeptTree> mygetDeptTree(List<SysDept> depts) {
        List<DeptTree> treeList = depts.stream()
                .filter(dept -> !dept.getDeptId().equals(dept.getParentId()))
                .sorted(Comparator.comparingInt(SysDept::getSort))
                .map(dept -> {
                    List<UserVO> userVOS = etpInfoMapper.getSysUserByEtpId(dept.getDeptId());
                    List<UserVO> staff = userVOS.stream().filter(userVO -> userVO.getDeptId() == dept.getDeptId()).collect(Collectors.toList());
                    DeptTree node = new DeptTree();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName() + "（部门）");
                    node.setStaff(staff);
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.build(treeList, 0);
    }
    //_________my________________
}
