package poprice.wechat.web.rest.wechatrest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.UrlEncoded;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qq.wechat.WechatBase;
import com.qq.wechat.WechatHaoHelper;
import com.qq.wechat.WechatHaoPayHelper;

import me.hao0.wechat.exception.EventException;
import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.event.RecvMenuEvent;
import me.hao0.wechat.model.message.receive.event.RecvSubscribeEvent;
import me.hao0.wechat.model.message.receive.event.RecvUnSubscribeEvent;
import me.hao0.wechat.model.message.receive.msg.RecvTextMessage;
import me.hao0.wechat.model.message.resp.Article;
import me.hao0.wechat.model.message.send.TemplateField;
import me.hao0.wechat.model.user.User;
import poprice.wechat.domain.Customer;
import poprice.wechat.repository.CustomerRepository;
import poprice.wechat.service.CustomerService;

/**
 * Rest 返回對象的json
 */
@RestController
@RequestMapping("/rest")
public class WechatRestController {

    @Inject
    private CustomerService customerService;

    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/plain; charset=UTF-8")
    public String getWeixin(HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) {
        WechatBase wechatBase = new WechatBase(httpRequest);
        String result = "";
        
        String content = wechatBase.getContent();
        String msgSignature = wechatBase.getMsgSignature();
        String timestamp = wechatBase.getTimestamp();
        String nonce = wechatBase.getNonce();
        String signature = wechatBase.getSignature();
        // 更换 地址的验证
        if (!StringUtils.isEmpty(wechatBase.getEchostr()) && WechatHaoHelper.getInstance().checkSignature(timestamp, nonce, signature)) {
            result = wechatBase.getEchostr();
        }
        // 处理微信的推送消息
        else {
            try {
                if (!StringUtils.isEmpty(wechatBase.getMsgSignature())) {
                    content = WechatHaoHelper.getInstance().decodeXml(content, msgSignature, timestamp, nonce);
                }
                result = receiveMessage(content, wechatBase);
            } catch (Exception e) {
                if (e instanceof EventException) {
                    // 模板推送的过滤处理
                    System.out.println("未知类型");
                } else {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(result);
        return result;

    }

    /**
     * 将RecvMessage 封装为可返回的微信通用 格式化 字符串
     *
     * @param message
     * @param wechatBase
     * @return
     */
    private String parseResultXml(RecvMessage message, WechatBase wechatBase) {
        String result = null;
        System.out.println(message.getFromUserName());
        System.out.println(message.getMsgType());
        System.out.println(message.getToUserName());
        System.out.println(message.getCreateTime());
        // 对话的时间推送 此处仅为文字 更多扩展见RecvMsg
        if (message instanceof RecvTextMessage) {
//			RecvTextMessage m = (RecvTextMessage) message;
//			String content = m.getContent();
//			result = relpyText(m, content);
        }
        // 点击菜单的事件推送
        else if (message instanceof RecvMenuEvent) {
            replyMenuClick((RecvMenuEvent) message);
        }
        // 关注
        else if (message instanceof RecvSubscribeEvent) {
            replySubscribe(message.getFromUserName());
            User user = WechatHaoHelper.getInstance().getUserDetail(message.getFromUserName());
            String sex = 2 == user.getSex() ? "女士" : "先生";
            String content = user.getNickName() + sex + ", 爱上丰婷欢迎您";
            result = relpyText(message, content);
        }
        // 取消关注
        else if (message instanceof RecvUnSubscribeEvent) {
            relpyUnSubscribe(message.getFromUserName());
        }
        String timestamp = wechatBase.getTimestamp();
        String nonce = wechatBase.getNonce();
        try {
            if (null != result) {
                result = WechatHaoHelper.getInstance().encodeXml(result, timestamp, nonce);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 取消关注
     *
     * @param target
     */
    private void relpyUnSubscribe(String target) {
        Customer currents = customerService.findByOpenId(target);
        customerService.delete(currents);
    }

    /**
     * 关注
     *
     * @param target
     */
    private void replySubscribe(String target) {
        User user = WechatHaoHelper.getInstance().getUserDetail(target);
        Customer newCustomer = new Customer(user);
        customerService.save(newCustomer);
    }

    /**
     * 测试发送模板信息
     */
    private void testReplyMenuClickWithSendTemplate(String templateId, String target) {
        User user = WechatHaoHelper.getInstance()
                .getUserDetail(target);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm");
        String date = format.format(new Date());
        String remark = "请在预约时间前来我司咨询，如果成功签约，预约金将全额退回。如在预约当天未到场咨询，预约金将不予退回。";
        List<TemplateField> fields = Arrays.asList(
                new TemplateField("keyword1", user.getNickName()),
                new TemplateField("keyword2", "面部护理"),
                new TemplateField("keyword3", "100元"),
                new TemplateField("keyword4", date),
                new TemplateField("remark", remark));

//        String link = WechatHaoPayHelper.NGROK + "/wechat/order/pay";
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("https://open.weixin.qq.com/connect/oauth2/authorize");
//        buffer.append("?appid=" + WechatHaoHelper.APP_ID);
//        buffer.append("&redirect_uri=" + UrlEncoded.encodeString(link));
//        buffer.append("&response_type=code");
//        // buffer.append("&scope=" + "snsapi_base"); //   不弹出授权
//        buffer.append("&scope=" + "snsapi_userinfo"); // 弹出授权
//        buffer.append("&state=STATE#wechat_redirect");
        
        
        String link = WechatHaoPayHelper.NGROK + "/wechat/order/pay";
        String to = WechatHaoHelper.getInstance().getRedirectUrl(link);
        
        WechatHaoHelper.getInstance().replyXmlTemplate(target,
                //buffer.toString() 为认证页面 进行微信授权 link则为测试用普通跳转
                // templateId, fields, buffer.toString());
                templateId, fields, to);
    }

    /**
     * 按钮回复
     *
     * @param event
     * @return
     */
    private String replyMenuClick(RecvMenuEvent event) {
        if ("TEMPLATE".equals(event.getEventKey())) {
            // String templateId ="GPOvvmzEo68JSm2p6Hw3B33CUlH3LmCfjbYm9Uo-xqo";
            String templateId = "p_lDlEJK9YgJW7oSaCi5MeSa6FmCErK8vAq_eczs2-M";
            String target = event.getToUserName();
            testReplyMenuClickWithSendTemplate(templateId, target);
        }
        //此处可通过 event.getEventKey() 来调整对应该状况的返回内容  此处默认返回图文信息
        List<Article> articles = Arrays.asList(
                new Article("图文标题1", "图文描述",
                        "http://mpic.tiankong.com/c5f/9d6/c5f9d67fbfd7e5deb25e297aa2da00a5/east-A21-559966.jpg@360h",
                        "链接"),
                new Article("图文标题2", "图文描述", "图片链接", "https://www.baidu.com/"),
                new Article("图文标题3", "图文描述", "图片链接", "链接"),
                new Article("图文标题4", "图文描述", "图片链接", "链接"));
        return WechatHaoHelper.getInstance().replyXmlArticles(event,
                articles);
    }

    private String relpyText(RecvMessage message, String content) {
        return WechatHaoHelper.getInstance().replyXmlText(message, content);
    }

    /**
     * 根据业务需求具体完善 图片 视频 音频等扩展 现只实现文字类回复以及 模板推送
     *
     * @param xml
     * @param wechatBase
     * @return
     */
    private String receiveMessage(String xml, WechatBase wechatBase) {
        RecvMessage message = WechatHaoHelper.getInstance().parseMessageXml(xml);
        return parseResultXml(message, wechatBase);
        // RecvImageMessage);
        // RecvVoiceMessage);
        // RecvVideoMessage);
        // RecvShortVideoMessage);
        // RecvLocationMessage);
        // RecvLinkMessage);
        // RecvSubscribeEvent);
        // RecvSubscribeEvent);
        // RecvScanEvent);
        // RecvLocationEvent);
        // RecvMenuEvent);
    }

}
