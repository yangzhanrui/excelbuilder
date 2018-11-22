package com.xquant.component.common.excelbuilder.resolver;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.AfterCheckRowAttribute;
import com.xquant.component.common.excelbuilder.context.ExcelReaderContext;
import com.xquant.component.common.excelbuilder.dto.AfterCheckRowDefiniton;
import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4AfterCheck;
import com.xquant.component.common.excelbuilder.util.PlaceHolderUtil;
import com.xquant.component.common.excelbuilder.util.SqlStringUtil;

public class ExcelRowHolderResolver4AfterCheck implements ExcelRowHolderResolver {

	@Override
	public RowDefinition resolve(ExcelRowHolder excelRowHolder) {
		RowHolder4AfterCheck rowHolder = (RowHolder4AfterCheck) excelRowHolder;
		AfterCheckRowAttribute afterCheckRowAttribute = rowHolder.getAfterCheckRowAttribute();
		AfterCheckRowDefiniton definition = new AfterCheckRowDefiniton();
		definition.setTestCaseId(rowHolder.getCaseId());
		
		if(StringUtils.isNotBlank(afterCheckRowAttribute.getSql())) {
			// 代表sql
			definition.setSqlType(true);
			String sql = SqlStringUtil.stripToExecutableSql(afterCheckRowAttribute.getSql());
			sql = PlaceHolderUtil.resolvePlaceHolder(sql, ExcelReaderContext.getBaseJsonDataMap(),rowHolder.getCaseId());
			definition.setSql(sql);
			definition.setCheckJsonStr(rowHolder.getJsonStr());
		}else {
			// 代表对象
			definition.setSqlType(false);
			definition.setClassType(afterCheckRowAttribute.getClsType());
			definition.setObject(JSON.parseObject(rowHolder.getJsonStr(), definition.getClassType()));
		}
		Cell lastCell = rowHolder.getLastCell();
		definition.setRowNum(lastCell.getRowIndex());
		definition.setColumnNum(lastCell.getColumnIndex());
		return definition;
	}

	@Override
	public boolean support(ExcelRowHolder excelRowHolder) {
		return RowTypeEnum.AFTER_CHECK.equals(excelRowHolder.rowType());
	}

}
