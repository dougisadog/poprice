package poprice.wechat.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import poprice.wechat.domain.base.Announcement;

import java.util.List;

/**
 * 系统公告功能
 */
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> , JpaSpecificationExecutor<Announcement> {
    List<Announcement> findByActiveTrue();
}
