package com.xquant.component.common.excelbuilder.reader;

import org.apache.poi.ss.usermodel.Row;

import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;

/**
 * excel row解析器
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public interface IExeclRowReader {

	/**
	 * 是否支持当前行解析器
	 * 
	 * @param row
	 * @return
	 */
	boolean support(Row row);

	/**
	 * 将excel中的行信息储存到ExcelRowHolder对象
	 * 
	 * @param row
	 * @return
	 */
	ExcelRowHolder read(Row row);
}
