package poprice.wechat.web.mvc.account;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poprice.wechat.Constants;
import poprice.wechat.domain.Role;
import poprice.wechat.domain.User;
import poprice.wechat.repository.AuditLogRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuditLogService;
import poprice.wechat.service.UserService;
import poprice.wechat.util.RestUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("/account/profile")
public class ProfileController {
    private final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Inject
    private UserService userService;

    @Inject
    private AuditLogService auditLogService;

    @RequestMapping(value = "edit-password")
    public String editPassword() {
        return "account/profile/edit-password";
    }

    @RequestMapping(value = "update-password", method = RequestMethod.POST)
    public String editPassword(@RequestParam(value = "password") String password,
                               @RequestParam(value = "newPassword") String newPassword,
                               final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        String username = SecurityUtils.getCurrentLogin();
        String ip = RestUtils.getRealIp(request);

        if(StringUtils.isBlank(password) || StringUtils.isBlank(newPassword)) {
            redirectAttributes.addFlashAttribute("flash.error", "登录密码不能为空！");
            return "redirect:/account/profile/edit-password";
        }


        try {
            userService.changePassword(password, newPassword);
            redirectAttributes.addFlashAttribute("flash.message", "修改密码成功");
            log.info(username + "修改密码成功, IP:" + ip);
            auditLogService.save(username, ip, Constants.ModuleType.user, Constants.ActionType.pwd, "修改密码成功");
        } catch (Exception e) {
            log.error(username + "修改密码失败", e);
            auditLogService.save(username, ip, Constants.ModuleType.user, Constants.ActionType.pwd, "修改密码失败");
            redirectAttributes.addFlashAttribute("flash.error", "修改密码失败:" + e.getMessage());
        }
        return "redirect:/account/profile/edit-password";
    }

}
