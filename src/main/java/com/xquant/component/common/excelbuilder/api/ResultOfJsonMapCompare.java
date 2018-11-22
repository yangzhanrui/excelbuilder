package com.xquant.component.common.excelbuilder.api;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 普通json与map的比较结果
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月21日
 */
public class ResultOfJsonMapCompare {

	private boolean success = true;

	private List<String> errorInfos;

	public void addErrorInfo(String errorInfo) {
		if (errorInfos == null) {
			errorInfos = new ArrayList<>();
		}
		success = false;
		errorInfos.add(errorInfo);
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getErrorInfos() {
		return errorInfos;
	}

	public void setErrorInfos(List<String> errorInfos) {
		this.errorInfos = errorInfos;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
