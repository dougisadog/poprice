package poprice.wechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.Constants;
import poprice.wechat.domain.AttachedFile;

import java.util.List;

/**
 * Spring Data JPA repository for the ContractType entity.
 */
public interface AttachedFileRepository extends JpaRepository<AttachedFile,Long>, JpaSpecificationExecutor<AttachedFile> {

    AttachedFile findOneByUuid(String uuid);

    List<AttachedFile> findByRelevantIdAndAttachedType(Long relevantId, Constants.AttachedType attachedType);
}
