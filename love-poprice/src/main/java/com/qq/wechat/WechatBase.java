package com.qq.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.qq.wechat.utils.RestUtils;

/**
 * 基础微信server的通讯参数
 * @author Doug
 *
 */
public class WechatBase {

	public WechatBase(HttpServletRequest httpRequest) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		if (m.containsKey("msg_signature")) {
			msgSignature = m.get("msg_signature")[0];
		}
		if (m.containsKey("timestamp")) {
			timestamp = m.get("timestamp")[0];
		}
		if (m.containsKey("nonce")) {
			nonce = m.get("nonce")[0];
		}
		if (m.containsKey("echostr")) {
			echostr = m.get("echostr")[0];
		}
		if (m.containsKey("encrypt_type")) {
			encryptType = m.get("encrypt_type")[0];
		}
		if (m.containsKey("openid")) {
			openId = m.get("openid")[0];
		}
		if (m.containsKey("signature")) {
			signature = m.get("signature")[0];
		}
		content = RestUtils.getInputContent(httpRequest);
	}
	
	private String echostr; // url验证信息回复
	
	private String msgSignature; // ase加密key

	private String timestamp; // 时间戳

	private String encryptType;// 加密类型

	private String nonce; // 随机数

	private String signature; // 加密验证签名

	private String openId; // 请求方openid
	
	private String content; // 请求流内容

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMsgSignature() {
		return msgSignature;
	}

	public void setMsgSignature(String msgSignature) {
		this.msgSignature = msgSignature;
	}

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOpenid() {
		return openId;
	}

	public void setOpenid(String openid) {
		this.openId = openid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
