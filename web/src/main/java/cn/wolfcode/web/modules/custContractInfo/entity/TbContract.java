package cn.wolfcode.web.modules.custContractInfo.entity;

import cn.wolfcode.web.modules.custContractInfo.validateUtils.validateAnnotation.ValidNumber;
import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 合同信息
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-28
 */
@Data
public class TbContract implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 客户id
     */
    @NotBlank(message = "企业名称不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private String custId;

    /**
     * 企业用户名称
     */
    @TableField(exist = false) //标识表中不存在该字段
    private String custName;

    /**
     * 合同名称
     */
    @NotBlank(message = "合同名称不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100, message = "合同名称不能超过100字!", groups = {AddGroup.class, UpdateGroup.class})
    private String contractName;

    /**
     * 合同编码
     */
    @NotBlank(message = "合同编码不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50, message = "合同编码不能超过50字!", groups = {AddGroup.class, UpdateGroup.class})
    private String contractCode;

    /**
     * 合同金额
     */
    @NotBlank(message = "合同金额不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @DecimalMin(value = "0.01", message = "合同金额必须为正数!", groups = {AddGroup.class, UpdateGroup.class})
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "合同金额不能超过两位小数!", groups = {AddGroup.class, UpdateGroup.class})
    private String amounts;

    /**
     * 合同生效开始时间
     */
    @NotNull(message = "合同开始时间不能为空!",groups = {AddGroup.class, UpdateGroup.class})
    private LocalDate startDate;

    /**
     * 合同生效结束时间
     */
    @NotNull(message = "合同终止时间不能为空!",groups = {AddGroup.class, UpdateGroup.class})
    private LocalDate endDate;

    /**
     * 合同内容
     */
    @NotBlank(message = "合同内容不能为空!",groups = {AddGroup.class, UpdateGroup.class})
    private String content;

    /**
     * 是否盖章确认 0 否 1 是
     */
    //@NotNull(message = "请选择是否盖章!",groups = {AddGroup.class, UpdateGroup.class})
    private Integer affixSealStatus;

    /**
     * 审核状态 0 未审核 1 审核通过 -1 审核不通过
     */
    //@NotNull(message = "审核状态不能为空!",groups = {AddGroup.class, UpdateGroup.class})
    private Integer auditStatus;

    /**
     * 是否作废 1 作废 0 在用
     */
    //@NotNull(message = "请选择是否作废!",groups = {AddGroup.class, UpdateGroup.class})
    private Integer nullifyStatus;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    @Override
    public String toString() {
        return "TbContract{" +
            "id=" + id +
            ", custId=" + custId +
            ", contractName=" + contractName +
            ", contractCode=" + contractCode +
            ", amounts=" + amounts +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", content=" + content +
            ", affixSealStatus=" + affixSealStatus +
            ", auditStatus=" + auditStatus +
            ", nullifyStatus=" + nullifyStatus +
            ", inputUser=" + inputUser +
            ", inputTime=" + inputTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
