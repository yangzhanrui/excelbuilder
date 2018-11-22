package com.xquant.component.common.excelbuilder.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.xquant.component.common.excelbuilder.constant.CaseIdConstant;
import com.xquant.component.common.excelbuilder.parser.PlaceHolderParser;

/**
 * 占位符工具类
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月19日
 */
public class PlaceHolderUtil {

	public static final String PLACEHOLDER_REGEX = "#\\{.+\\}";

	/**
	 * 当前字符串是否包含占位符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasPlaceHolder(String str) {
		Assert.notNull(str, "字符串不允许为空");
		Pattern p = Pattern.compile(PLACEHOLDER_REGEX);
		Matcher m = p.matcher(str);
		return m.find();
		// return m.matches();
	}

	/**
	 * 进行占位符的解析操作
	 * 
	 * @param str
	 *            需要进行占位符解析的字符串
	 * @param map
	 *            用于解析占位符的map
	 * @param caseId
	 *            测试单元Id
	 * @return 解析之后的字符串
	 */
	public static String resolvePlaceHolder(String str, Map<String, String> map, String caseId) {
		Assert.notNull(str, "str can not be null");
		Assert.notNull(map, "map can not be null");
		Assert.notNull(caseId, "caseId can not be null");
		String afterResolve = PlaceHolderParser.parse(str, map, caseId);
		if (hasPlaceHolder(afterResolve)) {
			afterResolve = PlaceHolderParser.parse(str, map, CaseIdConstant.COMMON_CASE_ID_SIGN);
		}
		if (hasPlaceHolder(afterResolve)) {
			throw new RuntimeException("解析字符串{" + str + "}占位符失败");
		}
		return afterResolve;
	}

	public static void main(String[] args) {

		String str = "SELECT * FROM ttrd_xcc_register_push_message WHERE MESSAGE_KEY = #{case1.MESSAGE_KEY};";

		System.out.println(hasPlaceHolder(str));

		System.out.println("#{case1.icode}".matches(PLACEHOLDER_REGEX));
		System.out.println("#case1.icode}".matches(PLACEHOLDER_REGEX));
		System.out.println("#{case1.icode".matches(PLACEHOLDER_REGEX));
		System.out.println("{case1.icode}".matches(PLACEHOLDER_REGEX));
		Map<String, String> map = new HashMap<String, String>();
		map.put("case1.icode", "1");
		map.put("case2.icode", "2");
		System.out.println(resolvePlaceHolder("a = #{case1.icode} = #{case2.icode}", map, "1"));
	}

}
