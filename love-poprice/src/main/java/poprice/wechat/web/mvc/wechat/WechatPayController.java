package poprice.wechat.web.mvc.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qq.wechat.WechatHaoHelper;
import com.qq.wechat.WechatHaoPayHelper;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/wechat/order")
public class WechatPayController {

    private final Logger log = LoggerFactory.getLogger(WechatPayController.class);

    @RequestMapping(value = "/pay")
    public String pay(HttpServletRequest httpRequest, HttpServletResponse httpResponse, final Model model) {
        String contextpath = httpRequest.getScheme()
                + "://"
                + httpRequest.getServerName()
                + ":" + httpRequest.getServerPort()
                + httpRequest.getContextPath();
        Map<String, String> ret = WechatHaoPayHelper.getInstance().sign(contextpath);
        //String openid = "oMvjavwH3x2wbkq5xdfw3lQsksCU";
        String openId = "ov1zQwSeOo_3E1upFSWHjMa8s3Dg";
        if (httpRequest.getParameterMap().containsKey("code")) {
            String code = httpRequest.getParameterValues("code")[0];
            openId = WechatHaoHelper.getInstance().getWechat().base().openId(code);
        }
        ret.put("openId", openId);
        model.addAllAttributes(ret);
        return "wechat/pay";

    }


}
