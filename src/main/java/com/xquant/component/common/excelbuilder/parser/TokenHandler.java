package com.xquant.component.common.excelbuilder.parser;

public interface TokenHandler {
	// 此处的content即为占位符中的元素
	String handleToken(String content,String caseId);
}