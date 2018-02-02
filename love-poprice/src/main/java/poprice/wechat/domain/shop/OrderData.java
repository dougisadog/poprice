package poprice.wechat.domain.shop;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import poprice.wechat.domain.AbstractAuditingEntity;


@Entity
@Table(name = "ORDOER_DATA")
public class OrderData extends AbstractAuditingEntity implements Serializable {

	public static final int PAYTYPE_CREDITS = 1;
	public static final int PAYTYPE_CASH = 0;
	public static final int PAYTYPE_BOTH = -1;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 订单编号 用于各种支付
     */
    @Column(name = "orderNo", nullable = true)
	private String orderNo;
    
    /**
     * 备注
     */
    @Column(name = "tag", nullable = true)
	private String tag;
    
    /**
     * 可支付类型
     */
    @Column(name = "payType", nullable = true)
	private int payType; //
	
    @Transient
	private List<String> addresses;

    /**
     * 总价
     */
    @Column(name = "cost", nullable = true)
	private double cost;

    /**
     * 快递编号
     */
    @Column(name = "expressNo", nullable = true)
	private String expressNo;

    /**
     * 快递人
     */
    @Column(name = "expressor", nullable = true)
	private String expressor;

    @Transient
    private List<ShopCart> shopCarts;
	
    public List<ShopCart> getShopCarts() {
		return shopCarts;
	}
	public void setShopCarts(List<ShopCart> shopCarts) {
		this.shopCarts = shopCarts;
	}
	/**
     * 过期时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "overTime", nullable = true)
	private Date overTime;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public List<String> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getExpressor() {
		return expressor;
	}
	public void setExpressor(String expressor) {
		this.expressor = expressor;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
}
