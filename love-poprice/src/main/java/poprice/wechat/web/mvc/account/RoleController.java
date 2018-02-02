package poprice.wechat.web.mvc.account;

import com.google.common.collect.Lists;

import poprice.wechat.domain.Authority;
import poprice.wechat.domain.Role;
import poprice.wechat.repository.RoleRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuthorityService;
import poprice.wechat.service.RoleService;
import poprice.wechat.util.BeanUtils;
import poprice.wechat.util.Servlets;
import poprice.wechat.util.persistence.SearchFilter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 角色的CRUD，字典表, 无分页和搜索，可以页面排序。
 */
@Controller
@RequestMapping("/account/role")
public class RoleController {
    private final Logger log = LoggerFactory.getLogger(RoleController.class);

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private RoleService roleService;

    @Inject
    private AuthorityService authorityService;


    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "sortProp", defaultValue = "id") String sortProp,
                       @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder,
                       final Model model, final HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Specification<Role> spec = SearchFilter.buildSpecificationByParams(searchParams, Role.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);

        List<Role> list = roleRepository.findAll(spec, sort);
        model.addAttribute("list", list);
        return "account/role/list";
    }

    @RequestMapping(value = "/create")
    public String create(final Model model) {
        Role entity = new Role();
        model.addAttribute("entity", entity);

        List<Authority> authList = authorityService.getList();
        model.addAttribute("authList", authList);

        return "account/role/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid final Role entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            List<Authority> authList = authorityService.getList();
            model.addAttribute("authList", authList);

            return "account/role/create";
        }

        entity.setId(null);
        entity.setCreatedBy(SecurityUtils.getCurrentLogin());
        entity.setLastModifiedBy(SecurityUtils.getCurrentLogin());

        roleService.save(entity);
        redirectAttributes.addFlashAttribute("flash.message", "新增角色'" + entity.getName() + "'成功");
        log.info("{}新增角色{}成功, ID:{}", SecurityUtils.getCurrentLogin(), entity.getName(), entity.getId());
        return "redirect:/account/role/show/" + entity.getId();
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Role entity = roleRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/account/role/list";
        }

        List<String> descList = Lists.newArrayList();
        for (String authKey : entity.getAuthorities()) {
            descList.add(authorityService.getByAuthority(authKey).getName());
        }
        model.addAttribute("entity", entity);
        model.addAttribute("descList", descList);
        return "account/role/show";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Role entity = roleRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/account/role/list";
        }

        model.addAttribute("entity", entity);
        List<Authority> authList = authorityService.getList();
        model.addAttribute("authList", authList);

        return "account/role/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid final Role entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {
        Role exist = roleRepository.findOne(entity.getId());
        if (exist == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/account/role/list";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            List<Authority> authList = authorityService.getList();
            model.addAttribute("authList", authList);

            return "account/role/edit";
        }

        BeanUtils.copyPropertiesByDefault(entity, exist);

        roleService.save(exist);
        redirectAttributes.addFlashAttribute("flash.message", "更新角色'" + exist.getName() + "'成功");
        log.info("{}更新角色{}成功, ID:{}", SecurityUtils.getCurrentLogin(), exist.getName(), exist.getId());
        return "redirect:/account/role/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") final Long id, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        Role entity = roleRepository.findOne(id);

        try {
            roleService.delete(id);
        } catch (Exception e) {
            log.error(SecurityUtils.getCurrentLogin() + "删除角色失败id:" + id, e);
            redirectAttributes.addFlashAttribute("flash.error", "删除角色'" + entity.getName() + "'失败");
            return "redirect:" + request.getHeader("referer");
        }

        redirectAttributes.addFlashAttribute("flash.message", "删除角色'" + entity.getName() + "'成功");
        log.info("{}删除角色{}成功, ID:{}", SecurityUtils.getCurrentLogin(), entity.getName(), entity.getId());
        return "redirect:/account/role/list";
    }

}

