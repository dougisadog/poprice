package com.qq.wechat.template;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {
	
	private static final String BASE_PATTERN = "[{][ ]?[{][a-zA-Z0-9]+[.][D][A][T][A][}][ ]?[}]";

	/**
	 * 正则匹配 	{{first.DATA}}
				客户姓名：{{keyword1.DATA}}
				预约项目：{{keyword2.DATA}}
				预约金额：{{keyword3.DATA}}
				预约时间：{{keyword4.DATA}}
				{{remark.DATA}}
		类型模板
		拼装 参数名称和key名
	 * @param content 匹配内容
	 * @return
	 */
	public static List<TemplateParam> parseTemplateContent(String content) {
		Pattern pattern = Pattern.compile(BASE_PATTERN);
		Matcher matcher = pattern.matcher(content);
		int currentStart = 0;
		int currentEnd = 0;
		List<TemplateParam> params = new ArrayList<>();
		while (matcher.find()) {
			TemplateParam param = new TemplateParam();
			
			currentEnd = matcher.start();
			if (currentEnd > currentStart) {
				String result = content.substring(currentStart, currentEnd);
				result = result.trim();
//				System.out.println("result:" + result);
				param.setTitle(result);
			}
			currentStart = matcher.end();
			
			String data = content.substring(matcher.start(), matcher.end());
			data = parseData(data);
			if (data.length() == 0) continue;
			
			if (null == param.getTitle() || param.getTitle().length() == 0) {
				param.setTitle(data);
			}
			param.setKey(data);
//			System.out.println("data:" + data);
			params.add(param);
		}
		return params;
	}
	
	/**
	 * 将{{keyword1.DATA}}类型内容格式化 剥取keyword1 参数名称
	 * @param data
	 * @return
	 */
	public static String parseData(String data) {
		String result = "";
		data = data.trim().replace(" ", "");
		String temp = data.split("\\.")[0];
		result = temp.substring(2, temp.length());
		return result;
	}
}
