package com.xquant.component.common.excelbuilder.parser;

public class GenericTokenParser {

	private final String openToken;
	private final String closeToken;
	private final TokenHandler handler;

	public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
		this.openToken = openToken;
		this.closeToken = closeToken;
		this.handler = handler;
	}

	public String parse(String text,String caseId) {
		StringBuilder builder = new StringBuilder();
		if (text != null) {
			String after = text;
			int start = after.indexOf(openToken);
			int end = after.indexOf(closeToken);
			while (start > -1) {
				if (end > start) {
					String before = after.substring(0, start);
					String content = after.substring(start + openToken.length(), end);
					String substitution;

					// check if variable has to be skipped
					if (start > 0 && text.charAt(start - 1) == '\\') {
						before = before.substring(0, before.length() - 1);
						substitution = new StringBuilder(openToken).append(content).append(closeToken).toString();
					} else {
						substitution = handler.handleToken(content,caseId);
					}

					builder.append(before);
					builder.append(substitution);
					after = after.substring(end + closeToken.length());
				} else if (end > -1) {
					String before = after.substring(0, end);
					builder.append(before);
					builder.append(closeToken);
					after = after.substring(end + closeToken.length());
				} else {
					break;
				}
				start = after.indexOf(openToken);
				end = after.indexOf(closeToken);
			}
			builder.append(after);
		}
		return builder.toString();
	}

}