package com.xquant.component.common.excelbuilder.dto;

import com.alibaba.fastjson.JSON;

public class AfterCheckRowDefiniton extends RowDefinition {

	private boolean isSqlType = false;
	
	private String sql;

	private String checkJsonStr;
	
	private int rowNum;
	
	private int columnNum;
	
	private Class<?> classType;
	
	private Object object;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCheckJsonStr() {
		return checkJsonStr;
	}

	public void setCheckJsonStr(String checkJsonStr) {
		this.checkJsonStr = checkJsonStr;
	}
	
	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	
	public boolean getSqlType() {
		return isSqlType;
	}

	public void setSqlType(boolean isSqlType) {
		this.isSqlType = isSqlType;
	}

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
