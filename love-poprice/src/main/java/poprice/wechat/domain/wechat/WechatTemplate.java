package poprice.wechat.domain.wechat;

import custom.tool.annotation.RenderParameter;
import poprice.wechat.domain.AbstractAuditingEntity;

import org.hibernate.validator.constraints.NotBlank;

import com.qq.wechat.template.TemplateParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "WECHAT_TEMPLATE")
public class WechatTemplate extends AbstractAuditingEntity implements Serializable {

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public static final String APPOINT = "p_lDlEJK9YgJW7oSaCi5MeSa6FmCErK8vAq_eczs2-M";

	public static final String VIP = "hJW_BgCvIKoCyWXc7W8_qBVFpH3g_FEKkOc_p1fu2ow";

	@Transient
	private List<TemplateParam> templateParams;

    public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 是否激活
     */
    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "title")
    @RenderParameter(label = "标题", onPage = true)
    private String title;

    @Column(name = "link")
    private String link;

	@Column(name = "templateId")
	private String templateId;

    @Column(name = "number")
    @RenderParameter(label = "编号", onPage = true)
    private String number;

    @Column(name = "industry")
    @RenderParameter(label = "行业", onPage = true)
    private String industry;

    /**
     * 实际内容，text
     */
    @NotBlank
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

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

	public List<TemplateParam> getTemplateParams() {
		return templateParams;
	}

	public void setTemplateParams(List<TemplateParam> templateParams) {
		this.templateParams = templateParams;
	}
}
