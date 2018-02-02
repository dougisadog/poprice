package poprice.wechat.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import poprice.wechat.Constants;

import java.io.Serializable;

/**
 * 客户储蓄次数记录
 */
@Entity
@Table(name = "CUSTOMER_ITEM_RECORD")
public class CustomerItemRecord extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 具体客户
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * 充值  还是 消费
     */
    @Enumerated(EnumType.STRING)
    private Constants.ActionType actionType;

    /**
     * 记录 如 给某客户充值 xx服务n次 赠送xx服务m次
     */
    private String record;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Constants.ActionType getActionType() {
        return actionType;
    }

    public void setActionType(Constants.ActionType actionType) {
        this.actionType = actionType;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

