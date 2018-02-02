package poprice.wechat.domain.shop;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import custom.tool.annotation.RenderParameter;
import poprice.wechat.domain.AbstractAuditingEntity;

@Entity
@Table(name = "ADDRESS")
public class Address extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 地址
     */
    @Column(name = "location", nullable = true)
    private String location;
	
    /**
     * 街道
     */
    @Column(name = "street", nullable = true)
	private String street;
	
    /**
     * 详细地址
     */
    @Column(name = "detailed", nullable = true)
	private String detailed;
	
    @Column(name = "zipCode", nullable = true)
	private String zipCode; //邮政编码
	
    @Column(name = "recipient", nullable = true)
	private String recipient; //接收人姓名
	
    @Column(name = "phone", nullable = true)
	private String phone;  //联系电话
	
    /**
     * 主要收货地址地址
     */
    @Column(name = "main", nullable = true)
	private Boolean main;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDetailed() {
		return detailed;
	}

	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Boolean getMain() {
		return main;
	}

	public void setMain(Boolean main) {
		this.main = main;
	}
}
