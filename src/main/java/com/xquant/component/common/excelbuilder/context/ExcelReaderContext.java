package com.xquant.component.common.excelbuilder.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import com.xquant.component.common.excelbuilder.rowholder.RowHolder4BaseJson;
import com.xquant.component.common.excelbuilder.util.JsonStringUtils;

/**
 * 记录excel读取上下文
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public class ExcelReaderContext {

	/**
	 * 保存BASE_JSON元素
	 */
	private static ThreadLocal<Map<String, RowHolder4BaseJson>> BASE_JSON_HOLDER_MAP = new ThreadLocal<Map<String, RowHolder4BaseJson>>();

	/**
	 * 保存每个BASE_JSON的json转换后的map
	 */
	private static ThreadLocal<Map<String, String>> BASE_JSON_HOLDER_DATA_MAP = new ThreadLocal<Map<String, String>>();

	/**
	 * 要包含其他BASE_JSON的map
	 */
	private static ThreadLocal<Set<RowHolder4BaseJson>> BASE_JSON_HOLDER_INCLUDE_OTHER = new ThreadLocal<Set<RowHolder4BaseJson>>();

	public static void registerHoldersIncludeOther(RowHolder4BaseJson baseJsonHolderBaseJson) {
		if (BASE_JSON_HOLDER_INCLUDE_OTHER.get() == null) {
			BASE_JSON_HOLDER_INCLUDE_OTHER.set(new HashSet<RowHolder4BaseJson>());
		}
		BASE_JSON_HOLDER_INCLUDE_OTHER.get().add(baseJsonHolderBaseJson);
	}

	public static Set<RowHolder4BaseJson> getBaseJsonHolderIncludeOther() {
		if (BASE_JSON_HOLDER_INCLUDE_OTHER.get() == null) {
			return new HashSet<RowHolder4BaseJson>();
		}
		return BASE_JSON_HOLDER_INCLUDE_OTHER.get();
	}

	/**
	 * key = jsonId
	 * @param key
	 * @param jsonStr
	 */
	public static void addBaseJsonDataMap(String key, String jsonStr) {
		if (BASE_JSON_HOLDER_DATA_MAP.get() == null) {
			BASE_JSON_HOLDER_DATA_MAP.set(new HashMap<String, String>());
		}
		if (BASE_JSON_HOLDER_DATA_MAP.get().containsKey(key)) {
			throw new RuntimeException("当前jsonId{" + key + "}重复定义?不允许重复注册");
		}
		BASE_JSON_HOLDER_DATA_MAP.get().putAll(JsonStringUtils.toMapWithDefinePrefix(jsonStr, key));
	}

	/**
	 * 获取指定key值用于填充占位符
	 * 
	 * @param key
	 * @return
	 */
	public static String getBaseJsonDataViaKey(String key) {
		if (BASE_JSON_HOLDER_DATA_MAP.get() == null) {
			return null;
		}
		return BASE_JSON_HOLDER_DATA_MAP.get().get(key);
	}

	public static Map<String, String> getBaseJsonDataMap() {
		return BASE_JSON_HOLDER_DATA_MAP.get();
	}

	/**
	 * 进行注册
	 * 
	 * @param jsonId
	 * @param rowHolder
	 * @return
	 */
	public static RowHolder4BaseJson registerBaseJsonHolder(String jsonId, RowHolder4BaseJson rowHolder) {
		Assert.notNull(jsonId, "jsonId can not be null");
		Assert.notNull(rowHolder, "rowHolder can not be null");
		if (getBaseJsonRowHolder(jsonId) != null) {
			throw new RuntimeException("BaseCaseParmRowHolder with same jsonId is already exists");
		}
		if (BASE_JSON_HOLDER_MAP.get() == null) {
			BASE_JSON_HOLDER_MAP.set(new HashMap<String, RowHolder4BaseJson>());
		}
		return BASE_JSON_HOLDER_MAP.get().put(jsonId, rowHolder);
	}

	public static RowHolder4BaseJson registerOrReplaceBaseJsonHolder(String jsonId, RowHolder4BaseJson rowHolder) {
		Assert.notNull(jsonId, "jsonId can not be null");
		Assert.notNull(rowHolder, "rowHolder can not be null");
		if (BASE_JSON_HOLDER_MAP.get() == null) {
			BASE_JSON_HOLDER_MAP.set(new HashMap<String, RowHolder4BaseJson>());
		}
		return BASE_JSON_HOLDER_MAP.get().put(jsonId, rowHolder);
	}

	/**
	 * 根据caseId获取BaseCaseParmRowHolder对象
	 * 
	 * @param jsonId
	 * @return 不存在 则返回null
	 */
	public static RowHolder4BaseJson getBaseJsonRowHolder(String jsonId) {
		Assert.notNull(jsonId, "jsonId can not be null");
		if (BASE_JSON_HOLDER_MAP.get() == null) {
			return null;
		}
		return BASE_JSON_HOLDER_MAP.get().get(jsonId);
	}

	/**
	 * 获取所有注册的BaseCaseParmRowHolder
	 * 
	 * @return
	 */
	public static Collection<RowHolder4BaseJson> getAllRegisterBaseJsonHolder() {
		return BASE_JSON_HOLDER_MAP.get().values();
	}

	/**
	 * 清空上下文保存的holder
	 */
	public static void clearAllBaseJsonHolder() {
		BASE_JSON_HOLDER_MAP.remove();
		BASE_JSON_HOLDER_INCLUDE_OTHER.remove();
	}

	/**
	 * 清空用于解析占位符的parmMap
	 */
	public static void clearAllBaseJsonData() {
		BASE_JSON_HOLDER_DATA_MAP.remove();
	}

}
