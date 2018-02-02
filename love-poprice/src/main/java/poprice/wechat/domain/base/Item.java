package poprice.wechat.domain.base;

import custom.tool.annotation.RenderParameter;
import poprice.wechat.domain.AbstractAuditingEntity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
public class Item extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 是否激活
     */
    @Column(nullable = false)
    private boolean active = true;

    @NotBlank
    @Size(max = 50)
    @Column(name = "title", length = 100)
    @RenderParameter(label = "标题", onPage = true)
    private String title;

    /**
     * 价格
     */
    @NotNull
    @Column(precision=12, scale=2)
    private BigDecimal price = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}
