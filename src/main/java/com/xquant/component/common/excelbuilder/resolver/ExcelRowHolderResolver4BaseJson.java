package com.xquant.component.common.excelbuilder.resolver;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.attribute.BaseJsonRowAttribute;
import com.xquant.component.common.excelbuilder.dto.ObjRowDefinition;
import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.dto.SqlRowDefinition;
import com.xquant.component.common.excelbuilder.enums.RowTypeEnum;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4BaseJson;
import com.xquant.component.common.excelbuilder.util.JsonStringUtils;

/**
 * rowHolder对象解析成真实的对象或sql
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public class ExcelRowHolderResolver4BaseJson implements ExcelRowHolderResolver {

	@Override
	public RowDefinition resolve(ExcelRowHolder excelRowHolder) {
		RowHolder4BaseJson rowHolder = (RowHolder4BaseJson) excelRowHolder;
		BaseJsonRowAttribute baseJsonRowAttribute = rowHolder.getBaseJsonRowAttribute();
		if (StringUtils.isNotBlank(baseJsonRowAttribute.getTableName())) {
			// 当前行代表一个sql
			SqlRowDefinition rowDefinition = new SqlRowDefinition();
			rowDefinition.setTestCaseId(rowHolder.getCaseId());
			// 将json转成insert sql的工具
			rowDefinition.setSql(JsonStringUtils.simpleJsonToInsertSql(baseJsonRowAttribute.getTableName(), rowHolder.getJsonStr()));
			rowDefinition.setIndex(baseJsonRowAttribute.getIndex());
			rowDefinition.setBeforeTest(baseJsonRowAttribute.getBeforeTest());
			return rowDefinition;
		} else if (StringUtils.isNotBlank(baseJsonRowAttribute.getClassType())) {
			// 当前行代表一个对象
			ObjRowDefinition rowDefinition = new ObjRowDefinition();
			Class<?> cls = null;
			try {
				cls = Class.forName(baseJsonRowAttribute.getClassType());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (cls != null) {
				rowDefinition.setTestCaseId(rowHolder.getCaseId());
				rowDefinition.setClassType(cls);
				rowDefinition.setObj(JSON.parseObject(rowHolder.getJsonStr(), cls));
				rowDefinition.setBeforeTest(baseJsonRowAttribute.getBeforeTest());
				return rowDefinition;
			}
		}
		return null;
	}

	@Override
	public boolean support(ExcelRowHolder excelRowHolder) {
		return RowTypeEnum.BASE_JSON.equals(excelRowHolder.rowType());
	}

}
