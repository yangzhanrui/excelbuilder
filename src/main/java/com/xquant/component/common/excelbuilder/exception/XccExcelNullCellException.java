package com.xquant.component.common.excelbuilder.exception;

import org.apache.poi.ss.usermodel.Row;

/**
 * 空单元格异常
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月21日
 */
@SuppressWarnings("serial")
public class XccExcelNullCellException extends RuntimeException {

	private Row row;

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public XccExcelNullCellException(Row row, int cellnum) {
		super("解析excel出现异常!行" + (row.getRowNum() + 1) + ",列" + (cellnum + 1) + "不允许为空!");
		this.row = row;
	}

	public XccExcelNullCellException() {
		super();
	}

	public XccExcelNullCellException(String message) {
		super(message);
	}

	public XccExcelNullCellException(String message, Throwable cause) {
		super(message, cause);
	}

	public XccExcelNullCellException(Throwable cause) {
		super(cause);
	}

	protected XccExcelNullCellException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
