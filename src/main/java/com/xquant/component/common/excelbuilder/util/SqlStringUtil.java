package com.xquant.component.common.excelbuilder.util;

public class SqlStringUtil {
	
	/**
	 * 去掉Sql字符串的前后空格以及最后的";"
	 * @param sql
	 * @return
	 */
	public static String stripToExecutableSql(String sql) {
		String trimSql = sql.trim();
		while(trimSql.endsWith(";")) {
			trimSql = trimSql.substring(0, trimSql.length()-1).trim();
		}
		return trimSql;
	}
	
	public static void main(String[] args) {
		String sql = "DELETE FROM TTRD_SET_INSTRUCTION WHERE INST_ID = '49073';\r\n;";
		System.out.println(stripToExecutableSql(sql));
	}

}
