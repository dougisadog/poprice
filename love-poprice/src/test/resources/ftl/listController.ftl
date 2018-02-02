package ${packageName};


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

import ${projectName}.Constants;
import ${projectName}.security.SecurityUtils;
import ${projectName}.util.BeanUtils;
import ${projectName}.util.Servlets;
import ${projectName}.util.persistence.SearchFilter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import ${classPackageName};
import ${repositoryClassPackageName};

@Controller
@RequestMapping("${mapping}")
public class ${className}Controller {

    private final Logger log = LoggerFactory.getLogger(${className}Controller.class);

    @Inject
    private ${repositoryClassName} ${repositoryClassSimpleName};

    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "sortProp", defaultValue = "id") String sortProp,
        @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder, final Model model, final HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Specification<${className}> spec = SearchFilter.buildSpecificationByParams(searchParams, ${className}.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);
        List<${className}> list = ${repositoryClassSimpleName}.findAll(spec, sort);
        model.addAttribute("list", list);
        return "${mapping}/list";
    }
    
    @RequestMapping(value = "/create")
    public String create(final Model model) {
        ${className} entity = new ${className}();
        model.addAttribute("entity", entity);
        return "${mapping}/create";
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid final ${className} entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "${mapping}/create";
        }

        entity.setCreatedBy(SecurityUtils.getCurrentLogin());
        entity.setLastModifiedBy(SecurityUtils.getCurrentLogin());

        try {
            ${repositoryClassSimpleName}.save(entity);
            redirectAttributes.addFlashAttribute("flash.message", "新增'" + entity.getId() + "'成功");
            log.info("{}新增${className}成功, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
        } catch (Exception e) {
            request.setAttribute("flash.error", "新增'" + entity.getId() + "'失败:" + e.getMessage());
            log.error("{}新增${className}失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("新增失败", e);
            model.addAttribute("entity", entity);
            return "${mapping}/create";
        }

        return "redirect:${mapping}/list";
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        ${className} entity = ${repositoryClassSimpleName}.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:${mapping}/list";
        }

        model.addAttribute("entity", entity);
        return "${mapping}/show";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        ${className} entity = ${repositoryClassSimpleName}.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:${mapping}/list";
        }

        model.addAttribute("entity", entity);
        return "${mapping}/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid final ${className} entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request){
        ${className} exist = ${repositoryClassSimpleName}.findOne(entity.getId());
        if (exist == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:${mapping}/list";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "${mapping}/edit";
        }

        BeanUtils.copyPropertiesByDefault(entity, exist);
        try {
            ${repositoryClassSimpleName}.save(exist);
            redirectAttributes.addFlashAttribute("flash.message", "修改'" + exist.getId() + "'成功");
            log.info("{}修改${className}成功, 提交数据Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.info("{}修改${className}成功, 保存数据Entity:{}", SecurityUtils.getCurrentLogin(), exist.toString());
        } catch (Exception e) {
            request.setAttribute("flash.error", "修改'" + entity.getId() + "'失败");
            log.error("{}修改${className}失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("修改失败", e);
            model.addAttribute("entity", entity);
            return "${mapping}/edit";
        }
        return  "redirect:${mapping}/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") final Long id, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        ${className} entity = ${repositoryClassSimpleName}.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:${mapping}/list";
        }
        try {
            ${repositoryClassSimpleName}.delete(entity);
            redirectAttributes.addFlashAttribute("flash.message", "删除'" + entity.getId() + "'成功");
            log.info("{}删除${className}成功, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flash.error", "删除'" + entity.getId() + "'失败");
            log.error("{}删除${className}失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("删除失败", e);
            return "redirect:" + request.getHeader("referer");
        }
        return  "redirect:${mapping}/list";
    }

}

