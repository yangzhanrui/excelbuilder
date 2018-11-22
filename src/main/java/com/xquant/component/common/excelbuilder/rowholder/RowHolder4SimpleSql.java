package com.xquant.component.common.excelbuilder.rowholder;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.SimpleSQLRowAttribute;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;

/**
 * excel中的测试初始化sql
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public class RowHolder4SimpleSql extends ExcelRowHolder {

	private SimpleSQLRowAttribute simpleSQLRowAttribute;
	
	private String sql;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public SimpleSQLRowAttribute getSimpleSQLRowAttribute() {
		return simpleSQLRowAttribute;
	}

	public void setSimpleSQLRowAttribute(SimpleSQLRowAttribute simpleSQLRowAttribute) {
		this.simpleSQLRowAttribute = simpleSQLRowAttribute;
	}

	@Override
	public RowTypeEnum rowType() {
		return RowTypeEnum.SIMPLE_SQL;
	}

	@Override
	public String toString() {
		RowHolder4SimpleSql rowHolder4SimpleSql = new RowHolder4SimpleSql();
		BeanUtils.copyProperties(this, rowHolder4SimpleSql, "row");
		return JSON.toJSONString(rowHolder4SimpleSql);
	}
}
