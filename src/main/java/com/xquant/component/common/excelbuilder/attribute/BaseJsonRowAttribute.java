package com.xquant.component.common.excelbuilder.attribute;

import com.alibaba.fastjson.JSON;

/**
 * 代表BASE_JSON类型的参数定义
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public class BaseJsonRowAttribute {

	private String jsonId;
	private String includeJsonId;
	private String tableName;
	private String classType;
	private boolean beforeTest = true;
	private Integer index;

	public String getJsonId() {
		return jsonId;
	}

	public void setJsonId(String jsonId) {
		this.jsonId = jsonId;
	}

	public String getIncludeJsonId() {
		return includeJsonId;
	}

	public void setIncludeJsonId(String includeJsonId) {
		this.includeJsonId = includeJsonId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public boolean getBeforeTest() {
		return beforeTest;
	}

	public void setBeforeTest(boolean beforeTest) {
		this.beforeTest = beforeTest;
	}

	public Integer getIndex() {
		if (index == null) {
			return 100;
		}
		if (index < 100) {
			return index * 100;
		}
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
