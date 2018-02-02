package poprice.wechat.domain.base;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotBlank;

import poprice.wechat.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 专门用来保存基本数据的
 */
@Entity
@Table(name = "CONFIGURATION", indexes = {@Index(name = "IDX_CONFIGURATION_NAME", columnList = "name", unique = true)})
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * title作为名称
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false, unique = true)
    private String title;

    /**
     * name作为key
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
