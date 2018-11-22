package com.xquant.component.common.excelbuilder.enums;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * excel row 类型
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月19日
 */
public enum RowTypeEnum {
	/**
	 * 基础测试参数类型 主要用于提供测试时使用的各种参数
	 */
	BASE_JSON("BASE_JSON"),
	/**
	 * 测试过程所使用SQL
	 */
	SIMPLE_SQL("SIMPLE_SQL"),
	/**
	 * 结果测试对象或sql
	 */
	AFTER_CHECK("AFTER_CHECK");
	
	private RowTypeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}

	private static final Logger logger = LoggerFactory.getLogger(RowTypeEnum.class);

	public static Map<String, RowTypeEnum> map = new HashMap<String, RowTypeEnum>();

	static {
		RowTypeEnum[] _arr = RowTypeEnum.values();
		for (int i = 0, _len = _arr.length; i < _len; i++) {
			RowTypeEnum temp = _arr[i];
			map.put(temp.getValue(), temp);
		}
	}

	public static RowTypeEnum getEnumName(String value) {
		if (StringUtils.isBlank(value)) {
			if (logger.isWarnEnabled()) {
				logger.warn(RowTypeEnum.class.getName() + " pass argument null or \"\" in getEnumName method and ignored ");
			}
			return null;
		}
		if (map.containsKey(value)) {
			return map.get(value);
		} else {
			throw new IllegalArgumentException(RowTypeEnum.class.getName() + " : 不支持的value值！" + value);
		}
	}

}
