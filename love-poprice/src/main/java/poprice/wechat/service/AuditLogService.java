package poprice.wechat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import poprice.wechat.Constants;
import poprice.wechat.domain.AuditLog;
import poprice.wechat.repository.AuditLogRepository;
import poprice.wechat.security.SecurityUtils;

import javax.inject.Inject;

/**
 * Service for managing audit events.
 *
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional
public class AuditLogService {

    @Inject
    private AuditLogRepository auditLogRepository;

    /**
     * username必须传入，因为登录的时候没法获取当前用户。
     *
     * @param username
     * @param createdIp
     * @param moduleType
     * @param actionType
     * @param content
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(String username, String createdIp, Constants.ModuleType moduleType, Constants.ActionType actionType, String content) {
        AuditLog auditLog = new AuditLog();
        auditLog.setCreatedBy(username);
        auditLog.setCreatedIp(createdIp);
        auditLog.setModuleType(moduleType);
        auditLog.setActionType(actionType);
        auditLog.setContent(content);
        auditLogRepository.save(auditLog);
    }

    public void save(String createdIp, Constants.ModuleType moduleType, Constants.ActionType actionType, String content) {
        String username = SecurityUtils.getCurrentLogin();
        save(username, createdIp, moduleType, actionType, content);
    }

}
