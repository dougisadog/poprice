package poprice.wechat.web.rest.wechatrest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qq.wechat.MenuItem;
import com.qq.wechat.WechatHaoHelper;
import com.qq.wechat.WechatHaoPayHelper;

import me.hao0.common.date.Dates;
import me.hao0.wechat.core.Callback;
import me.hao0.wepay.model.pay.JsPayRequest;
import me.hao0.wepay.model.pay.JsPayResponse;
import me.hao0.wepay.util.Maps;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/wechat")
public class WechatRestBackController {

	private final Logger log = LoggerFactory.getLogger(WechatRestBackController.class);

	@RequestMapping(value = "/menu", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String createWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {

//		String link = httpRequest.getScheme()
//				+"://"
//				+ httpRequest.getServerName()
//				+ ":" +httpRequest.getServerPort()
//				+ httpRequest.getContextPath();
		String url = "";
		if (httpRequest.getParameterMap().containsKey("url")) {
			url = httpRequest.getParameterMap().get("url")[0].trim();
			if (!url.startsWith("/")) {
				url = "/" + url;
			}
		}
		
//		String link = "http://asftys.poprice.cn" + url;
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("https://open.weixin.qq.com/connect/oauth2/authorize");
//		buffer.append("?appid=" + WechatHaoHelper.APP_ID);
//		buffer.append("&redirect_uri=" + URLEncoder.encode(link));
//		buffer.append("&response_type=code");
//		// buffer.append("&scope=" + "snsapi_base"); //不弹出授权
//		buffer.append("&scope=" + "snsapi_userinfo"); // 弹出授权
//		buffer.append("&state=STATE#wechat_redirect");
		
        String link = "http://asftys.poprice.cn" + url;
        String to = WechatHaoHelper.getInstance().getRedirectUrl(url);
		
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		MenuItem m1 = new MenuItem("我要下单", to, MenuItem.VIEW);
		MenuItem m2 = new MenuItem("优惠活动", "ACTIVITIES", MenuItem.CLICK);
		MenuItem m3 = new MenuItem("更多", "", MenuItem.PARENT);
		m3.setMenuItems(Arrays.asList(
				new MenuItem("关于我们", "ACT", MenuItem.CLICK),
				new MenuItem("模板推送", "TEMPLATE", MenuItem.CLICK),
				new MenuItem("我的订单", to, MenuItem.VIEW)));
		menuItems.add(m1);
		menuItems.add(m2);
		menuItems.add(m3);
		
		WechatHaoHelper.getInstance().createMenu(menuItems, new Callback<Boolean>() {

			@Override
			public void onSuccess(Boolean t) {
				System.out.println("create success");

			}

			@Override
			public void onFailure(Exception e) {
				System.out.println("create fail");

			}
		});
		return "success";

	}

	@RequestMapping(value = "/menu/delete", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String deleteWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		WechatHaoHelper.getInstance().deleteMenu(null);
		return "success";

	}

	/**
	 * 创建微信订单
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	@RequestMapping(value = "/order/create/{id}", method = {RequestMethod.GET,
			RequestMethod.POST})
	public JsPayResponse createOrder(@PathVariable("id") final String openid) {
		JsPayResponse r = null;
		JsPayRequest jsRequest = new JsPayRequest();
		jsRequest.setOpenId(openid);
        jsRequest.setBody("测试订单");
        jsRequest.setClientId("127.0.0.1");
        jsRequest.setTotalFee(1); //分
        jsRequest.setOutTradeNo(System.currentTimeMillis() + "");
        jsRequest.setTimeStart(Dates.now("yyyyMMddHHmmss"));
		try {
			r = WechatHaoPayHelper.getInstance().jsPay(jsRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;

	}

    /**
     * 支付成功通知
     * @param request 请求对象
     * @return 处理结果
     */
    @RequestMapping(value = "/paid/notify", method = {RequestMethod.GET,
			RequestMethod.POST}, produces = "text/plain; charset=UTF-8")
    public String paid(HttpServletRequest request){

        String notifyXml = getPostRequestBody(request);
        if (notifyXml.isEmpty()){
            return WechatHaoPayHelper.getInstance().notifyNotOk("body为空");
        }

        Map<String, Object> notifyParams = Maps.toMap(notifyXml);

        if (WechatHaoPayHelper.getInstance().verifySign(notifyParams)){

            // TODO business logic

            log.info("verify sign success: {}", notifyParams);
    		for (Entry<String, Object> entry : notifyParams.entrySet()) {
    			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue());

    		}

            return WechatHaoPayHelper.getInstance().notifyOk();
        } else {

            log.error("verify sign failed: {}", notifyParams);
            return WechatHaoPayHelper.getInstance().notifyNotOk("签名失败");
        }
    }

    public String getPostRequestBody(HttpServletRequest req) {
        if (req.getMethod().equals("POST")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = req.getReader()) {
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = br.read(charBuffer)) != -1) {
                    sb.append(charBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                log.warn("failed to read request body, cause: {}", e.toString());
            }
            return sb.toString();
        }
        return "";
    }

}
