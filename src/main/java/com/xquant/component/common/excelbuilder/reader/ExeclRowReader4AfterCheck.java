package com.xquant.component.common.excelbuilder.reader;

import org.apache.poi.ss.usermodel.Row;

import com.xquant.component.common.excelbuilder.attribute.AfterCheckRowAttribute;
import com.xquant.component.common.excelbuilder.enums.ColumnNumEnum;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.exception.XccExcelNullCellException;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4AfterCheck;

/**
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月21日
 */
public class ExeclRowReader4AfterCheck extends AbstractExeclRowReader {

	public static final String AFTER_CHECK_SQL_SIGN = "SELECT";

	@Override
	public ExcelRowHolder read(Row row) {
		RowHolder4AfterCheck excelRowHolder = new RowHolder4AfterCheck();
		setRowAndTestUnit(excelRowHolder, row);
		String attribute = getRowAttibute(row).trim();
		AfterCheckRowAttribute afterCheckRowAttribute = new AfterCheckRowAttribute();
		if (attribute.toUpperCase().startsWith(AFTER_CHECK_SQL_SIGN)) {
			afterCheckRowAttribute.setSql(attribute);
		} else {
			try {
				Class<?> classType = Class.forName(attribute.trim());
				afterCheckRowAttribute.setClsType(classType);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"解析出错,行[" + (row.getRowNum() + 1) + "]列[" + (ColumnNumEnum.ROW_ATTRIBUTE.getValue() + 1) + "],对象类型" + attribute + "不存在?");
			}
		}
		excelRowHolder.setAfterCheckRowAttribute(afterCheckRowAttribute);
		excelRowHolder.setJsonStr(getRowData(row));
		excelRowHolder.setLastCell(getRemarkCell(row));
		checkObjectIfAttributeNull(excelRowHolder, row);
		return excelRowHolder;
	}

	private void checkObjectIfAttributeNull(RowHolder4AfterCheck excelRowHolder, Row row) {
		if (excelRowHolder.getLastCell() == null) {
			throw new XccExcelNullCellException(row, ColumnNumEnum.ROW_REMARK.getValue());
		}
	}

	@Override
	public boolean support(Row row) {
		return RowTypeEnum.AFTER_CHECK.equals(getRowtype(row));
	}

}
