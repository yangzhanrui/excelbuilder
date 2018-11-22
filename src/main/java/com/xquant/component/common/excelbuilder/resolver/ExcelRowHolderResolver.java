package com.xquant.component.common.excelbuilder.resolver;

import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;

/**
 * excelRowHolder的解析
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public interface ExcelRowHolderResolver {

	/**
	 * 进行解析
	 * @param excelRowHolder
	 */
	RowDefinition resolve(ExcelRowHolder excelRowHolder);
	
	/**
	 * 是否支持当前行解析器
	 * 
	 * @param row
	 * @return
	 */
	boolean support(ExcelRowHolder excelRowHolder);
	
}
