package com.xquant.component.common.excelbuilder.attribute;

import com.alibaba.fastjson.JSON;

/**
 * 代表BASE_JSON类型的参数定义
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月21日
 */
public class AfterCheckRowAttribute {

	private Class<?> clsType;

	private String sql;

	public Class<?> getClsType() {
		return clsType;
	}

	public void setClsType(Class<?> clsType) {
		this.clsType = clsType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
