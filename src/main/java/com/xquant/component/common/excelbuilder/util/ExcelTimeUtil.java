package com.xquant.component.common.excelbuilder.util;

public class ExcelTimeUtil {
	
   private static final String BEFORE_SIGN = "BEFORE";	
	/**
	 * 根据excel中execTime判断是否测试之前执行
	 * @param execTime
	 * @return
	 */
	public static boolean beforeTest(String execTime) {
		return execTime.toUpperCase().contains(BEFORE_SIGN);
	}

}
