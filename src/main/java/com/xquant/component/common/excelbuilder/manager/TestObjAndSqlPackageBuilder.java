package com.xquant.component.common.excelbuilder.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.Assert;

import com.xquant.component.common.excelbuilder.api.TestObjAndSqlHolder;
import com.xquant.component.common.excelbuilder.dto.ObjRowDefinition;
import com.xquant.component.common.excelbuilder.dto.RowDefinition;
import com.xquant.component.common.excelbuilder.dto.AfterCheckRowDefiniton;
import com.xquant.component.common.excelbuilder.dto.SqlRowDefinition;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.util.ExcelFileLoaderUtils;

public class TestObjAndSqlPackageBuilder {

	public static TestObjAndSqlHolder[] buildPackge(Map<String, List<RowDefinition>> map) {
		if (map == null || map.size() < 1) {
			throw new RuntimeException("数据不存在,不允许进行转换");
		}
		TestObjAndSqlHolder[] packages = new TestObjAndSqlHolder[map.size()];
		int index = 0;
		Set<String> keySet = map.keySet();
		List<String> keyList = new ArrayList<String>(keySet);
		Collections.sort(keyList);
		for (String testUnit : keyList) {
			packages[index] = new TestObjAndSqlHolder();
			buildPackage(packages[index++], map.get(testUnit));
		}
		return packages;
	}

	public static TestObjAndSqlHolder buildPackage(TestObjAndSqlHolder testPackage, List<RowDefinition> rowDefinitionList) {

		Assert.notNull(testPackage, "testPackage can not be null");
		Assert.notNull(rowDefinitionList, "rowDefinitionList can not be null");

		if (rowDefinitionList.size() < 1) {
			throw new RuntimeException("不存在真实数据?");
		}

		List<SqlRowDefinition> sqlRowDefinitions = new ArrayList<SqlRowDefinition>();
		for (RowDefinition rowDefinition : rowDefinitionList) {
			if (ObjRowDefinition.class.isInstance(rowDefinition)) {
				ObjRowDefinition definition = (ObjRowDefinition) rowDefinition;
				if (Boolean.TRUE.equals(definition.getBeforeTest())) {
					testPackage.addObjUsedBeforeTest(definition.getClassType(), definition.getObj());
				} else {
					testPackage.addObjUsedAfterTest(definition.getClassType(), definition.getObj());
				}
			} else if (SqlRowDefinition.class.isInstance(rowDefinition)) {
				sqlRowDefinitions.add((SqlRowDefinition) rowDefinition);
			} else if (AfterCheckRowDefiniton.class.isInstance(rowDefinition)) {
				AfterCheckRowDefiniton definition = (AfterCheckRowDefiniton) rowDefinition;
                if(definition.getSqlType()) {				
                	testPackage.addSqlAndCheck((AfterCheckRowDefiniton) rowDefinition);
                }else {
                	testPackage.addObjUsedAfterTest(definition.getClassType(), definition.getObject());
                }
			}
		}

		Collections.sort(sqlRowDefinitions, new Comparator<SqlRowDefinition>() {
			@Override
			public int compare(SqlRowDefinition o1, SqlRowDefinition o2) {
				return o1.getIndex() - o2.getIndex();
			}
		});

		for (SqlRowDefinition sqlRowDefinition : sqlRowDefinitions) {
			String sql = sqlRowDefinition.getSql();
			if (sql.contains(";")) {
				for (String sqlAftersplit : sql.split(";")) {
					if (StringUtils.isNotBlank(sqlAftersplit)) {
						// 进行注册
						registerSql(testPackage, sqlRowDefinition, sqlAftersplit);
					}
				}
			} else {
				// 进行注册
				registerSql(testPackage, sqlRowDefinition, sql);
			}
		}

		return testPackage;
	}

	public static void registerSql(TestObjAndSqlHolder testPackage, SqlRowDefinition sqlRowDefinition, String sql) {
		if (sqlRowDefinition.getBeforeTest()) {
			testPackage.addSqlBeforeTest(sql);
		} else {
			testPackage.addSqlAfterTest(sql);
		}
	}

	public static void main(String[] args) {
		ExeclRowReaderManager execlRowReaderManager = new ExeclRowReaderManager();
		String resourceLocation = "classpath:exceldata1121.xlsx";
		List<Row> rowList = ExcelFileLoaderUtils.loadExcelResourceBySheetNum(resourceLocation, 0);
		List<ExcelRowHolder> rowHolderList = execlRowReaderManager.read(rowList);
		ExcelRowHolderResolverManager excelRowHolderResolverManager = new ExcelRowHolderResolverManager();
		Map<String, List<RowDefinition>> resolveMap = excelRowHolderResolverManager.resolve(rowHolderList);
		TestObjAndSqlHolder[] buildPackge = TestObjAndSqlPackageBuilder.buildPackge(resolveMap);
		for (TestObjAndSqlHolder testObjAndSqlHolder : buildPackge) {
			System.out.println(testObjAndSqlHolder.getObjUsedBeforeTest());
			System.out.println(testObjAndSqlHolder.getObjUsedAfterTest());
			System.out.println(testObjAndSqlHolder.getSqlUsedBeforeTest());
			System.out.println(testObjAndSqlHolder.getSqlUsedAfterTest());
			System.out.println(testObjAndSqlHolder.getSqlAndChecks());
		}
	}

}
