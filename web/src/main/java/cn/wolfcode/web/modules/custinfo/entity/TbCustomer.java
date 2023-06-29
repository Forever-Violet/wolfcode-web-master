package cn.wolfcode.web.modules.custinfo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-25
 */
public class TbCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 企业名称
     */                    //数据效验
    @Excel(name = "企业名称")
    @NotBlank(message = "请填写企业名称!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100, message = "企业名称不能超过100个字!", groups = {AddGroup.class, UpdateGroup.class})
    private String customerName;

    /**
     * 法定代表人
     */  //@NotBlank用于String
    @Excel(name = "法定代表人")
    @NotBlank(message = "请填写法定代表人!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30, message = "法定代表人不能超过30个字捏!", groups = {AddGroup.class, UpdateGroup.class})
    private String legalLeader;

    /**
     * 成立时间
     */
    @Excel(name = "成立日期")
    @NotNull(message = "请选择成立日期!", groups = {AddGroup.class, UpdateGroup.class})
    private LocalDate registerDate;

    /**
     * 经营状态, 0 开业、1 注销、2 破产
     */   // @NotNull用于Integer、Bigdecimal.LocalDateTime、LocalDate、实体类
    @Excel(name = "经营状态", replace = {"开业_0", "注销_1", "破产_2"})
    @NotNull(message = "请选择经营状态!", groups = {AddGroup.class, UpdateGroup.class})
    private Integer openStatus; //默认开业

    /**
     * 所属地区省份
     */
    @NotBlank(message = "请选择所属省份!", groups = {AddGroup.class, UpdateGroup.class})
    private String province;  //默认中国

    /**
     * 所属地区省份的名字
     */
    @TableField(exist = false)
    private String provinceName;

    /**
     * 注册资本,(万元)
     */
    @NotBlank(message = "请填写注册资本!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(message = "注册资本不能超过20个字", groups = {AddGroup.class, UpdateGroup.class})
    private String regCapital;

    /**
     * 所属行业
     */
    @NotBlank(message = "请填写所属行业!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(message = "所属行业不能超过30个字", groups = {AddGroup.class, UpdateGroup.class})
    private String industry;

    /**
     * 经营范围
     */
    @Length(message = "经营范围不能超过500个字", groups = {AddGroup.class, UpdateGroup.class})
    private String scope;

    /**
     * 注册地址
     */
    @Length(message = "注册地址不能超过500个字", groups = {AddGroup.class, UpdateGroup.class})
    private String regAddr;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 录入人id
     */
    private String inputUserId;

    /**
     * 录入人账号
     */
    @TableField(exist = false) //表示表中不存在该字段
    private String inputUserName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getLegalLeader() {
        return legalLeader;
    }

    public void setLegalLeader(String legalLeader) {
        this.legalLeader = legalLeader;
    }
    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }
    public Integer getOpenStatus() {
        return openStatus;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "TbCustomer{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", legalLeader='" + legalLeader + '\'' +
                ", registerDate=" + registerDate +
                ", openStatus=" + openStatus +
                ", province='" + province + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", regCapital='" + regCapital + '\'' +
                ", industry='" + industry + '\'' +
                ", scope='" + scope + '\'' +
                ", regAddr='" + regAddr + '\'' +
                ", inputTime=" + inputTime +
                ", updateTime=" + updateTime +
                ", inputUserId='" + inputUserId + '\'' +
                ", inputUserName='" + inputUserName + '\'' +
                '}';
    }

    public String getInputUserName() {
        return inputUserName;
    }

    public void setInputUserName(String inputUserName) {
        this.inputUserName = inputUserName;
    }

    public void setOpenStatus(Integer openStatus) {
        this.openStatus = openStatus;
    }
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public String getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(String regCapital) {
        this.regCapital = regCapital;
    }
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }
    public LocalDateTime getInputTime() {
        return inputTime;
    }

    public void setInputTime(LocalDateTime inputTime) {
        this.inputTime = inputTime;
    }
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

}
