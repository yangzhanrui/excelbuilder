package com.xquant.component.common.excelbuilder.manager;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xquant.component.common.excelbuilder.api.TestObjAndSqlHolder;
import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.util.ExcelFileLoaderUtils;


public class ExcelDataResourceLoader {

	private Logger logger = LoggerFactory.getLogger(ExcelDataResourceLoader.class);

	private ExcelRowHolderResolverManager excelRowHolderResolverManager = new ExcelRowHolderResolverManager();

	private ExeclRowReaderManager execlRowReaderManager = new ExeclRowReaderManager();

	public TestObjAndSqlHolder[] loadResource(String resourceLocation, int sheetNum) {
		try {
			List<Row> rowList = ExcelFileLoaderUtils.loadExcelResourceBySheetNum(resourceLocation,sheetNum);
			List<ExcelRowHolder> rowHolderList = execlRowReaderManager.read(rowList);
			Map<String, List<RowDefinition>> rowDefinitionMap = excelRowHolderResolverManager.resolve(rowHolderList);
			return TestObjAndSqlPackageBuilder.buildPackge(rowDefinitionMap);
		} catch (Exception e) {
			logger.error("读取excel测试数据错误", e);
			return null;
		}
	}
	
	public static void main(String[] args) {
		String resourceLocation = "classpath:exceldata1121.xlsx";
		ExcelDataResourceLoader loader = new ExcelDataResourceLoader();
		TestObjAndSqlHolder[] testObjAndSqlHolders = loader.loadResource(resourceLocation, 0);
		for (TestObjAndSqlHolder testObjAndSqlHolder : testObjAndSqlHolders) {
			System.out.println(testObjAndSqlHolder);
		}
	}
}
