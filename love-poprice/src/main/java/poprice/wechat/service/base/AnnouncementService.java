package poprice.wechat.service.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import poprice.wechat.domain.base.Announcement;
import poprice.wechat.repository.base.AnnouncementRepository;

import javax.inject.Inject;

/**
 * 系统公告功能
 */
@Service
public class AnnouncementService {
    private final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

    @Inject
    private AnnouncementRepository announcementRepository;

}
