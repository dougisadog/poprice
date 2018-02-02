package poprice.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import poprice.wechat.Constants;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 记录谁在什么时间干了什么
 *
 */
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@NotNull
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false, updatable = false)
    @JsonIgnore
    private ZonedDateTime createdDate = ZonedDateTime.now();

    /**
     * 创建时的ip地址
     */
    @Size(max = 15)
    @Column(length = 15, nullable = false, updatable = false)
    private String createdIp = null;

    /**
     * 模块
     */
    @Enumerated(EnumType.STRING)
    private Constants.ModuleType moduleType;

    /**
     * 操作
     */
    @Enumerated(EnumType.STRING)
    private Constants.ActionType actionType;

    /**
     * 具体操作的内容，最大125个汉字
     */
    @Column(columnDefinition = "TEXT")
    private String content = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

    public Constants.ModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(Constants.ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public Constants.ActionType getActionType() {
        return actionType;
    }

    public void setActionType(Constants.ActionType actionType) {
        this.actionType = actionType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
