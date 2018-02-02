package poprice.wechat.web.mvc.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import poprice.wechat.domain.Authority;
import poprice.wechat.domain.Role;
import poprice.wechat.repository.RoleRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuthorityService;
import poprice.wechat.util.BeanUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 权限列表的Controller，仅查看。
 */
@Controller
@RequestMapping("/account/authority")
public class AuthorityController {
    private final Logger log = LoggerFactory.getLogger(AuthorityController.class);

    @Inject
    private AuthorityService authorityService;

    @Inject
    private RoleRepository roleRepository;

    @RequestMapping(value = "/list")
    public String list(final Model model) {
        List<Authority> list = authorityService.getList();
        model.addAttribute("list", list);
        return "account/authority/list";
    }

}

