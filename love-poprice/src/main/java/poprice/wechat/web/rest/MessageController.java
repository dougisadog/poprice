package poprice.wechat.web.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.mns.AliyunMsgHelper;
import com.aliyun.mns.model.BatchSmsAttributes;

import poprice.wechat.util.RestUtils;

/**
 * 测试用
 *
 */
@RestController
@RequestMapping("/aliyun")
public class MessageController {

	@RequestMapping(value = "/message", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String deleteWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		String content = RestUtils.getInputContent(httpRequest);
		System.out.println(content);
		
		BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
     	// 3.1 设置发送短信的签名（SMSSignName）
     	batchSmsAttributes.setFreeSignName("爱上大米花");
     	// 3.2 设置发送短信使用的模板（SMSTempateCode）
     	batchSmsAttributes.setTemplateCode("SMS_63550001");
     	// 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
     	BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
     	smsReceiverParams.setParam("name", "输出param1");
     	smsReceiverParams.setParam("time", "输出param2");
     	// 3.4 增加接收短信的号码
     	batchSmsAttributes.addSmsReceiver("18341134983", smsReceiverParams);
		AliyunMsgHelper.getInstance().sendMsg(batchSmsAttributes);
		return "success";

	}
	
}
