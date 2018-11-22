package com.xquant.component.common.excelbuilder.rowholder;

import org.apache.poi.ss.usermodel.Row;

import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;

public abstract class ExcelRowHolder {

	private Row row;

	private String caseId;

	public abstract RowTypeEnum rowType();

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

}
