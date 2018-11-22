package com.xquant.component.common.excelbuilder.util;

public class CellValueConvertorUtil {

	/**
	 * excel中的数字类型获取出来为double 此处获取正整数
	 * 
	 * @param numericCellValue
	 * @return
	 */
	public static String convert(double numericCellValue) {

//		if (!"\\d+".equals(String.valueOf(numericCellValue))) {
//			throw new RuntimeException("double value [" + numericCellValue + "] can not cast to int");
//		}
		String doubleStr = String.valueOf(numericCellValue);
		if(doubleStr.contains(".")) {		
			doubleStr = doubleStr.substring(0, doubleStr.lastIndexOf("."));
		}
		return doubleStr;
	}
	
	public static void main(String[] args) {
		System.out.println(convert(0.00)); 
		System.out.println(convert(1.00)); 
	}

}
