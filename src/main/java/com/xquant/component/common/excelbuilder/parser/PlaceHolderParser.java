package com.xquant.component.common.excelbuilder.parser;

import java.util.Map;

public class PlaceHolderParser {

	public static String parse(String str, Map<String, String> map, String caseId) {
		VariableTokenHandler handler = new VariableTokenHandler(map);
		GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
		return parser.parse(str, caseId);
	}

	private static class VariableTokenHandler implements TokenHandler {
		private Map<String, String> map;

		public VariableTokenHandler(Map<String, String> map) {
			this.map = map;
		}

		public String handleToken(String content, String caseId) {
			// 此处真实key采用caseId再加上占位符中的字符串
			String key = caseId + "." + content;
			if (map != null && map.containsKey(key)) {
				return "'" + map.get(key) + "'";
			}
			return "'#{" + content + "}'";
		}
	}
}