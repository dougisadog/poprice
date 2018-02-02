package com.qq.wechat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.io.Resources;

public class WechatSetting {
	private static final String SETTINGS_FILE_NAME = "wechat-base.properties";

	private static final Log log = LogFactory.getLog(WechatSetting.class);

	private static Properties properties = new Properties();
	
	static {
		load();
	}

	public static void load() {
		InputStream is = null;
		try {
			is = Resources.getResource(SETTINGS_FILE_NAME).openStream();
			properties.load(is);
		} catch (FileNotFoundException e) {
			log.warn("The settings file '" + SETTINGS_FILE_NAME + "' does not exist.");
		} catch (IOException e) {
			log.warn("Failed to load the settings from the file: " + SETTINGS_FILE_NAME);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}
	}
	
	public static boolean needAES() {
		return "on".equals(properties.getProperty("message.needAES"));
	}
	
	public static boolean isDebug() {
		return "on".equals(properties.getProperty("message.debug"));
	}

	public static String getAppId() {
		return properties.getProperty("wechat.appId");
	}

	public static String getAppSecret() {
		return properties.getProperty("wechat.appSecret");
	}


	public static String getAES() {
		return properties.getProperty("message.aes");
	}


	public static String getToken() {
		return properties.getProperty("message.token");
	}
	
	public static String getMCH() {
		return properties.getProperty("pay.mchId");
	}
	
	public static String getPayKey() {
		return properties.getProperty("pay.payKey");
	}
	
	public static String getPayNotifyUrl() {
		return properties.getProperty("pay.payNotifyUrl");
	}

}
