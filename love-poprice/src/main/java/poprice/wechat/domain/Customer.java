package poprice.wechat.domain;

import custom.tool.annotation.RenderParameter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;


@Entity
@Table(name = "customer")
public class Customer extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "open_id")
    private String openId;

    @Size(max = 50)
    @Column(name = "nick_name", length = 20)
    @RenderParameter(label = "微信昵称", onPage = true)
    private String nickName;

    @Column(name = "real_name")
    @RenderParameter(label = "客户名称", onPage = true)
    private String realName;

    @Column(name = "phone")
    @RenderParameter(label = "电话", onPage = true)
    private String phone;

    /**
     * 0未知，1男，2女
     */
    @Column(name = "sex")
    private Integer sex;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "head_img_url")
    private String headImgUrl;

    /**
     * 关注时间
     */
    @Column(name = "subscribe_time", nullable = false)
    private Date subscribeTime;

    /**
     * unionId
     */
    @Column(name = "union_id")
    private String unionId;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * groupId
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 0未关注，1已关注
     */
    @Column(name = "subscribe")
    private Integer subscribe;

    /**
     * 是否关注
     */
    @Column(nullable = false)
    private boolean active = true;

    public Customer() {
    }

    public Customer(me.hao0.wechat.model.user.User user) {
        this.openId = user.getOpenId();
        this.nickName = user.getNickName();
        this.country = user.getCountry();
        this.province = user.getProvince();
        this.city = user.getCity();
        this.headImgUrl = user.getHeadImgUrl();
        this.sex = user.getSex();
        this.remark = user.getRemark();
        this.subscribe = user.getSubscribe();
        this.subscribeTime = user.getSubscribeTime();
        this.unionId = user.getUnionId();
        this.groupId = user.getGroupId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
