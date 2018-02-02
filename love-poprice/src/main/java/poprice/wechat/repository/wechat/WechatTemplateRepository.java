package poprice.wechat.repository.wechat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.wechat.WechatTemplate;

import java.util.List;

public interface WechatTemplateRepository extends JpaRepository<WechatTemplate,Long> , JpaSpecificationExecutor<WechatTemplate> {
	List<WechatTemplate> findByActiveTrue();

	List<WechatTemplate> findByTemplateId(String templateId);
}
