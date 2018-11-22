package com.xquant.component.common.excelbuilder.api;

import java.util.HashMap;
import java.util.Map;

public class XccJunitBeforeTestObjContext {

	private static ThreadLocal<Map<Class<?>, Object>> CONTEXT_OBJ = new ThreadLocal<Map<Class<?>, Object>>();

	public static void setObject(Class<?> type, Object obj) {
		if (CONTEXT_OBJ.get() == null) {
			CONTEXT_OBJ.set(new HashMap<Class<?>, Object>());
		}
		CONTEXT_OBJ.get().put(type, obj);
	}

	public static void addMap(Map<Class<?>, Object> map) {
		if (CONTEXT_OBJ.get() == null) {
			CONTEXT_OBJ.set(new HashMap<Class<?>, Object>());
		}
		CONTEXT_OBJ.get().putAll(map);
	}

	public static Object getByType(Class<?> type) {
		if (CONTEXT_OBJ.get() == null) {
			return null;
		}
		return CONTEXT_OBJ.get().get(type);
	}

}