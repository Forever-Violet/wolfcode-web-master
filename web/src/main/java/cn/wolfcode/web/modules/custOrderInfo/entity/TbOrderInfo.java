package cn.wolfcode.web.modules.custOrderInfo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-29
 */
@Data
public class TbOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 所属客户id
     */
    @NotBlank(message = "企业名称不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    private String custId;

    /**
     * 所属企业名称
     */
    @TableField(exist = false) // 标识表中不存在该字段
    @Excel(name = "企业名称")
    private String custName;

    /**
     * 产品名称
     */

    @NotBlank(message = "产品名称不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50, message = "产品名称不能超过50个字！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "产品名称")
    private String prodName;

    /**
     * 产品数量
     */
    @DecimalMin(value = "0.01", message = "产品数量必须为正数!", groups = {AddGroup.class, UpdateGroup.class})
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "产品数量必须为整数!", groups = {AddGroup.class, UpdateGroup.class})
    @NotNull(message = "产品数量不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "产品数量")
    private Integer amounts;

    /**
     * 产品价格
     */
    @DecimalMin(value = "0.01", message = "产品单价必须为正数!", groups = {AddGroup.class, UpdateGroup.class})
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "产品单价不能超过两位小数!", groups = {AddGroup.class, UpdateGroup.class})
    @NotBlank(message = "产品单价不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "产品单价")
    private String price;

    /**
     * 状态 0 未发货 1 已发货 2 已收货
     */
    @Excel(name = "状态", replace = {"未发货_0", "已发货_1", "已收货_2"})
    private Integer status;

    /**
     * 收货人
     */
    @Length(max = 30, message = "收货人不能超过30个字！", groups = {AddGroup.class, UpdateGroup.class})
    @NotBlank(message = "收货人不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "收货人")
    private String receiver;

    /**
     * 收货人电话
     */
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "收货人电话必须为纯数字!", groups = {AddGroup.class, UpdateGroup.class})
    @NotBlank(message = "收货人电话不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 20, message = "收货人电话不能超过20个字！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "收货人电话")
    private String linkPhone;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50, message = "收货地址不能超过50个字！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "收货人地址")
    private String address;

    /**
     * 物流
     */
    //@NotBlank(message = "物流不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 20, message = "物流不能超过20个字！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "物流")
    private String logistcs;

    /**
     * 物流单号
     */
    //@NotBlank(message = "物流单号不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50, message = "物流单号不能超过50个字！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "物流单号")
    private String logisticsCode;

    /**
     * 发货时间
     */
  //  @NotNull(message = "物流单号不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "发货时间")
    private LocalDateTime deliverTime;

    /**
     * 收货时间
     */
  //  @NotNull(message = "物流单号不能为空！", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "收货时间")
    private LocalDateTime receiveTime;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;


    @Override
    public String toString() {
        return "TbOrderInfo{" +
            "id=" + id +
            ", custId=" + custId +
            ", prodName=" + prodName +
            ", amounts=" + amounts +
            ", price=" + price +
            ", status=" + status +
            ", receiver=" + receiver +
            ", linkPhone=" + linkPhone +
            ", address=" + address +
            ", logistcs=" + logistcs +
            ", logisticsCode=" + logisticsCode +
            ", deliverTime=" + deliverTime +
            ", receiveTime=" + receiveTime +
        "}";
    }
}
