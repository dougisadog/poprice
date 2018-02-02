package com.qq.wechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.hao0.common.date.Dates;
import me.hao0.wepay.core.Wepay;
import me.hao0.wepay.core.WepayBuilder;
import me.hao0.wepay.model.pay.JsPayRequest;
import me.hao0.wepay.model.pay.JsPayResponse;
import me.hao0.wepay.model.pay.QrPayRequest;
import me.hao0.wepay.model.refund.RefundApplyRequest;
import me.hao0.wepay.model.refund.RefundApplyResponse;

public class WechatHaoPayHelper {

	private static WechatHaoPayHelper wechatHaoPayHelper;
	
	private Wepay wepay;
	
    
    private final static String APP_ID = WechatSetting.getAppId();
    
    private final static String APP_KEY = WechatSetting.getPayKey();
    
    private final static String MCH_ID = WechatSetting.getMCH();
    
    public final static String NGROK = "http://5ac9d583.ngrok.io";
    
    private final static String PAY_NOTIFY_URL = WechatSetting.getPayNotifyUrl();
    
	public static WechatHaoPayHelper getInstance() {
		if (null == wechatHaoPayHelper)
			wechatHaoPayHelper = new WechatHaoPayHelper();
		return wechatHaoPayHelper;
	}
    
    
	private WechatHaoPayHelper() {
		Path path = Paths.get("src/main/webapp/static/apiclient_cert.p12");
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(data.toString());
		wepay = WepayBuilder.newBuilder(APP_ID, APP_KEY, MCH_ID)
		         .certPasswd(MCH_ID)
		         .certs(data)
		         .build();
		
	};
	
    /**
     * 动态二维码支付
     * @param orderNumber 订单号
     * @return 二维码链接
     */
    public String qrPay(String orderNumber){
        QrPayRequest request = new QrPayRequest();
        request.setBody("测试订单");
        request.setClientId("127.0.0.1");
        request.setTotalFee(1);
        request.setNotifyUrl(PAY_NOTIFY_URL);
        request.setOutTradeNo(orderNumber);
        request.setTimeStart(Dates.now("yyyyMMddHHmmss"));
        return wepay.pay().qrPay(request);
    }
    
    /**
     *  公众号支付
     *	必选参数
     *  put(payParams, WepayField.BODY, request.getBody());
        put(payParams, WepayField.OUT_TRADE_NO, request.getOutTradeNo());
        put(payParams, WepayField.TOTAL_FEE, request.getTotalFee() + "");
        put(payParams, WepayField.SPBILL_CREATE_IP, request.getClientId());
        put(payParams, WepayField.NOTIFY_URL, request.getNotifyUrl());
        put(payParams, WepayField.FEE_TYPE, request.getFeeType().type());
        put(payParams, WepayField.TIME_START, request.getTimeStart());
        
                       可选参数
        putIfNotEmpty(payParams, WepayField.DEVICE_INFO, request.getDeviceInfo());
        putIfNotEmpty(payParams, WepayField.ATTACH, request.getAttach());
        putIfNotEmpty(payParams, WepayField.DETAIL, request.getDetail());
        putIfNotEmpty(payParams, WepayField.GOODS_TAG, request.getGoodsTag());
        putIfNotEmpty(payParams, WepayField.TIME_EXPIRE, request.getTimeExpire());
        putIfNotEmpty(payParams, WepayField.LIMIT_PAY, request.getLimitPay());
     * @param request
     * @return pkg 对应 就是前端调用的package
     */
    public JsPayResponse jsPay(JsPayRequest request){
    	request.setNotifyUrl(PAY_NOTIFY_URL);
        return wepay.pay().jsPay(request);
    }

    /**
     * 校验签名
     * @param params 参数(包含sign)
     * @return 校验成功返回true，反之false
     */
    public Boolean verifySign(Map<String, ?> params){
        return wepay.notifies().verifySign(params);
    }

    /**
     * 通知成功
     */
    public String notifyOk(){
        return wepay.notifies().ok();
    }

    /**
     * 通知不成功
     * @param errMsg 错误消息
     */
    public String notifyNotOk(String errMsg){
        return wepay.notifies().notOk(errMsg);
    }

    public RefundApplyResponse refundApply(String orderNumber){
        RefundApplyRequest req = new RefundApplyRequest();
        req.setOutTradeNo(orderNumber);
        req.setOutRefundNo(orderNumber);
        req.setTotalFee(1);
        req.setRefundFee(1);
        req.setOpUserId(wepay.getMchId());
        return wepay.refund().apply(req);
    }
    
    public Map<String, String> sign(String url) {
    	String jsapi_ticket = WechatHaoHelper.getInstance().getTicket();
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
