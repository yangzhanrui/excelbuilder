package com.xquant.component.common.excelbuilder.reader;

import org.apache.poi.ss.usermodel.Row;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.BaseJsonRowAttribute;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4BaseJson;

/**
 * BASE_JSON类Row解析器
 * 
 * <li>第一列 ： 测试的案例次序 C代表公共  从1开始 如: 1</li>
 * <li>第二列 ： BASE_JSON</li>
 * <li>第三列 ： 属性信息</li>
 * <li>第四列 ： json数据</li>
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public class ExeclRowReader4BaseJson extends AbstractExeclRowReader {

	@Override
	public ExcelRowHolder read(Row row) {
		RowHolder4BaseJson excelRowHolder = new RowHolder4BaseJson();
		setRowAndTestUnit(excelRowHolder, row);
		excelRowHolder.setBaseJsonRowAttribute(JSON.parseObject(getRowAttibute(row), BaseJsonRowAttribute.class));
		excelRowHolder.setJsonId(excelRowHolder.getBaseJsonRowAttribute().getJsonId());
		excelRowHolder.setJsonStr(getRowData(row));
		checkObjectIfAttributeNull(excelRowHolder);
		return excelRowHolder;
	}

	@Override
	public boolean support(Row row) {
		return RowTypeEnum.BASE_JSON.equals(getRowtype(row));
	}

	private void checkObjectIfAttributeNull(RowHolder4BaseJson excelRowHolder) {

	}
}
