package poprice.wechat.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import poprice.wechat.Constants;
import poprice.wechat.util.RestUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录之后记录成功和失败。http://stackoverflow.com/questions/15493237/how-to-correctly-update-the-login-date-time-after-successful-login-with-spring-s
 * 另外可以 google spring boot AuthenticationSuccessHandler 或者 spring security login callback
 * 参考：http://www.baeldung.com/spring_redirect_after_login
 *
 */
@Service
public class AuthencationResultService implements ApplicationListener<AbstractAuthenticationEvent> {

    @Inject
    private AuditLogService auditLogService;

    /**
     * 登录事件判断，成功还是失败
     * @param event
     */
    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = RestUtils.getRealIp(request);

        if (event instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event1 = (AuthenticationSuccessEvent)event;
            String userName = ((UserDetails) event1.getAuthentication().getPrincipal()).getUsername();
            auditLogService.save(userName, ip, Constants.ModuleType.user, Constants.ActionType.login, "登录成功");
        } else if (event instanceof AbstractAuthenticationFailureEvent) {
            AbstractAuthenticationFailureEvent event1 = (AbstractAuthenticationFailureEvent)event;
            if (event1.getException() instanceof UsernameNotFoundException) {
                return;
            }
            String userName = event1.getAuthentication().getPrincipal().toString();
            auditLogService.save(userName, ip, Constants.ModuleType.user, Constants.ActionType.login, "登录失败:" + event1.getException().getMessage());
        }
    }
}
