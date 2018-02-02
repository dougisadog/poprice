package poprice.wechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poprice.wechat.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByUsername(String username);

    @Query("select u from User u left join u.roles r where r.id=:id")
    List<User> findAllByRoleId(@Param("id") Long roleId);

    @Query("select u from User u left join u.roles r where r.id=:id and u.id > 2")
    List<User> findAllAvailableByRoleId(@Param("id") Long roleId);

    @Override
    void delete(User t);

    int countByEmail(String email);
}
