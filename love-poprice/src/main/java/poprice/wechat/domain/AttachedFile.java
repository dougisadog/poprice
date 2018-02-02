package poprice.wechat.domain;

import com.google.common.base.Strings;

import poprice.wechat.Constants;
import poprice.wechat.util.Identities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 上传文件需要存到这个表里面，下载会通过uuid
 */
@Entity
@Table(name = "ATTACHED_FILE", indexes = {@Index(name="IDX_ATTACHED_FILE_UUID", columnList = "uuid", unique = true)})
public class AttachedFile extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 其他的表的id，关联到哪个
     */
    private Long relevantId;

    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String originalName;

    @Size(max = 255)
    @Column(length = 255, nullable = true)
    private String localName;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String uuid;

    @Column(length = 150, nullable = false)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "attached_type", length = 32)
    private Constants.AttachedType attachedType;

    public void generateLocalName() {
        this.uuid = Identities.uuid2();
        int pos = originalName.lastIndexOf(".");
        String ext = "";
        if (pos != -1 && pos != originalName.length() - 1) {
            ext = originalName.substring(pos + 1);
        }

        this.localName = this.uuid + "." + ext;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRelevantId() {
        return relevantId;
    }

    public void setRelevantId(Long relevantId) {
        this.relevantId = relevantId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Constants.AttachedType getAttachedType() {
        return attachedType;
    }

    public void setAttachedType(Constants.AttachedType attachedType) {
        this.attachedType = attachedType;
    }

    @Transient
    public boolean isImage() {
        if (!Strings.isNullOrEmpty(this.contentType)) {
            return this.contentType.toLowerCase().startsWith("image");
        }
        if (!Strings.isNullOrEmpty(this.originalName)) {
            String name = this.originalName.toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".jpeg") || name.endsWith(".bmp");
        }
        return false;
    }

    @Transient
    public boolean isWord() {
        if (!Strings.isNullOrEmpty(this.originalName)) {
            String name = this.originalName.toLowerCase();
            return name.endsWith(".doc") || name.endsWith(".docx");
        }
        return false;
    }

    @Transient
    public boolean isExcel() {
        if (!Strings.isNullOrEmpty(this.originalName)) {
            String name = this.originalName.toLowerCase();
            return name.endsWith(".xls") || name.endsWith(".xlsx");
        }
        return false;
    }

    @Transient
    public boolean isPowerpoint() {
        if (!Strings.isNullOrEmpty(this.originalName)) {
            String name = this.originalName.toLowerCase();
            return name.endsWith(".ppt") || name.endsWith(".pptx");
        }
        return false;
    }

    @Transient
    public boolean isPdf() {
        if (!Strings.isNullOrEmpty(this.originalName)) {
            String name = this.originalName.toLowerCase();
            return name.endsWith(".pdf");
        }
        return false;
    }

    @Transient
    public boolean isUnkown() {
        return !isImage() && !isWord() && !isExcel() && !isPowerpoint() && !isPdf();
    }
}
