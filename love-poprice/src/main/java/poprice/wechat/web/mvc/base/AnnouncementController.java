package poprice.wechat.web.mvc.base;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import poprice.wechat.Constants;
import poprice.wechat.domain.base.Announcement;
import poprice.wechat.repository.base.AnnouncementRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuditLogService;
import poprice.wechat.util.BeanUtils;
import poprice.wechat.util.RestUtils;
import poprice.wechat.util.Servlets;
import poprice.wechat.util.persistence.SearchFilter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/base/announcement")
public class AnnouncementController {

    private final Logger log = LoggerFactory.getLogger(AnnouncementController.class);

    @Inject
    private AnnouncementRepository announcementRepository;

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
        Specification<Announcement> spec = SearchFilter.buildSpecificationByParams(searchParams, Announcement.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Page<Announcement> page = announcementRepository.findAll(spec, pageRequest);
        model.addAttribute("page", page);
        Constants.update("announcement",announcementRepository.findByActiveTrue());
        return "base/announcement/list";
    }

    @RequestMapping(value = "/create")
    public String create(final Model model) {
        Announcement entity = new Announcement();
        model.addAttribute("entity", entity);
        return "base/announcement/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid final Announcement entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "base/announcement/create";
        }
        try {
            announcementRepository.save(entity);
            redirectAttributes.addFlashAttribute("flash.message", "新增'" + entity.getTitle() + "'成功");
            log.info("{}新增Announcement成功, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.announcement, Constants.ActionType.c, "ID:" + entity.getId() + ", " + entity.getTitle());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flash.error", "新增'" + entity.getTitle() + "'失败:" + e.getMessage());
            log.error("{}新增Announcement失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("新增失败", e);
        }

        return  "redirect:/base/announcement/list";
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Announcement entity = announcementRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:/base/announcement/list";
        }

        model.addAttribute("entity", entity);
        return "base/announcement/show";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Announcement entity = announcementRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:/base/announcement/list";
        }

        model.addAttribute("entity", entity);
        return "base/announcement/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid final Announcement entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        Announcement exist = announcementRepository.findOne(entity.getId());
        if (exist == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "base/announcement/list";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "base/announcement/edit";
        }

        BeanUtils.copyPropertiesByDefault(entity, exist);
        try {
            announcementRepository.save(exist);
            redirectAttributes.addFlashAttribute("flash.message", "修改'" + exist.getTitle() + "'成功");
            log.info("{}修改Announcement成功, 提交数据Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.info("{}修改Announcement成功, 保存数据Entity:{}", SecurityUtils.getCurrentLogin(), exist.toString());

            auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.announcement, Constants.ActionType.u, "ID:" + entity.getId() + ", " + entity.getTitle());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flash.error", "修改'" + entity.getTitle() + "'失败");
            log.error("{}修改Announcement失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("修改失败", e);
        }
        return  "redirect:/base/announcement/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") final Long id, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        Announcement entity = announcementRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:/base/announcement/list";
        }
        try {
            announcementRepository.delete(entity);
            redirectAttributes.addFlashAttribute("flash.message", "删除'" + entity.getTitle() + "'成功");
            log.info("{}删除$Announcement成功, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.announcement, Constants.ActionType.d, "ID:" + entity.getId() + ", " + entity.getTitle());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flash.error", "删除'" + entity.getTitle() + "'失败");
            log.error("{}删除Announcement失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("删除失败", e);
        }
        return  "redirect:/base/announcement/list";
    }


}

