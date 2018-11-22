package com.xquant.component.common.excelbuilder.resolver;

import com.xquant.component.common.excelbuilder.attribute.SimpleSQLRowAttribute;
import com.xquant.component.common.excelbuilder.context.ExcelReaderContext;
import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.dto.SqlRowDefinition;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4SimpleSql;
import com.xquant.component.common.excelbuilder.util.PlaceHolderUtil;
import com.xquant.component.common.excelbuilder.util.SqlStringUtil;

public class ExcelRowHolderResolver4SimpleSql implements ExcelRowHolderResolver {

	@Override
	public RowDefinition resolve(ExcelRowHolder excelRowHolder) {
		RowHolder4SimpleSql rowHolder = (RowHolder4SimpleSql) excelRowHolder;
		SimpleSQLRowAttribute simpleSQLRowAttribute = rowHolder.getSimpleSQLRowAttribute();
		SqlRowDefinition definition = new SqlRowDefinition();
		definition.setTestCaseId(rowHolder.getCaseId());
		definition.setBeforeTest(simpleSQLRowAttribute.getBeforeTest());
		definition.setIndex(simpleSQLRowAttribute.getIndex());
		String sql =  SqlStringUtil.stripToExecutableSql(rowHolder.getSql());
		if (PlaceHolderUtil.hasPlaceHolder(sql)) {
			sql = PlaceHolderUtil.resolvePlaceHolder(sql, ExcelReaderContext.getBaseJsonDataMap(), rowHolder.getCaseId());
		}
		definition.setSql(sql);
		return definition;
	}

	@Override
	public boolean support(ExcelRowHolder excelRowHolder) {
		return RowTypeEnum.SIMPLE_SQL.equals(excelRowHolder.rowType());
	}

}
