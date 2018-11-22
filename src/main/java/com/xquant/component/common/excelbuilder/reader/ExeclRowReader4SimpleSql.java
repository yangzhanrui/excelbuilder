package com.xquant.component.common.excelbuilder.reader;

import org.apache.poi.ss.usermodel.Row;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.SimpleSQLRowAttribute;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4SimpleSql;

public class ExeclRowReader4SimpleSql extends AbstractExeclRowReader {

	@Override
	public ExcelRowHolder read(Row row) {

		RowHolder4SimpleSql excelRowHolder = new RowHolder4SimpleSql();
		setRowAndTestUnit(excelRowHolder, row);
		excelRowHolder.setSimpleSQLRowAttribute(JSON.parseObject(getRowAttibute(row), SimpleSQLRowAttribute.class));
		excelRowHolder.setSql(getRowData(row));
		checkObjectIfAttributeNull(excelRowHolder);
		return excelRowHolder;
	}
	
	@Override
	public boolean support(Row row) {
		return RowTypeEnum.SIMPLE_SQL.equals(getRowtype(row));
	}

	private void checkObjectIfAttributeNull(RowHolder4SimpleSql excelRowHolder) {
			
	}


}
