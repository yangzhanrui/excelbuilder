package com.xquant.component.common.excelbuilder.api;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xquant.component.common.excelbuilder.dto.AfterCheckRowDefiniton;
import com.xquant.component.common.excelbuilder.manager.ExcelDataResourceLoader;
import com.xquant.component.common.excelbuilder.util.ExcelFileLoaderUtils;
import com.xquant.component.common.excelbuilder.util.JsonStringUtils;

public class XccJunitTestRule implements TestRule {

	private Logger logger = LoggerFactory.getLogger(XccJunitTestRule.class);

	private JdbcTemplate jdbcTemplate;

	private DataSource dataSource;

	private ExcelDataResourceLoader excelDataResourceLoader = new ExcelDataResourceLoader();

	public XccJunitTestRule(DataSource dataSource) {
		if (dataSource != null) {
			this.dataSource = dataSource;
			this.jdbcTemplate = new JdbcTemplate(dataSource);
		} else {
			logger.info("XccJunitTestRule constructed without database operation because of input parameter [dataSource] is null");
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {

				Class<?> testClass = description.getTestClass();
				XccTestContextConfiguration contextConfiguration = testClass.getAnnotation(XccTestContextConfiguration.class);
				Method testMethod = description.getTestClass().getMethod(description.getMethodName());
				XccTestSheetIndex sheetIndex = testMethod.getAnnotation(XccTestSheetIndex.class);
				if (contextConfiguration != null && sheetIndex != null) {
					String resourceLocation = contextConfiguration.resourceLocation();
					int index = sheetIndex.value();
					TestObjAndSqlHolder[] holders = excelDataResourceLoader.loadResource(resourceLocation, index);

					for (TestObjAndSqlHolder testObjAndSqlHolder : holders) {

						TestObjAndSqlHolder holder = testObjAndSqlHolder;

						// 填充上下文对象
						Map<Class<?>, Object> objUsedBeforeTestMap = holder.getObjUsedBeforeTest();
						XccJunitBeforeTestObjContext.addMap(objUsedBeforeTestMap);
						// 执行sql
						if (jdbcTemplate != null) {
							List<String> sqlUsedBeforeTest = holder.getSqlUsedBeforeTest();
							for (String sql : sqlUsedBeforeTest) {
								jdbcTemplate.execute(sql);
							}
						}

						base.evaluate();

						if (jdbcTemplate != null) {
							List<String> sqlUsedAfterTest = holder.getSqlUsedAfterTest();
							for (String sql : sqlUsedAfterTest) {
								jdbcTemplate.execute(sql);
							}

							List<AfterCheckRowDefiniton> sqlAndChecks = holder.getSqlAndChecks();
							for (AfterCheckRowDefiniton sqlAndCheckRowDefiniton : sqlAndChecks) {
								String sql = sqlAndCheckRowDefiniton.getSql();
								String checkStr = sqlAndCheckRowDefiniton.getCheckJsonStr();
								List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
								if (queryForList.size() >= 1) {
									Map<String, Object> resultMap = queryForList.get(0);
									ResultOfJsonMapCompare resultCompare = JsonStringUtils.compareSimpleJsonWithMap(checkStr, resultMap);
									System.out.println(resultCompare);
									if (!resultCompare.getSuccess()) {
										ExcelFileLoaderUtils.updateExcelAtCell(resourceLocation, index, sqlAndCheckRowDefiniton.getRowNum(),
												sqlAndCheckRowDefiniton.getColumnNum(), resultCompare.getErrorInfos().toString());
										throw new RuntimeException(resultCompare.getErrorInfos().toString());
									} else {
										ExcelFileLoaderUtils.updateExcelAtCell(resourceLocation, index, sqlAndCheckRowDefiniton.getRowNum(),
												sqlAndCheckRowDefiniton.getColumnNum(), "测试通过");
									}
								}
							}
						}

					}

				}

			}
		};
	}

}
