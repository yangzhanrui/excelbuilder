package com.xquant.component.common.excelbuilder.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xquant.component.common.excelbuilder.api.ResultOfJsonMapCompare;

public class JsonStringUtils {

	private static final String JSON_PREFIX = "{";
	private static final String JSON_SUBFIX = "}";
	private static final String JSON_KEY_VALUE_MIDDLE_REGEX = ":";
	private static final String INSERT_SQL_BEFORE = "INSERT INTO ";
	private static final String INSERT_SQL_VALUES = ") VALUES (";
	private static final String DOUBLE_QUOTATION = "\"";
	private static final String SINGLE_QUOTATION = "\'";
	private static final String COMMA = ",";
	private static final String LEFT_BRACKET = " (";
	private static final String RIGHT_BRACKET = ")";

	/**
	 * 合并两个json字符串
	 * 
	 * @param jsonStr1
	 * @param jsonStr2
	 * @return
	 */
	public static String merge(String jsonStr1, String jsonStr2) {
		StringBuilder builder = new StringBuilder();
		jsonStr1 = StringUtils.strip(jsonStr1).substring(0, jsonStr1.length() - 1);
		jsonStr2 = StringUtils.strip(jsonStr2).substring(1, jsonStr2.length());
		return builder.append(jsonStr1.trim()).append(",\r\n\t").append(jsonStr2.trim()).toString();
	}

	/**
	 * 将json字符串转成map
	 * 
	 * <li>json字符串如下:</li>
	 * <li>{ "firstName": "John", "spouse": { "firstName": "Mary" }, "addresses":
	 * [</li> { "description": "home" }, { "description": "work" } ] }</li>
	 * <li>转换为map格式如下:</li>
	 * <li>{firstName=John, addresses[0].description=home, spouse.firstName=Mary,
	 * addresses[1].description=work}</li>
	 * 
	 * @param jsonStr
	 *            json字符串
	 * @return 根据json字符串生成的map
	 */
	public static Map<String, String> toMap(String jsonStr) {
		if (StringUtils.isBlank(jsonStr)) {
			throw new RuntimeException("jsonStr can not be null");
		}
		return toMapWithPrefix(jsonStr, "");
	}

	/**
	 * 将json字符串转成map
	 * 
	 * <li>json字符串如下:</li>
	 * <li>{ "firstName": "John", "spouse": { "firstName": "Mary" }, "addresses":
	 * [</li> { "description": "home" }, { "description": "work" } ] }</li>
	 * <li>转换为map格式如下:</li>
	 * <li>{prefix.firstName=John, prefix.addresses[0].description=home,
	 * prefix.spouse.firstName=Mary, prefix.addresses[1].description=work}</li>
	 * 
	 * @param jsonStr
	 *            json字符串
	 * @return 根据json字符串生成的map
	 */
	public static Map<String, String> toMapWithDefinePrefix(String jsonStr, String prefix) {
		if (StringUtils.isBlank(jsonStr)) {
			throw new RuntimeException("jsonStr can not be null");
		}
		if (StringUtils.isBlank(prefix)) {
			throw new RuntimeException("caseId can not be null");
		}
		return toMapWithPrefix(jsonStr, prefix + ".");
	}

	private static Map<String, String> toMapWithPrefix(String jsonStr, String prefix) {
		Map<String, String> outMap = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSON.parseObject(jsonStr, Map.class);
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			Object value = map.get(key);
			if (String.class.isInstance(value)) {
				outMap.put(prefix + key, (String) value);
			}
			if (JSONObject.class.isInstance(value)) {
				outMap.putAll(toMapWithPrefix(value.toString(), prefix + key + "."));
			}
			if (JSONArray.class.isInstance(value)) {
				outMap.putAll(jsonArrayToMap((JSONArray) value, key, prefix));
			}
		}
		return outMap;
	}

	private static Map<String, String> jsonArrayToMap(JSONArray value, String jsonArraykey, String prefix) {
		Map<String, String> outMap = new HashMap<String, String>();
		String[] splitJsons = StringUtils.strip(value.toString(), "[]").split("\\},\\{");
		int length = splitJsons.length;
		int index = 0;
		for (String json : splitJsons) {
			prefix = prefix + jsonArraykey + "[" + index + "].";
			if (index == 0) {
				json = json + "}";
			} else if (index == length - 1) {
				json = "{" + json;
			} else {
				json = "{" + json + "}";
			}
			outMap.putAll(toMapWithPrefix(json, prefix));
			index++;
		}
		return outMap;
	}

	/**
	 * 将普通json转变为insert sql语句
	 * 
	 * @param tableName
	 *            表名称
	 * @param jsonStr
	 *            json字符串
	 * @return
	 */
	public static String simpleJsonToInsertSql(String tableName, String jsonStr) {
		String afterStrip = jsonStr.substring(jsonStr.indexOf(JSON_PREFIX), jsonStr.lastIndexOf(JSON_SUBFIX));
		String[] splitJsonStrs = afterStrip.split(",");
		String[] keys = new String[splitJsonStrs.length];
		String[] values = new String[splitJsonStrs.length];
		int index = 0;
		for (String splitJsonStr : splitJsonStrs) {
			String[] keyAndValue = splitJsonStr.split(JSON_KEY_VALUE_MIDDLE_REGEX, 2);
			String key = keyAndValue[0];
			key = StringUtils.substringBetween(key, DOUBLE_QUOTATION, DOUBLE_QUOTATION);
			String value = keyAndValue[1].trim();
			if (value.startsWith(DOUBLE_QUOTATION)) {
				value = StringUtils.replaceChars(value, DOUBLE_QUOTATION, SINGLE_QUOTATION);
			}
			keys[index] = key;
			values[index] = value;
			index++;
		}
		StringBuilder builder = new StringBuilder(INSERT_SQL_BEFORE);
		builder.append(tableName);
		builder.append(LEFT_BRACKET);
		for (String key : keys) {
			builder.append(key).append(COMMA);
		}
		builder.deleteCharAt(builder.length() - 1).append(INSERT_SQL_VALUES);
		for (String value : values) {
			builder.append(value).append(COMMA);
		}
		builder.deleteCharAt(builder.length() - 1).append(RIGHT_BRACKET);
		return builder.toString();
	}

	/**
	 * 与jsonStr与map进行比较
	 * 
	 * @param simpleJsonStr
	 * @param map
	 * @return
	 */
	public static ResultOfJsonMapCompare compareSimpleJsonWithMap(String simpleJsonStr, Map<String, Object> map) {
		ResultOfJsonMapCompare result = new ResultOfJsonMapCompare();
		Map<String, String> jsonMap = JSON.parseObject(simpleJsonStr, Map.class);
		Set<String> keySet = jsonMap.keySet();
		for (String key : keySet) {
			String jsonValue = jsonMap.get(key);
			Object mapValue = map.get(key);
			if (mapValue == null) {
				result.addErrorInfo("查询返回结果集中对应的key{" + key + "}不存在");
			}
			Class<?> cls = mapValue.getClass();
			try {
				Object jsonValueObject = convertValueToRequiredType(jsonValue, cls);
				boolean flag = true;
				// 针对bigDecimal需要特殊处理
				if (BigDecimal.class == cls) {
					BigDecimal mapValueOfBigDeci = (BigDecimal) mapValue;
					BigDecimal jsonValueOfBigDeci = (BigDecimal) jsonValueObject;
					if (mapValueOfBigDeci.compareTo(jsonValueOfBigDeci) != 0) {
						flag = false;
					}
				}
				if (!mapValue.equals(jsonValueObject)) {
					flag = false;
				}
				if (!flag) {
					result.addErrorInfo("对应{" + key + "}：期望值{" + jsonValueObject + "},结果值{" + mapValue + "}");
				}
			} catch (Exception e) {
				result.addErrorInfo(e.getMessage());
			}
		}
		return result;
	}

	private static Object convertValueToRequiredType(Object value, Class<?> requiredType) {
		if (String.class == requiredType) {
			return value.toString();
		} else if (Number.class.isAssignableFrom(requiredType)) {
			if (value instanceof Number) {
				// Convert original Number to target Number class.
				return NumberUtils.convertNumberToTargetClass(((Number) value), (Class<Number>) requiredType);
			} else {
				// Convert stringified value to target Number class.
				return NumberUtils.parseNumber(value.toString(), (Class<Number>) requiredType);
			}
		} else {
			throw new IllegalArgumentException("Value [" + value + "] is of type [" + value.getClass().getName()
					+ "] and cannot be converted to required type [" + requiredType.getName() + "]");
		}
	}

	public static void main(String[] args) {

		String jsonStr1 = "{ \r\n" + "   \"EXHACC_ID\":\"222\",\r\n" + "   \"ACCNAME\":\"杭州衡泰测试帐户\"\r\n" + "}";
		String jsonStr2 = "{\r\n" + "  \"EXHACC\":\"98769876987607\"\r\n" + "}";

		System.out.println(merge(jsonStr1, jsonStr2));

		System.out.println(simpleJsonToInsertSql("TTRD_ACC_CASH_EXT", merge(jsonStr1, jsonStr2)));

		BigDecimal b1 = new BigDecimal("35");
		BigDecimal b2 = new BigDecimal("35.00");
		System.out.println(b1.compareTo(b2));
		System.out.println(b1.equals(b2));
		System.out.println((b1.doubleValue() == b2.doubleValue()));

		System.out.println("------------------------");

		BigDecimal b3 = new BigDecimal("35");
		BigDecimal b4 = new BigDecimal("35.01");
		System.out.println(b3.compareTo(b4));
		System.out.println(b3.equals(b4));
		System.out.println((b3.doubleValue() == b4.doubleValue()));
	}

}
