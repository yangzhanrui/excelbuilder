package com.xquant.component.common.excelbuilder.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.xquant.component.common.excelbuilder.constant.CaseIdConstant;
import com.xquant.component.common.excelbuilder.context.ExcelReaderContext;
import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.resolver.ExcelRowHolderResolver;
import com.xquant.component.common.excelbuilder.resolver.ExcelRowHolderResolver4AfterCheck;
import com.xquant.component.common.excelbuilder.resolver.ExcelRowHolderResolver4BaseJson;
import com.xquant.component.common.excelbuilder.resolver.ExcelRowHolderResolver4SimpleSql;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.util.ExcelFileLoaderUtils;

/**
 * 进行rowHolder的解析 占位符解析 以及 转化为 RowDefinition对象
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月20日
 */
public class ExcelRowHolderResolverManager {

	@Autowired
	private static List<ExcelRowHolderResolver> excelRowHolderResolvers;

	static {
		excelRowHolderResolvers = new ArrayList<ExcelRowHolderResolver>();
		excelRowHolderResolvers.add(new ExcelRowHolderResolver4AfterCheck());
		excelRowHolderResolvers.add(new ExcelRowHolderResolver4BaseJson());
		excelRowHolderResolvers.add(new ExcelRowHolderResolver4SimpleSql());
	}

	public Map<String, List<RowDefinition>> resolve(List<ExcelRowHolder> excelRowHolders) {

		Map<String, List<RowDefinition>> rowDefinitionMap = new HashMap<String, List<RowDefinition>>();
		try {
			for (ExcelRowHolder excelRowHolder : excelRowHolders) {
				RowDefinition rowDefintion = getDelegateResolver(excelRowHolder).resolve(excelRowHolder);
				if (rowDefintion != null) {
					String testUnit = rowDefintion.getTestCaseId();
					if (!rowDefinitionMap.containsKey(testUnit)) {
						rowDefinitionMap.put(testUnit, new ArrayList<RowDefinition>());
					}
					rowDefinitionMap.get(testUnit).add(rowDefintion);
				}
			}
		} finally {
			ExcelReaderContext.clearAllBaseJsonData();
		}

		// 公共信息
		if (rowDefinitionMap.get(CaseIdConstant.COMMON_CASE_ID_SIGN) != null) {
			// 将公共信息添加到其他测试组中
			Set<String> caseIdSet = rowDefinitionMap.keySet();
			for (String caseId : caseIdSet) {
				if (!CaseIdConstant.COMMON_CASE_ID_SIGN.equals(caseId)) {
					rowDefinitionMap.get(caseId).addAll(rowDefinitionMap.get(CaseIdConstant.COMMON_CASE_ID_SIGN));
				}
			}
			// 删除公共信息
			rowDefinitionMap.remove(CaseIdConstant.COMMON_CASE_ID_SIGN);
		}

		return rowDefinitionMap;
	}

	private ExcelRowHolderResolver getDelegateResolver(ExcelRowHolder excelRowHolder) {
		for (ExcelRowHolderResolver excelRowHolderResolver : excelRowHolderResolvers) {
			if (excelRowHolderResolver.support(excelRowHolder)) {
				return excelRowHolderResolver;
			}
		}
		throw new RuntimeException("不支持当前类型的rowHolder?" + excelRowHolder.rowType().getValue());
	}

	public static void main(String[] args) {
		ExeclRowReaderManager execlRowReaderManager = new ExeclRowReaderManager();
		String resourceLocation = "classpath:exceldata1121.xlsx";
		List<Row> rowList = ExcelFileLoaderUtils.loadExcelResourceBySheetNum(resourceLocation, 0);
		List<ExcelRowHolder> rowHolderList = execlRowReaderManager.read(rowList);
		ExcelRowHolderResolverManager excelRowHolderResolverManager = new ExcelRowHolderResolverManager();
		Map<String, List<RowDefinition>> resolveMap = excelRowHolderResolverManager.resolve(rowHolderList);
		Set<String> keySet = resolveMap.keySet();
		for (String key : keySet) {
			System.out.println("==================" + key + "==================");
			List<RowDefinition> rowDefinitionList = resolveMap.get(key);
			for (RowDefinition rowDefinition : rowDefinitionList) {
				System.out.println(rowDefinition);
			}
		}
	}

}
