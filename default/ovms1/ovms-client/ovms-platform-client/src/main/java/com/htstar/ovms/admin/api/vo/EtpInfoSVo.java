package com.htstar.ovms.admin.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.htstar.ovms.admin.api.dto.DeptTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 企业表
 *
 * @author flr
 * @date 2020-08-04 09:56:08
 */
@Data
public class EtpInfoSVo extends Model<EtpInfoSVo> {
private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    @TableId
    @ApiModelProperty(value="企业ID")
    private Integer id;
    /**
     * 企业名称
     */
    @ApiModelProperty(value="企业名称")
    private String etpName;

    /**
     * 企业名称
     */
    @ApiModelProperty(value="父级企业名称")
    private Integer parentId;

   private List<EtpInfoSVo> children;
    @ApiModelProperty(value="部门树")
    private List<DeptTree> deptTreeList;

    @ApiModelProperty(value="父级企业名称")
    private List<Integer> ids;
}
