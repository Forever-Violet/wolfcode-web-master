package cn.wolfcode.web.modules.linkmanInfo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import org.aspectj.bridge.IMessage;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 客户联系人
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-25
 */
public class TbCustLinkman implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 客户id
     */
    @NotBlank(message = "所属客户id不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private String custId;

    /**
     * 企业名称
     */
    @TableField(exist = false) //标识表中不存在该字段
    @Excel(name = "所属企业")
    private String custName;

    /**
     * 联系人名字
     */
    @NotBlank(message = "联系人名称不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30, message = "联系人名称不能超过30字", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "联系人姓名")
    private String linkman;


    /**
     * 性别 1 男 0 女
     */
    @NotNull(message = "性别不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "性别", replace = {"男_1", "女_0"})  // 值替换
    private Integer sex;

    /**
     * 年龄  自然数: 0-150岁
     */                          // fraction = 0 不支持小数
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "年龄必须为纯数字!", groups = {AddGroup.class, UpdateGroup.class})
    @Range(min = 0, max = 100, message = "年龄不能超出范围(0~100)!", groups = {AddGroup.class, UpdateGroup.class})
    private Integer age;

    /**
     * 联系人电话
     */
    @NotBlank(message = "手机号码不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 20, message = "手机号码长度不能超过20字", groups = {AddGroup.class, UpdateGroup.class})
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "手机号码必须为纯数字!", groups = {AddGroup.class, UpdateGroup.class})
    @Excel(name = "电话")
    private String phone;
    /**
     * 职位
     */
    @Length(max = 20, message = "职位不能超过20字", groups = {AddGroup.class, UpdateGroup.class})
    private String position;

    /**
     * 部门
     */
    @Length(max = 20, message = "部门不能超过20字", groups = {AddGroup.class, UpdateGroup.class})
    private String department;

    /**
     * 任职状态 1在职 0离职  默认在职
     */
    @NotNull(message = "任职状态不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer jobStatus;

    /**
     * 备注信息
     */
    @Length(max = 100, message = "备注信息不能超过100字", groups = {AddGroup.class, UpdateGroup.class})
    private String remark;

    /**
     * 录入人
     */
    private String inputUser;

    /**
     * 录入时间
     */
    private LocalDateTime inputTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }
    public Integer getSex() {
        return sex;
    }

    @Override
    public String toString() {
        return "TbCustLinkman{" +
                "id='" + id + '\'' +
                ", custId='" + custId + '\'' +
                ", custName='" + custName + '\'' +
                ", linkman='" + linkman + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", jobStatus=" + jobStatus +
                ", remark='" + remark + '\'' +
                ", inputUser='" + inputUser + '\'' +
                ", inputTime=" + inputTime +
                '}';
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public String getPhone() {
        return phone;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }
    public LocalDateTime getInputTime() {
        return inputTime;
    }

    public void setInputTime(LocalDateTime inputTime) {
        this.inputTime = inputTime;
    }

}
