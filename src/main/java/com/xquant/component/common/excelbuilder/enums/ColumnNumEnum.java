package com.xquant.component.common.excelbuilder.enums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ColumnNumEnum {

	CASE_ID(0), ROW_TYPE(1), ROW_ATTRIBUTE(2), ROW_DATA(3), ROW_REMARK(4);

	private ColumnNumEnum(Integer value) {
		this.value = value;
	}

	private Integer value;

	public Integer getValue() {
		return this.value;
	}

	private static final Logger logger = LoggerFactory.getLogger(ColumnNumEnum.class);

	public static Map<Integer, ColumnNumEnum> map = new HashMap<Integer, ColumnNumEnum>();

	static {
		ColumnNumEnum[] _arr = ColumnNumEnum.values();
		for (int i = 0, _len = _arr.length; i < _len; i++) {
			ColumnNumEnum temp = _arr[i];
			map.put(temp.getValue(), temp);
		}
	}

	public static ColumnNumEnum getEnumName(Integer value) {
		if (value == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(ColumnNumEnum.class.getName() + " pass argument null in getEnumName method and ignored ");
			}
			return null;
		}
		if (map.containsKey(value)) {
			return map.get(value);
		} else {
			throw new IllegalArgumentException(ColumnNumEnum.class.getName() + " : 不支持的value值！" + value);
		}
	}
}
