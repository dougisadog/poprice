/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package poprice.wechat.util.persistence;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SearchFilter {
	private static DateTimeFormatter FORMAT_DATE = DateTimeFormat.forPattern("yyyy-MM-dd");

	private static DateTimeFormatter FORMAT_DATETIME = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * EQE是专门给枚举用的
	 *
	 */
	public enum Operator {
		EQ, LIKE, GT, LT, GTE, LTE, EQE, EQB
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			String value = (String)entry.getValue();
			if (StringUtils.isBlank(value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			Object newValue = value;

			//检测value是否是joda time类型，以D开头
			if (filedName.startsWith("D")) {
				try {

					filedName = filedName.substring(1);
					DateTimeFormatter fmt = null;
					boolean isDateOnly = value.length() <= 10;
					if (!Strings.isNullOrEmpty(value) && isDateOnly) {
						fmt = FORMAT_DATE;
					} else {
						fmt = FORMAT_DATETIME;
					}

					DateTime _value = DateTime.parse(value, fmt);
					if (operator == Operator.LTE && isDateOnly) { //LT不包括
						newValue = _value.plusDays(1);
					} else {
						newValue = _value;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);

				}
			}
			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, newValue);
			filters.put(key, filter);
		}

		return filters;
	}

	/**
	 * 直接使用web参数的值构建排序，可以多排序
	 *
	 * @param sortProp
	 * @param sortOrder
	 * @return
	 */
	public static Sort buildSort(String sortProp, String sortOrder) {
		if (StringUtils.isBlank(sortProp) || StringUtils.isBlank(sortOrder)) {
			return null;
		}
		String[] propArray = StringUtils.split(sortProp, ",");
		String[] orderArray = StringUtils.split(sortOrder, ",");
		if ((propArray.length != orderArray.length) || propArray.length == 0) {
			return null;
		}
		List<Sort.Order> orders = Lists.newArrayList();
		for (int i = 0; i < propArray.length; i++) {
			String prop = propArray[i];
			String order = orderArray[i];
			orders.add(new Sort.Order(Sort.Direction.fromString(order), prop));
		}
		Sort sort = new Sort(orders);
		return sort;
	}

	/**
	 * 创建动态查询条件组合.
	 * 不加入字段白名单或者黑名单参数了，因为在这个系统里面没啥用，并不影响数据以及显示结果。
	 * 未来可能会在大规模系统加入，因为有些字段一旦查询了可能导致性能问题。
	 */
	public static <T> Specification<T> buildSpecificationByParams(Map<String, Object> searchParams, final Class<T> entityClazz) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), entityClazz);
		return spec;
	}

	/**
	 * 创建动态查询条件组合.
	 */
	public static <T> Specification<T> buildSpecificationByFilters(Map<String, SearchFilter> filters, final Class<T> entityClazz) {
		Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), entityClazz);
		return spec;
	}
}
