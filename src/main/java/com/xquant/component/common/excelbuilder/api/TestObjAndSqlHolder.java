package com.xquant.component.common.excelbuilder.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.xquant.component.common.excelbuilder.dto.AfterCheckRowDefiniton;

/**
 * 每一次测试案例的内容包
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public class TestObjAndSqlHolder {

	/**
	 * 开始测试之前使用的对象实例 key为当前对象的类型 value为对象实例
	 */
	private Map<Class<?>, Object> objectUsedBeforeTest;

	/**
	 * 测试完毕之后使用的对象实例 key为当前对象的类型 value为对象实例
	 */
	private Map<Class<?>, Object> objectUsedAfterTest;

	/**
	 * 测试之前执行的简单sql语句 包括清库和添加数据
	 */
	private List<String> simpleSqlBeforeTest;

	/**
	 * 测试之后执行的简单sql语句 主要用于清库
	 */
	private List<String> simpleSqlAfterTest;

	/**
	 * 测试之后执行的查询sql以及检验条件 此处key系统生成 仅用于用于将sql与check分组用 value为数组 用于保存查询sql和检验条件
	 */
	private List<AfterCheckRowDefiniton> sqlAndCheckList;

	public void addSqlBeforeTest(String sql) {
		if (simpleSqlBeforeTest == null) {
			simpleSqlBeforeTest = new ArrayList<String>();
		}
		simpleSqlBeforeTest.add(sql);
	}

	public void addSqlAfterTest(String sql) {
		if (simpleSqlAfterTest == null) {
			simpleSqlAfterTest = new ArrayList<String>();
		}
		simpleSqlAfterTest.add(sql);
	}

	public List<String> getSqlUsedBeforeTest() {
		if (simpleSqlBeforeTest == null) {
			return new ArrayList<String>();
		}
		return simpleSqlBeforeTest;
	}

	public List<String> getSqlUsedAfterTest() {
		if (simpleSqlAfterTest == null) {
			return new ArrayList<String>();
		}
		return simpleSqlAfterTest;
	}

	public void addSqlAndCheck(AfterCheckRowDefiniton sqlAndCheckRowDefiniton) {
		if (sqlAndCheckList == null) {
			sqlAndCheckList = new ArrayList<AfterCheckRowDefiniton>();
		}
		sqlAndCheckList.add(sqlAndCheckRowDefiniton);
	}

	public List<AfterCheckRowDefiniton> getSqlAndChecks() {
		if (sqlAndCheckList == null) {
			return new ArrayList<AfterCheckRowDefiniton>();
		}
		return sqlAndCheckList;
	}

	public void addObjUsedBeforeTest(Class<?> classType, Object obj) {
		if (objectUsedBeforeTest == null) {
			objectUsedBeforeTest = new HashMap<Class<?>, Object>();
		}
		objectUsedBeforeTest.put(classType, obj);
	}

	public void addObjUsedAfterTest(Class<?> classType, Object obj) {
		if (objectUsedAfterTest == null) {
			objectUsedAfterTest = new HashMap<Class<?>, Object>();
		}
		objectUsedAfterTest.put(classType, obj);
	}

	public Map<Class<?>, Object> getObjUsedBeforeTest() {
		if (objectUsedBeforeTest == null) {
			return new HashMap<Class<?>, Object>();
		}
		return objectUsedBeforeTest;
	}

	public Map<Class<?>, Object> getObjUsedAfterTest() {
		if (objectUsedBeforeTest == null) {
			return new HashMap<Class<?>, Object>();
		}
		return objectUsedAfterTest;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
}
