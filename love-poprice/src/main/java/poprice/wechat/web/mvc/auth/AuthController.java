package poprice.wechat.web.mvc.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(value = "/index")
    public String index() {
        return "auth/index";
    }

    @RequestMapping(value = "/dispatch")
    public String dispatch(final HttpServletRequest request) {
        /*
        String sessionId = request.getSession().getId();
        String ip = request.getRemoteAddr();
        log.info("用户登录成功{}访问dispatch，IP:{}, SessionID: {}", SecurityUtils.getCurrentLogin(), ip, sessionId);
        //*/
        return "redirect:/customer/list";
    }
}
