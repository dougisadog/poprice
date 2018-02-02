package poprice.wechat.domain.shop;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import poprice.wechat.domain.AbstractAuditingEntity;

@Entity
@Table(name = "GOODS")
public class Goods extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	public static final int TYPE_BOTH = -1;
	public static final int PAYTYPE_CREDITS = 1;
	public static final int PAYTYPE_CASH = 0;
	
	public static final Integer OFF = 0;
	public static final Integer UP = 1;
	
	public static final int TYPE_VIRTUAL = 1;
	public static final int TYPE_REAL = 0;
	 
	public static final int GIFT_AVAILABLE  = 1;
	public static final int GIFT_UNVAILABLE = 0;
	
	public static final Integer WITH_ADDRESS = 1;
	public static final Integer WITHOUT_ADDRESS = 0;
	
	public static final Integer WITH_CODE = 1;
	public static final Integer WITHOUT_CODE = 0;

    @Column(name = "name", nullable = true)
	private String name;
    
    @Column(name = "details", nullable = true)
	private String details;
    
    /**
     * 商品类型 TYPE_VIRTUAL 虚拟 TYPE_REAL实物
     */
    @Column(name = "payType", nullable = true)
	private int payType = 0;
    
    @Column(name = "price", nullable = true)
	private double price;
    
    @Column(name = "payoff", nullable = true)
	private double payoff;
    
    /**
     * 下架时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "overTime", nullable = true)
	private Date overTime;
    
    /**
     * 库存
     */
    @Column(name = "stock", nullable = true)
	private Integer stock;
    
    /**
     * 积分价格
     */
    @Column(name = "scorePrice", nullable = true)
	private Integer scorePrice;
    
    @Column(name = "type", nullable = true)
	private Integer type;
    
    /**
     * 限购数量
     */
    @Column(name = "limitCount", nullable = true)
	private int limitCount; //  限购数量
	
    /**
     * 已购买数量
     */
    @Column(name = "takenCount", nullable = true)
	private int takenCount; //  已购买数量

    /**
     * ios用商品id
     */
    @Column(name = "appStoreId", nullable = true)
	private String appStoreId;

    /**
     * 图片地址
     */
    @Column(name = "url", nullable = true)
	private String url;

    /**
     * 介绍
     */
    @Column(name = "introduction", nullable = true)
	private String introduction;
    
    /**
     * 可送礼物？
     */
    @Column(name = "gift", nullable = true)
	private Integer gift;
	
	/**
	 * 状态
	 */
    @Column(name = "state", nullable = true)
	private int state;
	
	/**
	 * 0无激活码商品类型  1则为有
	 */
    @Column(name = "isCode", nullable = true)
	private Integer isCode; //0无激活码商品类型  1则为有
	
	/**
	 * 功能码 用于购买后逻辑
	 */
    @Column(name = "func", nullable = true)
	private Integer func;
	
	/**
	 * 支持的支付类型
	 */
    @Column(name = "cashPayType", nullable = true)
	private int cashPayType;
	
	/**
	 * 0为不支持地址 1 为支持
	 */
    @Column(name = "needAddress", nullable = true)
	private Integer needAddress = 0;//0为不支持地址 1 为支持


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPayoff() {
		return payoff;
	}
	public void setPayoff(double payoff) {
		this.payoff = payoff;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getScorePrice() {
		return scorePrice;
	}
	public void setScorePrice(Integer scorePrice) {
		this.scorePrice = scorePrice;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	public int getTakenCount() {
		return takenCount;
	}
	public void setTakenCount(int takenCount) {
		this.takenCount = takenCount;
	}
	public String getAppStoreId() {
		return appStoreId;
	}
	public void setAppStoreId(String appStoreId) {
		this.appStoreId = appStoreId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Integer getGift() {
		return gift;
	}
	public void setGift(Integer gift) {
		this.gift = gift;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Integer getIsCode() {
		return isCode;
	}
	public void setIsCode(Integer isCode) {
		this.isCode = isCode;
	}
	public Integer getFunc() {
		return func;
	}
	public void setFunc(Integer func) {
		this.func = func;
	}
	public int getCashPayType() {
		return cashPayType;
	}
	public void setCashPayType(int cashPayType) {
		this.cashPayType = cashPayType;
	}
	public Integer getNeedAddress() {
		return needAddress;
	}
	public void setNeedAddress(Integer needAddress) {
		this.needAddress = needAddress;
	}

}
