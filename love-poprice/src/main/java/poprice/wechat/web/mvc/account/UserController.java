package poprice.wechat.web.mvc.account;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import poprice.wechat.Constants;
import poprice.wechat.domain.*;
import poprice.wechat.repository.RoleRepository;
import poprice.wechat.repository.UserRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuditLogService;
import poprice.wechat.service.AuthorityService;
import poprice.wechat.service.UserService;
import poprice.wechat.util.BeanUtils;
import poprice.wechat.util.Reflections;
import poprice.wechat.util.RestUtils;
import poprice.wechat.util.Servlets;
import poprice.wechat.util.persistence.SearchFilter;

import org.springframework.data.jpa.domain.Specifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 用户的CRUD，字典表, 有分页和搜索，可以页面排序。
 */
@Controller
@RequestMapping("/account/user")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityService authorityService;

    @Inject
    private AuditLogService auditLogService;

    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "pageSize", defaultValue = "0") Integer pageSize,
                       @RequestParam(value = "sortProp", defaultValue = "id") String sortProp,
                       @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
                       final Model model, final HttpServletRequest request) {
        if (pageSize == 0) {
            pageSize = Constants.getInt("page_size");
        }

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Specification<User> spec = SearchFilter.buildSpecificationByParams(searchParams, User.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);

        Specification<User> spec1 = (root, query, builder)-> {
            Predicate p = builder.equal(root.get("adminFlag"), true);
            return p;
        };
        Page<User> page = userRepository.findAll(Specifications.where(spec).and(spec1), pageRequest);
        model.addAttribute("page", page);
        return "account/user/list";
    }

    @RequestMapping(value = "/create")
    public String create(final Model model) {
        User entity = new User();
        model.addAttribute("entity", entity);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        return "account/user/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid final User entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        //用户名重复
        Optional<User> _user = userRepository.findOneByUsername(entity.getUsername());
        if (_user.isPresent()) {
            FieldError fieldError = new FieldError("user", "username", entity.getUsername(), true, null, null, "用户名不能重复");
            bindingResult.addError(fieldError);
        }
        if (entity.getEmail() != null && !entity.getEmail().equals("")) {
            Optional<User> _user1 = userRepository.findOneByEmail(entity.getEmail());
            if (_user1.isPresent()) {
                FieldError fieldError = new FieldError("user", "email", entity.getEmail(), true, null, null, "邮箱已经存在,不能重复");
                bindingResult.addError(fieldError);
            }
        }

        if (Strings.isNullOrEmpty(entity.getPassword1()) || entity.getPassword1().length() < 6 || entity.getPassword1().length() > 25) {
            FieldError fieldError = new FieldError("user", "password1", entity.getPassword1(), true, null, null, "密码不能为空，长度在6-25字符之间");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);

            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);

            return "account/user/create";
        }

        entity.setId(null);
        entity.setCreatedBy(SecurityUtils.getCurrentLogin());
        entity.setLastModifiedBy(SecurityUtils.getCurrentLogin());
        entity.setAdminFlag(true);
        userService.saveUser(entity);
        redirectAttributes.addFlashAttribute("flash.message", "新增用户'" + entity.getUsername() + "'成功");
        log.info("{}新增用户{}成功, ID:{}", SecurityUtils.getCurrentLogin(), entity.getUsername(), entity.getId());

        auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.user, Constants.ActionType.c, "ID:" + entity.getId() + ", " + entity.getUsername());

        return "redirect:/account/user/show/" + entity.getId();
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        User entity = userRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/account/user/list";
        }

        List<String> descList = Lists.newArrayList();
        if (entity.getId() == 1L) { //超级管理员
            for (Authority authority : authorityService.getList()) {
                descList.add(authority.getName());
            }
        } else {
            for (Role role : entity.getRoles()) {
                for (String authName : role.getAuthorities()) {
                    Authority authority = authorityService.getByAuthority(authName);
                    descList.add(authority.getName());
                }
            }
        }

        model.addAttribute("descList", descList);
        model.addAttribute("entity", entity);

        return "account/user/show";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        User entity = userRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/account/user/list";
        }

        model.addAttribute("entity", entity);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        return "account/user/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid final User entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        User exist = userRepository.findOne(entity.getId());
        if (exist == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/account/user/list";
        }
        if (!Objects.equals(exist.getEmail(), entity.getEmail()) && userService.isEmailExist(entity.getEmail())) {
            FieldError fieldError = new FieldError("user", "email", entity.getEmail(), true, null, null, "邮箱已经存在,不能重复");
            bindingResult.addError(fieldError);
        }
        if (!Strings.isNullOrEmpty(entity.getPassword1()) && (entity.getPassword1().length() < 6 || entity.getPassword1().length() > 25)) {
            FieldError fieldError = new FieldError("user", "password1", entity.getPassword1(), true, null, null, "密码长度在6-25字符之间，不修改密码请留空");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);

            List<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);

            return "account/user/edit";
        }

        BeanUtils.copyPropertiesWithExclude(entity, exist, "id", "username", "password", "persistentTokens", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "adminFlag");
        userService.saveUser(exist);
        redirectAttributes.addFlashAttribute("flash.message", "更新用户'" + exist.getUsername() + "'成功");
        log.info("{}更新用户{}成功, ID:{}", SecurityUtils.getCurrentLogin(), exist.getUsername(), exist.getId());
        auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.user, Constants.ActionType.u, "ID:" + entity.getId() + ", " + entity.getUsername());
        return "redirect:/account/user/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") final Long id, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        User entity = userRepository.findOne(id);

        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            log.error(SecurityUtils.getCurrentLogin() + "删除用户失败id:" + id, e);
            redirectAttributes.addFlashAttribute("flash.error", "删除用户'" + entity.getUsername() + "'失败");
            return "redirect:" + request.getHeader("referer");
        }

        redirectAttributes.addFlashAttribute("flash.message", "删除用户'" + entity.getUsername() + "'成功");
        log.info("{}删除用户{}成功, ID:{}", SecurityUtils.getCurrentLogin(), entity.getUsername(), entity.getId());

        auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.user, Constants.ActionType.d, "ID:" + entity.getId() + ", " + entity.getUsername());
        return "redirect:/account/user/list";
    }

}

