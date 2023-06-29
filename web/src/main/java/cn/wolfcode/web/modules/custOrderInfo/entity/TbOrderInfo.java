package cn.wolfcode.web.modules.custOrderInfo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

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
    @Excel(name = "产品名称")
    private String prodName;

    /**
     * 产品数量
     */
    @Excel(name = "产品数量")
    private Integer amounts;

    /**
     * 产品价格
     */
    @Excel(name = "产品单价")
    private String price;

    /**
     * 状态 0 未发货 1 已发货 2 已收货
     */
    private Integer status;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货人电话
     */
    private String linkPhone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 物流
     */
    private String logistcs;

    /**
     * 物流单号
     */
    private String logisticsCode;

    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;

    /**
     * 收货时间
     */
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
