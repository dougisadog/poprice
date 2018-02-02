package poprice.wechat.domain.base;

import custom.tool.annotation.RenderParameter;
import poprice.wechat.domain.AbstractAuditingEntity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统公告功能
 */
@Entity
@Table(name = "ANNOUNCEMENT")
public class Announcement extends AbstractAuditingEntity implements Serializable {
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
     * 实际内容，text
     */
    @NotBlank
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 发布日期
     */
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate = LocalDateTime.now();

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

    public String getSpec() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }
}
