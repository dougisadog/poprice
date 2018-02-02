package com.alipay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.io.Resources;

public class AlipaySetting {
	private static final String SETTINGS_FILE_NAME = "alipay.properties";

	private static final Log log = LogFactory.getLog(AlipaySetting.class);

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
	
	public static String getMerchantId() {
		return properties.getProperty("alipay.merchantId");
	}

	public static String getSecret() {
		return properties.getProperty("alipay.secret");
	}

	public static String getWapReturnUrl() {
		return properties.getProperty("alipay.wapReturnUrl");
	}
	
	public static String getWebReturnUrl() {
		return properties.getProperty("alipay.webReturnUrl");
	}
	
	public static String getRefundNotifyUrl() {
		return properties.getProperty("alipay.refundNotifyUrl");
	}
	
	public static String getPayNotifyUrl() {
		return properties.getProperty("alipay.payNotifyUrl");
	}

}
