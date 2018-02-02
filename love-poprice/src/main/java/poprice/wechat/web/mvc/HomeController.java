package poprice.wechat.web.mvc;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.qq.wechat.WechatHaoHelper;

import poprice.wechat.domain.Customer;
import poprice.wechat.domain.base.Announcement;
import poprice.wechat.domain.base.Item;
import poprice.wechat.repository.CustomerItemRepository;
import poprice.wechat.repository.CustomerRepository;
import poprice.wechat.repository.base.AnnouncementRepository;
import poprice.wechat.repository.base.ItemRepository;
import poprice.wechat.util.RandomValidateCode;
import poprice.wechat.web.rest.dto.ItemDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private AnnouncementRepository announcementRepository;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerItemRepository customerItemRepository;


    @RequestMapping(value = "/wx")
    public String index(@RequestParam(defaultValue = "") String code, @RequestParam(defaultValue = "") String state, HttpServletRequest request) {
//        HttpSession session = request.getSession(true);
//
//        if (!StringUtils.isBlank(code)) {
//            String openId = WechatHaoHelper.getInstance().getWechat().base().openId(code);
//            session.setAttribute(WechatHaoHelper.WX_LOGIN_KEY, openId);
//        }
////
//        //第一次进来，没登录的去登录
//        if (session.getAttribute(WechatHaoHelper.WX_LOGIN_KEY) == null) {
//            String to = WxSecurityUtil.getRedirectUrl(null, request);
//            return "redirect:" + to;
//        }

        return "index";
    }

    /**
     * 我的账号
     *
     * @return
     */
    @RequestMapping(value = "/wx/my-account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @Timed
    public String myAccount(final Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String openId = session.getAttribute(WechatHaoHelper.WX_LOGIN_KEY).toString();
        Customer entity = customerRepository.findByOpenId(openId);//用户个人信息
        model.addAttribute("entity", entity);
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
        return "my-account";
    }

    @RequestMapping(value = "/admin")
    public String admin() {
        return "admin/index";
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public JsonResult login(@RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String password, HttpServletRequest request) {
        JsonResult result = new JsonResult(false, null, null);
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authRequest); //调用loadUserByUsername
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
            result.setOk(true);
        } catch (AuthenticationException ex) {
            result.setOk(false);
            result.setMessage("用户名或密码错误.");
        }
        request.getSession().removeAttribute(RandomValidateCode.RANDOMCODEKEY);
        return result;
    }


    @RequestMapping(value = "/announcement/show/{id}")
    public String show(@PathVariable("id") final Long id, final Model model, final RedirectAttributes redirectAttributes) {
        Announcement entity = announcementRepository.findOne(id);
        if (entity == null) {
            return "redirect:/";
        }
        model.addAttribute("entity", entity);
        return "/announcement/show";
    }
}
