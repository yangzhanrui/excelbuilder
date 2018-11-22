package com.xquant.component.common.excelbuilder.rowholder;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.BaseJsonRowAttribute;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;

/**
 * 基础数据类型rowHolder
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月21日
 */
public class RowHolder4BaseJson extends ExcelRowHolder {

	/**
	 * case id
	 */
	private String jsonId;
	/**
	 * 限定定义
	 */
	private BaseJsonRowAttribute baseJsonRowAttribute;
	/**
	 * 从excel表中读取的json数据
	 */
	private String jsonStr;

	public String getJsonId() {
		return jsonId;
	}

	public void setJsonId(String jsonId) {
		this.jsonId = jsonId;
	}

	public BaseJsonRowAttribute getBaseJsonRowAttribute() {
		return baseJsonRowAttribute;
	}

	public void setBaseJsonRowAttribute(BaseJsonRowAttribute baseJsonRowAttribute) {
		this.baseJsonRowAttribute = baseJsonRowAttribute;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	@Override
	public RowTypeEnum rowType() {
		return RowTypeEnum.BASE_JSON;
	}
	
	@Override
	public String toString() {
		RowHolder4BaseJson rowHolder4BaseJson = new RowHolder4BaseJson();
		BeanUtils.copyProperties(this, rowHolder4BaseJson, "row");
		return JSON.toJSONString(rowHolder4BaseJson);
	}
}
