package cn.wolfcode.web.modules.linkmanVisitInfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.UpdateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 拜访信息表
 * </p>
 *
 * @author 吴彦祖
 * @since 2023-06-27
 */
public class TbVisit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    private String id;

    /**
     * 客户id
     */
    @NotBlank(message = "客户id不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private String custId;

    /**
     * 企业名称
     */
    @TableField(exist = false) //标识表中不存在该字段
    private String custName;

    /**
     * 联系人id
     */
    @NotBlank(message = "联系人不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private String linkmanId;

    /**
     * 联系人名称
     */
    @TableField(exist = false) //标识表中不存在该字段
    private String linkmanName;

    /**
     * 拜访方式, 1 上门走访, 2 电话拜访
     */
    @NotNull(message = "拜访方式不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    private Integer visitType;

    /**
     * 拜访原因
     */
    @NotBlank(message = "拜访原因不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100, message = "拜访原因不能超过100字", groups = {AddGroup.class, UpdateGroup.class})
    private String visitReason;

    /**
     * 交流内容
     */
    @NotBlank(message = "交流内容不能为空!", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100, message = "交流内容不能超过1000字", groups = {AddGroup.class, UpdateGroup.class})
    private String content;

    /**
     * 拜访时间
     */   //过去或今日
    @PastOrPresent(message = "日期不能超过今日!", groups = {AddGroup.class, UpdateGroup.class}) //校验日期是否在当前时间之前, 即日期不能超过当前日期
    private LocalDate visitDate;

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
    public String getLinkmanId() {
        return linkmanId;
    }

    public void setLinkmanId(String linkmanId) {
        this.linkmanId = linkmanId;
    }
    public Integer getVisitType() {
        return visitType;
    }

    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }
    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }
    public String getInputUser() {
        return inputUser;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
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

    @Override
    public String toString() {
        return "TbVisit{" +
                "id='" + id + '\'' +
                ", custId='" + custId + '\'' +
                ", custName='" + custName + '\'' +
                ", linkmanId='" + linkmanId + '\'' +
                ", linkmanName='" + linkmanName + '\'' +
                ", visitType=" + visitType +
                ", visitReason='" + visitReason + '\'' +
                ", content='" + content + '\'' +
                ", visitDate=" + visitDate +
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
}
