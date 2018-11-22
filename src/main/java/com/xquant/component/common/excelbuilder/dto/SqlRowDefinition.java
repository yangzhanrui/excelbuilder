package com.xquant.component.common.excelbuilder.dto;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public class SqlRowDefinition extends RowDefinition {

	private String sql;

	private int index;

	private Boolean beforeTest;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Boolean getBeforeTest() {
		return beforeTest;
	}

	public void setBeforeTest(Boolean beforeTest) {
		this.beforeTest = beforeTest;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
