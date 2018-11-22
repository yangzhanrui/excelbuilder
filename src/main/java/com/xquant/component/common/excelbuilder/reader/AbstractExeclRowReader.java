package com.xquant.component.common.excelbuilder.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.xquant.component.common.excelbuilder.enums.ColumnNumEnum;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.exception.XccExcelNullCellException;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.util.CellValueConvertorUtil;

/**
 * excel单元格读取抽象类
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public abstract class AbstractExeclRowReader implements IExeclRowReader {

	/**
	 * 设置当前单元格对象以及属于的测试单元
	 * 
	 * @param rowHolder
	 * @param row
	 */
	protected void setRowAndTestUnit(ExcelRowHolder rowHolder, Row row) {
		double numericCellValue = row.getCell(ColumnNumEnum.CASE_ID.getValue()).getNumericCellValue();
		rowHolder.setRow(row);
		rowHolder.setCaseId(CellValueConvertorUtil.convert(numericCellValue));
	}

	/**
	 * 获取行类型
	 * 
	 * @param row
	 * @return
	 */
	protected RowTypeEnum getRowtype(Row row) {
		return RowTypeEnum.getEnumName(row.getCell(ColumnNumEnum.ROW_TYPE.getValue()).getStringCellValue());
	}

	/**
	 * 获取行属性
	 * 
	 * @param row
	 * @return
	 */
	protected String getRowAttibute(Row row) {
		Cell cell = row.getCell(ColumnNumEnum.ROW_ATTRIBUTE.getValue());
		// 默认返回为String excel限定为String类型数据 如果不填写 则为【1.行号】 用于后面排序
		if (cell == null && RowTypeEnum.SIMPLE_SQL.equals(getRowtype(row))) {
			return "\"index\":" + 100 + row.getRowNum();
		}
		checkCellIfNull(row, cell, ColumnNumEnum.ROW_ATTRIBUTE.getValue());
		return cell.getStringCellValue();
	}

	/**
	 * 获取行数据
	 * 
	 * @param row
	 * @return
	 */
	protected String getRowData(Row row) {
		Cell cell = row.getCell(ColumnNumEnum.ROW_DATA.getValue());
		checkCellIfNull(row, cell, ColumnNumEnum.ROW_DATA.getValue());
		return cell.getStringCellValue();
	}

	/**
	 * 获取行标识
	 * 
	 * @param row
	 * @return
	 */
	protected String getRemarkData(Row row) {
		Cell cell = row.getCell(ColumnNumEnum.ROW_REMARK.getValue());
		checkCellIfNull(row, cell, ColumnNumEnum.ROW_REMARK.getValue());
		return cell.getStringCellValue();
	}

	protected Cell getRemarkCell(Row row) {
		Cell cell = row.getCell(ColumnNumEnum.ROW_REMARK.getValue());
		checkCellIfNull(row, cell, ColumnNumEnum.ROW_REMARK.getValue());
		return cell;
	}

	private void checkCellIfNull(Row row, Cell cell, int cellnum) {
		if (cell == null) {
			throw new XccExcelNullCellException(row, cellnum);
		}
	}

}
