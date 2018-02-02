package poprice.wechat.web.mvc.customer;

import com.google.common.collect.Lists;
import com.qq.wechat.WechatHaoHelper;
import com.qq.wechat.template.TemplateParam;
import com.qq.wechat.template.TemplateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.hao0.wechat.model.message.send.TemplateField;
import poprice.wechat.Constants;
import poprice.wechat.domain.Customer;
import poprice.wechat.domain.CustomerItemRecord;
import poprice.wechat.domain.base.Item;
import poprice.wechat.domain.wechat.WechatTemplate;
import poprice.wechat.repository.CustomerItemRecordRepository;
import poprice.wechat.repository.CustomerItemRepository;
import poprice.wechat.repository.CustomerRepository;
import poprice.wechat.repository.base.ItemRepository;
import poprice.wechat.repository.wechat.WechatTemplateRepository;
import poprice.wechat.security.SecurityUtils;
import poprice.wechat.service.AuditLogService;
import poprice.wechat.service.CustomerItemRecordService;
import poprice.wechat.util.RestUtils;
import poprice.wechat.util.Servlets;
import poprice.wechat.util.persistence.SearchFilter;
import poprice.wechat.web.mvc.JsonResult;
import poprice.wechat.web.rest.dto.ItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Inject
    private AuditLogService auditLogService;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private WechatTemplateRepository wechatTemplateRepository;

    @Inject
    private CustomerItemRecordService customerItemRecordService;

    @Inject
    private CustomerItemRecordRepository customerItemRecordRepository;

    @Inject
    private CustomerItemRepository customerItemRepository;

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
        Specification<Customer> spec = SearchFilter.buildSpecificationByParams(searchParams, Customer.class);
        Sort sort = SearchFilter.buildSort(sortProp, sortOrder);
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        Page<Customer> page = customerRepository.findAll(spec, pageRequest);
        model.addAttribute("page", page);
//        List<WechatTemplate> temps = wechatTemplateRepository.findAll();
//        model.addAttribute("temps", temps);

        return "customer/list";
    }

    @RequestMapping(value = "/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Customer entity = customerRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/customer/list";
        }
        model.addAttribute("entity", entity);

        List<CustomerItemRecord> customerItemRecordList = customerItemRecordRepository.findByCustomerOrderByIdDesc(entity);
        model.addAttribute("customerItemRecordList", customerItemRecordList);

        List<Item> items = itemRepository.findAll();

        List<ItemDTO> items1 = Lists.newArrayList();
        for (Item item : items) {
            ItemDTO itemDTO = new ItemDTO(item.getId(), item.getTitle(), customerItemRepository.findByitemTypeBAndActionTypeRec(entity, item) - customerItemRepository.findByitemTypeBAndActionTypeConsumer(entity, item));
            items1.add(itemDTO);
        }
        model.addAttribute("bugitems", items1);

        List<ItemDTO> items2 = Lists.newArrayList();
        for (Item item2 : items) {
            ItemDTO itemDTO = new ItemDTO(item2.getId(), item2.getTitle(), customerItemRepository.findByitemTypeGAndActionTypeRec(entity, item2) - customerItemRepository.findByitemTypeGAndActionTypeConsumer(entity, item2));
            items2.add(itemDTO);
        }
        model.addAttribute("giveitems", items2);

        return "customer/show";
    }

    @RequestMapping(value = "/edit/{id}")
    public String edit(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Customer entity = customerRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/customer/list";
        }

        model.addAttribute("entity", entity);
        return "customer/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid final Customer entity, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        Customer exist = customerRepository.findOne(entity.getId());
        if (exist == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "customer/list";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
            model.addAttribute("entity", entity);
            return "customer/edit";
        }

        try {
            exist.setPhone(entity.getPhone());
            exist.setRealName(entity.getRealName());
            customerRepository.save(exist);
            redirectAttributes.addFlashAttribute("flash.message", "修改'" + exist.getNickName() + "'成功");
            log.info("{}修改Customer成功, 提交数据Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.info("{}修改Customer成功, 保存数据Entity:{}", SecurityUtils.getCurrentLogin(), exist.toString());

            auditLogService.save(SecurityUtils.getCurrentLogin(), RestUtils.getRealIp(request), Constants.ModuleType.wechatCustomer, Constants.ActionType.u, "ID:" + entity.getId() + ", " + entity.getNickName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flash.error", "修改'" + exist.getNickName() + "'失败");
            log.error("{}修改Customer失败, Entity:{}", SecurityUtils.getCurrentLogin(), entity.toString());
            log.error("修改失败", e);
        }
        return "redirect:/customer/list";
    }


    @RequestMapping(value = "/{id}/item")
    public String editItem(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Customer entity = customerRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/customer/list";
        }
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        model.addAttribute("entity", entity);
        return "customer/item";
    }


    /**
     * @param id                 客户id
     * @param iid                itemId
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/{id}/{iid}/{t}/consume")
    @ResponseBody
    public JsonResult consumeItem(@PathVariable("id") final Long id, @PathVariable("iid") final Long iid, @PathVariable("t") final String t, final Model model, final RedirectAttributes redirectAttributes) {
        JsonResult result = new JsonResult(false, null, null);
        Customer entity = customerRepository.findOne(id);
        Item item = itemRepository.findOne(iid);
        if (entity == null) {
            result.setMessage("客户不存在");
        }
        if (item == null) {
            result.setMessage("套餐不存在");
        }
        result.setMessage("剩余次数不足");


        if (t.equals("b")) {
            //购买剩余次数
            if (customerItemRepository.findByitemTypeBAndActionTypeRec(entity, item) - customerItemRepository.findByitemTypeBAndActionTypeConsumer(entity, item) > 0) {
                customerItemRecordService.consume(entity,item,Constants.ItemType.b);
                result.setOk(true);
                result.setMessage("消费成功");
            }
        }

        if (t.equals("g")) {
            //赠送剩余次数
            if (customerItemRepository.findByitemTypeGAndActionTypeRec(entity, item) - customerItemRepository.findByitemTypeGAndActionTypeConsumer(entity, item) > 0) {
                customerItemRecordService.consume(entity,item,Constants.ItemType.g);
                result.setOk(true);
                result.setMessage("消费成功");
            }
        }
        return result;
    }


    @RequestMapping(value = "/update/item", method = RequestMethod.POST)
    public String updateItem(@Valid final Customer entity, Long[] buyIds, Integer[] buyNumbers, Long[] giveIds, Integer[] giveNumbers, String remark, final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        Customer exist = customerRepository.findOne(entity.getId());
        customerItemRecordService.save(exist, buyIds, buyNumbers, giveIds, giveNumbers, remark);
        return "redirect:/customer/show/" + entity.getId();
    }


    @RequestMapping(value = "/{cid}/template/{id}")
    public String template(@PathVariable("cid") final Long cid, @PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        WechatTemplate entity = wechatTemplateRepository.findOne(id);
        if (entity == null) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "redirect:/customer/list";
        }
        List<TemplateParam> templateParams = TemplateUtils.parseTemplateContent(entity.getContent());
        entity.setTemplateParams(templateParams);
        model.addAttribute("cid", cid);
        model.addAttribute("entity", entity);
        return "customer/send";
    }

    @RequestMapping(value = "/send/{cid}")
    public String send(@PathVariable("cid") final Long cid, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        WechatTemplate exist = null;
        Map<String, String[]> m = request.getParameterMap();
        List<TemplateField> fields = new ArrayList<>();
        for (Entry<String, String[]> entry : m.entrySet()) {
            if ("id".equals(entry.getKey())) {
                exist = wechatTemplateRepository.findOne(Long.parseLong(entry.getValue()[0]));
            }
            TemplateField tField = new TemplateField(entry.getKey(), entry.getValue()[0]);
            fields.add(tField);
            System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);

        }
        Customer customer = customerRepository.findOne(cid);
        if (null == exist || null == customer) {
            redirectAttributes.addFlashAttribute("flash.error", "没有找到这条数据，请重试");
            return "customer/list";
        }

        // String link = exist.getLink(); //默认链接地址
        String link = request.getScheme()
                + "://"
                + request.getServerName()
                + ":" + request.getServerPort()
                + "/introduction";
        WechatHaoHelper.getInstance().replyXmlTemplate(customer.getOpenId(),
                exist.getTemplateId(), fields, link);
        return "redirect:/customer/list";
    }


}
