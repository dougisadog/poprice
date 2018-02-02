package com.alipay;

import me.hao0.alipay.core.Alipay;
import me.hao0.alipay.core.AlipayBuilder;
import me.hao0.alipay.model.pay.WapPayDetail;
import me.hao0.alipay.model.pay.WebPayDetail;
import me.hao0.alipay.model.refund.RefundDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qq.wechat.WechatHaoPayHelper;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 23/11/15
 */
public class AlipayHelper {

    private String merchantId = AlipaySetting.getMerchantId();

    private String secret = AlipaySetting.getSecret();

    private String payNotifyUrl = AlipaySetting.getPayNotifyUrl();

    private String refundNotifyUrl = AlipaySetting.getRefundNotifyUrl();

    private String webReturnUrl = AlipaySetting.getWebReturnUrl();

    private String wapReturnUrl = AlipaySetting.getWapReturnUrl();
    
    private static AlipayHelper alipayHelper;

    private Alipay alipay;
    
	public static AlipayHelper getInstance() {
		if (null == alipayHelper)
			alipayHelper = new AlipayHelper();
		return alipayHelper;
	}

    private AlipayHelper(){
        alipay = AlipayBuilder
                    .newBuilder(merchantId, secret)
                    .build();

        System.err.println(alipay);
    }
    
    public Map<String, String> getParams(WebPayDetail detail) {
    	 detail.setNotifyUrl(payNotifyUrl);
         detail.setReturnUrl(webReturnUrl);
    	return alipay.pay().buildWebPayParams(detail);
    }

    /**
     * web支付
     */
    public String webPay(WebPayDetail detail){
        detail.setNotifyUrl(payNotifyUrl);
        detail.setReturnUrl(webReturnUrl);
        return alipay.pay().webPay(detail);
    }

    /**
     * wap支付
     */
    public String wapPay(WapPayDetail detail) {
        detail.setNotifyUrl(payNotifyUrl);
        detail.setReturnUrl(wapReturnUrl);
        return alipay.pay().wapPay(detail);
    }

    /**
     * MD5验证
     */
    public Boolean notifyVerifyMd5(Map<String, String> params){
        return alipay.verify().md5(params);
    }

    /**
     * 退款申请
     */
    public Boolean refund(RefundDetail detail){
        detail.setNotifyUrl(refundNotifyUrl);
        return alipay.refund().refund(detail);
    }
}
