package com.xquant.component.common.excelbuilder.api;

import java.util.HashMap;
import java.util.Map;

public class XccJunitReturnTestObjContext {

	private static ThreadLocal<Map<Class<?>, Object>> CONTEXT_OBJ = new ThreadLocal<Map<Class<?>, Object>>();

	public static void addReturnObj(Class<?> type, Object obj) {
		if (CONTEXT_OBJ.get() == null) {
			CONTEXT_OBJ.set(new HashMap<Class<?>, Object>());
		}
		CONTEXT_OBJ.get().put(type, obj);
	}
	
	public static Map<Class<?>, Object> getReturnMap(){
		if(CONTEXT_OBJ.get() == null) {
			return new HashMap<Class<?>, Object>();
		}
		return CONTEXT_OBJ.get();
	}


	public static Object getByType(Class<?> type) {
		if (CONTEXT_OBJ.get() == null) {
			return null;
		}
		return CONTEXT_OBJ.get().get(type);
	}

}