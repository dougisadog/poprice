package poprice.wechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.AuditLog;
import poprice.wechat.domain.Role;

/**
 * Spring Data JPA repository for the ContractType entity.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog,Long>, JpaSpecificationExecutor<AuditLog> {

}
