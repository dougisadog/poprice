package poprice.wechat.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import poprice.wechat.Constants;
import poprice.wechat.domain.base.Item;

import java.io.Serializable;

/**
 * 客户的服务
 */
@Entity
@Table(name = "CUSTOMER_ITEM")
public class CustomerItem extends AbstractAuditingEntity implements Serializable {
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
     * 增送还是购买
     */
    @Enumerated(EnumType.STRING)
    private Constants.ItemType itemType;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;


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

    public Constants.ItemType getItemType() {
        return itemType;
    }

    public void setItemType(Constants.ItemType itemType) {
        this.itemType = itemType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Constants.ActionType getActionType() {
        return actionType;
    }

    public void setActionType(Constants.ActionType actionType) {
        this.actionType = actionType;
    }
}
