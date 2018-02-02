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
import poprice.wechat.domain.base.Item;
import poprice.wechat.repository.base.ItemRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.util.BeanUtils;
import poprice.wechat.util.Servlets;
import poprice.wechat.util.persistence.SearchFilter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/base/item")
public class ItemController {

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Inject
    private ItemRepository menuRepository;

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
        Specification<Item> spec = SearchFilter.buildSpecificationByParams(searchParams, Item.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Page<Item> page = menuRepository.findAll(spec, pageRequest);
        model.addAttribute("page", page);
        return "base/item/list";
    }

    @RequestMapping(value = "/create")
    public String create(final Model model) {
        Item entity = new Item();
        model.addAttribute("entity", entity);
        return "base/item/create";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid final Item entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "base/item/create";
        }
        try {
            menuRepository.save(entity);
            redirectAttributes.addFlashAttribute("flash.message", "新增'" + entity.getId() + "'成功");
            log.info("{}新增项目成功, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
        } catch (Exception e) {
            request.setAttribute("flash.error", "新增'" + entity.getId() + "'失败:" + e.getMessage());
            log.error("{}新增项目失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            model.addAttribute("entity", entity);
            return "base/item/create";
        }
        return "redirect:/base/item/list";
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Item entity = menuRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:/item/list";
        }

        model.addAttribute("entity", entity);
        return "base/item/show";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Item entity = menuRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:/base/item/list";
        }

        model.addAttribute("entity", entity);
        return "base/item/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid final Item entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request){
        Item exist = menuRepository.findOne(entity.getId());
        if (exist == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/base/item/list";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "base/item/edit";
        }

        BeanUtils.copyPropertiesByDefault(entity, exist);
        try {
            menuRepository.save(exist);
            redirectAttributes.addFlashAttribute("flash.message", "修改'" + exist.getId() + "'成功");
            log.info("{}修改项目成功, 提交数据Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.info("{}修改项目成功, 保存数据Entity:{}", SecurityUtils.getCurrentLogin(), exist.toString());
        } catch (Exception e) {
            request.setAttribute("flash.error", "修改'" + entity.getId() + "'失败");
            log.error("{}修改项目失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            model.addAttribute("entity", entity);
            return "base/item/edit";
        }
        return  "redirect:/base/item/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") final Long id, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        Item entity = menuRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return  "redirect:/base/item/list";
        }
        try {
            menuRepository.delete(entity);
            redirectAttributes.addFlashAttribute("flash.message", "删除'" + entity.getId() + "'成功");
            log.info("{}删除项目成功, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flash.error", "删除'" + entity.getId() + "'失败");
            log.error("{}删除项目失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            return "redirect:" + request.getHeader("referer");
        }
        return  "redirect:/base/item/list";
    }

}

