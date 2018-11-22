package com.xquant.component.common.excelbuilder.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xquant.component.common.excelbuilder.context.ExcelReaderContext;
import com.xquant.component.common.excelbuilder.enums.ColumnNumEnum;
import com.xquant.component.common.excelbuilder.reader.ExeclRowReader4AfterCheck;
import com.xquant.component.common.excelbuilder.reader.ExeclRowReader4BaseJson;
import com.xquant.component.common.excelbuilder.reader.ExeclRowReader4SimpleSql;
import com.xquant.component.common.excelbuilder.reader.IExeclRowReader;
import com.xquant.component.common.excelbuilder.rowholder.ExcelRowHolder;
import com.xquant.component.common.excelbuilder.rowholder.RowHolder4BaseJson;
import com.xquant.component.common.excelbuilder.util.ExcelFileLoaderUtils;
import com.xquant.component.common.excelbuilder.util.JsonStringUtils;

/**
 * excel行读取器
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public class ExeclRowReaderManager {

	private Logger logger = LoggerFactory.getLogger(ExeclRowReaderManager.class);

	@Autowired
	private static List<IExeclRowReader> rowReaders;

	static {
		rowReaders = new ArrayList<IExeclRowReader>();
		rowReaders.add(new ExeclRowReader4AfterCheck());
		rowReaders.add(new ExeclRowReader4BaseJson());
		rowReaders.add(new ExeclRowReader4SimpleSql());
	}

	private static final String INCLUDEID_SPLIT_REGEX = ",";

	public List<ExcelRowHolder> read(List<Row> rows) {
		try {
			List<ExcelRowHolder> holders = new ArrayList<ExcelRowHolder>();
			for (Row row : rows) {
				Cell cell = row.getCell(0);
				if (cell != null && CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
					ExcelRowHolder holder = getReader(row).read(row);
					if (RowHolder4BaseJson.class.isInstance(holder)) {
						rigster4BaseCaseHolder((RowHolder4BaseJson) holder);
					} else {
						holders.add(holder);
					}
				} else if (cell != null && CellType.STRING.equals(cell.getCellTypeEnum())) {
					logger.info("行[" + (row.getRowNum() + 1) + "]参数caseid不为整数,属于标题?该行被舍弃");
				} else if (cell == null) {
					logger.info("行[" + (row.getRowNum() + 1) + "]真实数据不存在?该行被舍弃");
				} else {
					logger.info("行[" + (row.getRowNum() + 1) + "]第一个单元格数据即非数字类型也非字符类型,该行被舍弃!");
				}
			}
			// 合并有包含关系的JsonId
			mergeJsonHoldersIncludeOther();
			// 注册所有的JsonId以及JsonData用于后面解析占位符
			addAllJsonDataIntoMap();
			holders.addAll(ExcelReaderContext.getAllRegisterBaseJsonHolder());
			return holders;
		} finally {
			ExcelReaderContext.clearAllBaseJsonHolder();
		}
	}

	private IExeclRowReader getReader(Row row) {
		for (IExeclRowReader excelRowReader : rowReaders) {
			if (excelRowReader.support(row)) {
				return excelRowReader;
			}
		}
		throw new RuntimeException("不支持当前类型excel行的解析,对应类型为" + row.getCell(ColumnNumEnum.ROW_TYPE.getValue()).getStringCellValue());
	}

	/**
	 * 注册baseCaseParmRowHolder
	 * 
	 * @param baseHolder4BaseJson
	 */
	private void rigster4BaseCaseHolder(RowHolder4BaseJson baseHolder4BaseJson) {
		// 进行所有caseId注册 用于后面填充includeCaseId
		ExcelReaderContext.registerBaseJsonHolder(baseHolder4BaseJson.getJsonId(), baseHolder4BaseJson);
		// 包含其他的case的注册
		if (StringUtils.isNotBlank(baseHolder4BaseJson.getBaseJsonRowAttribute().getIncludeJsonId())) {
			ExcelReaderContext.registerHoldersIncludeOther(baseHolder4BaseJson);
		}
	}

	/**
	 * 填充basecase中的includeCaseId
	 */
	private void mergeJsonHoldersIncludeOther() {
		// 有需要进行填充的
		if (ExcelReaderContext.getBaseJsonHolderIncludeOther().size() > 0) {
			for (RowHolder4BaseJson rowHolder4BaseJson : ExcelReaderContext.getBaseJsonHolderIncludeOther()) {
				// 包含其他jsonId的json
				String jsonIdIncluding = rowHolder4BaseJson.getJsonId();
				String jsonStrIncluding = rowHolder4BaseJson.getJsonStr();
				// 被包含的jsonId
				String jsonIdIncluded = rowHolder4BaseJson.getBaseJsonRowAttribute().getIncludeJsonId();
				// 支持包含多个jsonId 格式为 "includeJsonId":"json1,json2",
				if (jsonIdIncluded.contains(INCLUDEID_SPLIT_REGEX)) {
					String[] jsonIdIncludeds = jsonIdIncluded.split(INCLUDEID_SPLIT_REGEX);
					for (String jsonId : jsonIdIncludeds) {
						if (StringUtils.isNotBlank(jsonId)) {
							mergeJsonDataIfIncuding(rowHolder4BaseJson, jsonStrIncluding, jsonId);
						}
					}
				} else {
					mergeJsonDataIfIncuding(rowHolder4BaseJson, jsonStrIncluding, jsonIdIncluded);
				}
				ExcelReaderContext.registerOrReplaceBaseJsonHolder(jsonIdIncluding, rowHolder4BaseJson);
			}
		}
	}

	private void mergeJsonDataIfIncuding(RowHolder4BaseJson rowHolder4BaseJson, String jsonStrIncluding, String jsonId) {
		
		int rowNum = rowHolder4BaseJson.getRow().getRowNum() + 1;
		int columnNum = ColumnNumEnum.ROW_ATTRIBUTE.getValue() + 1;
		if(rowHolder4BaseJson.getJsonId().equals(jsonId)) {
			throw new RuntimeException("行[" + rowNum + "],列[" + columnNum + "]解析出错,不可以包含jsonId等于自己 " + jsonId);
		}
		
		RowHolder4BaseJson baseJsonRowHolder = ExcelReaderContext.getBaseJsonRowHolder(jsonId);
		if (baseJsonRowHolder == null) {
			
			throw new RuntimeException("行[" + rowNum + "],列[" + columnNum + "]解析出错,不存在的jsonId = " + jsonId);
		}
		jsonStrIncluding = JsonStringUtils.merge(jsonStrIncluding, baseJsonRowHolder.getJsonStr());
		rowHolder4BaseJson.setJsonStr(jsonStrIncluding);
	}

	private void addAllJsonDataIntoMap() {
		for (RowHolder4BaseJson baseJson : ExcelReaderContext.getAllRegisterBaseJsonHolder()) {
			ExcelReaderContext.addBaseJsonDataMap(baseJson.getCaseId() + "." + baseJson.getJsonId(), baseJson.getJsonStr());
		}
	}

	public static void main(String[] args) {

		ExeclRowReaderManager execlRowReaderManager = new ExeclRowReaderManager();
		String resourceLocation = "classpath:exceldata1121.xlsx";
		List<Row> rowList = ExcelFileLoaderUtils.loadExcelDefaultResource(resourceLocation);
		List<ExcelRowHolder> rowHolderList = execlRowReaderManager.read(rowList);
		for (ExcelRowHolder excelRowHolder : rowHolderList) {
			System.out.println(excelRowHolder);
		}

	}

}
