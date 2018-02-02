package com.qq.wechat.utils;

import java.io.UnsupportedEncodingException;

public class CharUtils {

    /**
     * 替换xml特殊字符，
     * 过滤非法字符   HJX
     * @param str
     * @return
     */
	public static String charValidation(String str) {
		try {
			str = new String(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
				int c=str.codePointAt(i);
				if (c<0x0000||c>0xffff) {
					//为非法字符
				    continue;
				}
				buffer.append(str.charAt(i));
		}
		return buffer.toString();
	}
}
