package com.xquant.component.common.excelbuilder.attribute;

import com.alibaba.fastjson.JSON;

/**
 * 代表类型为SIMPLE_SQL类型的参数定义
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月21日
 */
public class SimpleSQLRowAttribute {

	private Integer index = 0;

	private boolean beforeTest = true;

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

	public boolean getBeforeTest() {
		return beforeTest;
	}

	public void setBeforeTest(boolean beforeTest) {
		this.beforeTest = beforeTest;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
