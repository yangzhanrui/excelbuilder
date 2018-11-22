package com.xquant.component.common.excelbuilder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.xquant.component.common.excelbuilder.manager.ExeclRowReaderManager;

/**
 * 读取excel文件并生成java对象
 * 
 * @project xquant-platform-component-excelbuilder
 * @author guanglai.zhou
 * @date 2018年11月15日
 */
public class ExcelFileLoaderUtils {

	private static ExeclRowReaderManager ExeclRowResolverManager = new ExeclRowReaderManager();

	private static Logger logger = LoggerFactory.getLogger(ExcelFileLoaderUtils.class);

	/**
	 * 装载excel资源数据
	 * 
	 * @param resourceLocation
	 * @return
	 */
	public static List<Row> loadExcelDefaultResource(String resourceLocation) {
		return loadExcelRow(loadExcelSheet(getResource(resourceLocation), 0));
	}

	/**
	 * 根据excel资源路径以及工作表数取得资源数据
	 * 
	 * @param resourceLocation
	 * @param sheetNum
	 *            工作表数
	 * @return excel工作表行集合信息
	 */
	public static List<Row> loadExcelResourceBySheetNum(String resourceLocation, int sheetNum) {
		return loadExcelRow(loadExcelSheet(getResource(resourceLocation), sheetNum));
	}

	/**
	 * 根据文件路径获取文件资源
	 * 
	 * @param resourceLocation
	 * @return
	 */
	public static Resource getResource(String resourceLocation) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] configResources = null;
		try {
			configResources = resourcePatternResolver.getResources(resourceLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (configResources.length == 0) {
			throw new RuntimeException("Unable to find excel resource for specified definition. " + "Group resource name [" + resourceLocation + "]");
		}
		// 暂时只支持配置一个文件
		return configResources[0];
	}

	/**
	 * 根据资源获取指定的Excel工作表
	 * 
	 * @param resource
	 *            资源对象
	 * @param sheetNum
	 *            工作表
	 * @return 工作表
	 */
	public static Sheet loadExcelSheet(Resource resource, int sheetNum) {
		try {
			Workbook workbook = WorkbookFactory.create(resource.getFile());
			int numerOfSheet = workbook.getNumberOfSheets();
			if (sheetNum >= numerOfSheet) {
				throw new RuntimeException("当前excel表只有" + numerOfSheet + "张工作表,不存在第" + (sheetNum + 1) + "张表");
			}
			return workbook.getSheetAt(sheetNum);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取一个sheet的所有行
	 * 
	 * @param sheet
	 * @return
	 */
	public static List<Row> loadExcelRow(Sheet sheet) {
		final List<Row> rowList = new ArrayList<Row>();
		sheet.forEach(new Consumer<Row>() {

			@Override
			public void accept(Row row) {
				rowList.add(row);
			}
		});
		return rowList;
	}

	/**
	 * 读取excel文件 并获取指定的Sheet
	 * 
	 * @param filePath
	 * @param sheetNum
	 * @return
	 */
	public static Sheet loadExcel(String filePath, int sheetNum) {

		File file = new File(filePath);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			int numerOfSheet = workbook.getNumberOfSheets();
			if (sheetNum >= numerOfSheet) {
				throw new RuntimeException("当前excel表只有" + numerOfSheet + "张工作表,不存在第" + (sheetNum + 1) + "张表");
			}
			return workbook.getSheetAt(sheetNum);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param filePath
	 * @param sheetNum
	 * @param rowNum
	 * @param colNum
	 * @param value
	 */
	public static boolean updateExcelAtCell(String resourceLocation, int sheetNum, int rowNum, int colNum, String value) {

		Resource resource = getResource(resourceLocation);
		File file = null;
		try {
			file = resource.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (file != null) {
			return updateExcelAtSpecifiedCell(file.getAbsolutePath(), sheetNum, rowNum, colNum, value);
		}
		return false;
	}

	/**
	 * 
	 * @param filePath
	 * @param sheetNum
	 * @param rowNum
	 * @param colNum
	 * @param value
	 */
	public static boolean updateExcelAtSpecifiedCell(String filePath, int sheetNum, int rowNum, int colNum, String value) {
		try {
			File file = new File(filePath);
			FileInputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			wb.getSheetAt(sheetNum).getRow(rowNum).getCell(colNum).setCellValue(value);
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			is.close();
			fileOut.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

		// FileOutputStream outputStream = null;
		// try {
		// File file = new File(filePath);
		// Workbook workbook = WorkbookFactory.create(file);
		// int numerOfSheet = workbook.getNumberOfSheets();
		// if (sheetNum >= numerOfSheet) {
		// throw new RuntimeException("当前excel表只有" + numerOfSheet + "张工作表,不存在第" +
		// (sheetNum + 1) + "张表");
		// }
		// Cell cell = workbook.getSheetAt(sheetNum).getRow(rowNum).getCell(colNum);
		// cell.setCellValue(value);
		// outputStream = new FileOutputStream(file);
		// outputStream.flush();
		// workbook.write(outputStream);
		// return true;
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (EncryptedDocumentException e) {
		// e.printStackTrace();
		// } catch (InvalidFormatException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (outputStream != null) {
		// outputStream.close();
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// return false;
	}

	public static void main(String[] args) {
		String filePath = "F:" + File.separator + "测试文档.xlsx";
		System.out.println(updateExcelAtSpecifiedCell(filePath, 0, 5, 4, "测试更新---->"));
	}

}
