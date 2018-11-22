package com.xquant.component.common.excelbuilder.dto;

import com.alibaba.fastjson.JSON;

public class ObjRowDefinition extends RowDefinition {

	private Class<?> classType;

	private Object obj;
	
	private Boolean beforeTest;

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
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
