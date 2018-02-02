package poprice.wechat.repository;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import poprice.wechat.domain.PersistentToken;
import poprice.wechat.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
