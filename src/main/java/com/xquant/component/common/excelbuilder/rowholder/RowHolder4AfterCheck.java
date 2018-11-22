package com.xquant.component.common.excelbuilder.rowholder;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.AfterCheckRowAttribute;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;

/**
 * 测试结束后数据库检查对象信息包装类
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public class RowHolder4AfterCheck extends ExcelRowHolder {

	private AfterCheckRowAttribute afterCheckRowAttribute;

	private String jsonStr;

	private Cell lastCell;

	public Cell getLastCell() {
		return lastCell;
	}

	public void setLastCell(Cell lastCell) {
		this.lastCell = lastCell;
	}

	public AfterCheckRowAttribute getAfterCheckRowAttribute() {
		return afterCheckRowAttribute;
	}

	public void setAfterCheckRowAttribute(AfterCheckRowAttribute afterCheckRowAttribute) {
		this.afterCheckRowAttribute = afterCheckRowAttribute;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	@Override
	public RowTypeEnum rowType() {
		return RowTypeEnum.AFTER_CHECK;
	}

	@Override
	public String toString() {
		RowHolder4AfterCheck rowHolder4AfterCheck = new RowHolder4AfterCheck();
		BeanUtils.copyProperties(this, rowHolder4AfterCheck, "row", "lastCell");
		return JSON.toJSONString(rowHolder4AfterCheck);
	}
}
