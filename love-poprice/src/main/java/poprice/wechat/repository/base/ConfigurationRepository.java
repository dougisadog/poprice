package poprice.wechat.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.base.Configuration;

/**
 * Spring Data JPA repository for the Station entity.
 */
public interface ConfigurationRepository extends JpaRepository<Configuration,Long> , JpaSpecificationExecutor<Configuration> {

}
