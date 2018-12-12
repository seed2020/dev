package com.common.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	/** 해당 Sheet 읽기 */
	public static String[][] readSheet(File file, int sheetNo) throws IOException {
		if(file == null || !file.isFile()) return null;
		if(file.getName().toLowerCase().endsWith(".xlsx")){
			return readXLSX(file, sheetNo, null);
		} else {
			return readXLS(file, sheetNo, null);
		}
	}
	
	/** 해당 Sheet 읽기 */
	public static String[][] readSheet(File file, String sheetName) throws IOException {
		if(file == null || !file.isFile()) return null;
		if(file.getName().toLowerCase().endsWith(".xlsx")){
			return readXLSX(file, -1, sheetName);
		} else {
			return readXLS(file, -1, sheetName);
		}
	}
	
	/** Sheet 별로 읽어서 배열에 담아 리턴 */
	public static List<String[][]> readToList(File file) throws IOException {
		return readToList(file, null);
	}
	/** Sheet 별로 읽어서 배열에 담아 리턴 */
	public static List<String[][]> readToList(File file, int[] sheetNos) throws IOException {
		if(file == null || !file.isFile()) return null;
		if(file.getName().toLowerCase().endsWith(".xlsx")){
			List<String[][]> returnList = new ArrayList<String[][]>();
			readXLSX(file, sheetNos, null, returnList, null);
			return returnList;
		} else {
			List<String[][]> returnList = new ArrayList<String[][]>();
			readXLS(file, sheetNos, null, returnList, null);
			return returnList;
		}
	}
	
	/** Sheet 별로 읽어서 맵에 담아 리턴 - KEY:쉬트명 */
	public static Map<String, String[][]> readToMap(File file) throws IOException {
		return readToMap(file, null);
	}
	/** Sheet 별로 읽어서 맵에 담아 리턴 - KEY:쉬트명 */
	public static Map<String, String[][]> readToMap(File file, String[] sheetNames) throws IOException {
		if(file == null || !file.isFile()) return null;
		if(file.getName().toLowerCase().endsWith(".xlsx")){
			Map<String, String[][]> returnMap = new HashMap<String, String[][]>();
			readXLSX(file, null, sheetNames, null, returnMap);
			return returnMap;
		} else {
			Map<String, String[][]> returnMap = new HashMap<String, String[][]>();
			readXLS(file, null, sheetNames, null, returnMap);
			return returnMap;
		}
	}
	/** XLSX 파일 읽기 - 엑셀2002 ~  */
	private static void readXLSX(File file, int[] sheetNos, String[] sheetNames,
			List<String[][]> returnList, Map<String, String[][]> returnMap) throws IOException{
		
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		XSSFSheet sheet;
		if(returnList != null){
			if(sheetNos != null){
				int sheetCount = workbook.getNumberOfSheets();
				for(int sheetNo : sheetNos){
					if(sheetNo >= 0 && sheetNo < sheetCount){
						sheet = workbook.getSheetAt(sheetNo);
						returnList.add(readSheetXLSX(sheet, evaluator));
					} else {
						String message = "Excel["+file.getName()+"] has no sheet - sheetNo : "+sheetNo;
						System.out.println(message);
						throw new IOException(message);
					}
				}
			} else if(returnMap != null){
				int sheetCount = workbook.getNumberOfSheets();
				for(int sheetNo=0; sheetNo < sheetCount; sheetNo++){
					sheet = workbook.getSheetAt(sheetNo);
					returnList.add(readSheetXLSX(sheet, evaluator));
				}
			}
		} else if(returnMap != null){
			
			if(sheetNames != null){
				int sheetNo;
				for(String sheetName : sheetNames){
					sheetNo = workbook.getSheetIndex(sheetName);
					if(sheetNo >= 0){
						sheet = workbook.getSheetAt(sheetNo);
						returnMap.put(sheet.getSheetName(), readSheetXLSX(sheet, evaluator));
					}
				}
			} else {
				int sheetCount = workbook.getNumberOfSheets();
				for(int sheetNo=0; sheetNo < sheetCount; sheetNo++){
					sheet = workbook.getSheetAt(sheetNo);
					returnMap.put(sheet.getSheetName(), readSheetXLSX(sheet, evaluator));
				}
			}
		}
	}
	/** XLS 파일 읽기 - 엑셀 97 ~ 2003 */
	private static void readXLS(File file, int[] sheetNos, String[] sheetNames,
			List<String[][]> returnList, Map<String, String[][]> returnMap) throws IOException{
		
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook workbook = new HSSFWorkbook(fs);// workbook 생성
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		HSSFSheet sheet;
		if(returnList != null){
			if(sheetNos != null){
				int sheetCount = workbook.getNumberOfSheets();
				for(int sheetNo : sheetNos){
					if(sheetNo >= 0 && sheetNo < sheetCount){
						sheet = workbook.getSheetAt(sheetNo);
						returnList.add(readSheetXLS(sheet, evaluator));
					} else {
						String message = "Excel["+file.getName()+"] has no sheet - sheetNo : "+sheetNo;
						System.out.println(message);
						throw new IOException(message);
					}
				}
			} else if(returnMap != null){
				int sheetCount = workbook.getNumberOfSheets();
				for(int sheetNo=0; sheetNo < sheetCount; sheetNo++){
					sheet = workbook.getSheetAt(sheetNo);
					returnList.add(readSheetXLS(sheet, evaluator));
				}
			}
		} else {
			
			if(sheetNames != null){
				int sheetNo;
				for(String sheetName : sheetNames){
					sheetNo = workbook.getSheetIndex(sheetName);
					if(sheetNo>=0){
						sheet = workbook.getSheetAt(sheetNo);
						returnMap.put(sheet.getSheetName(), readSheetXLS(sheet, evaluator));
					}
				}
			} else {
				int sheetCount = workbook.getNumberOfSheets();
				for(int sheetNo=0; sheetNo < sheetCount; sheetNo++){
					sheet = workbook.getSheetAt(sheetNo);
					returnMap.put(sheet.getSheetName(), readSheetXLS(sheet, evaluator));
				}
			}
		}
	}
	/** XLSX 파일 읽기 - 엑셀2002 ~  */
	private static String[][] readXLSX(File file, int sheetNo, String sheetName) throws IOException{
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		if(sheetName == null){
			int sheetCount = workbook.getNumberOfSheets();
			if(sheetNo >= sheetCount) sheetNo = -1;
		} else {
			sheetNo = workbook.getSheetIndex(sheetName);
		}
		
		if(sheetNo >= 0){
			XSSFSheet sheet = workbook.getSheetAt(sheetNo);
			return readSheetXLSX(sheet, evaluator);
		} else {
			String message = (sheetName == null) ?
					"Excel["+file.getName()+"] has no sheet - sheetNo : "+sheetNo :
					"Excel["+file.getName()+"] has no sheet - sheetName : "+sheetName;
			System.out.println(message);
			throw new IOException(message);
		}
	}
	/** XLS 파일 읽기 - 엑셀 97 ~ 2003 */
	private static String[][] readXLS(File file, int sheetNo, String sheetName) throws IOException{
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook workbook = new HSSFWorkbook(fs);// workbook 생성
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		if(sheetName == null){
			int sheetCount = workbook.getNumberOfSheets();
			if(sheetNo >= sheetCount) sheetNo = -1;
		} else {
			sheetNo = workbook.getSheetIndex(sheetName);
		}
		
		if(sheetNo >= 0){
			HSSFSheet sheet = workbook.getSheetAt(sheetNo);
			return readSheetXLS(sheet, evaluator);
		} else {
			String message = (sheetName == null) ?
					"Excel["+file.getName()+"] has no sheet - sheetNo : "+sheetNo :
					"Excel["+file.getName()+"] has no sheet - sheetName : "+sheetName;
			System.out.println(message);
			throw new IOException(message);
		}
	}
	/** XLSX 파일 읽기 */
	private static String[][] readSheetXLSX(XSSFSheet sheet, FormulaEvaluator evaluator){

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		int c, colCnt, r, rowCnt = sheet.getPhysicalNumberOfRows(), cellType;
		String[][] returnArray = new String[rowCnt][];

		XSSFRow row;
		XSSFCell cell;
		String data;
		for (r = 0; r < rowCnt; r++) {

			row = sheet.getRow(r);
			if (row != null) {
				
				colCnt = row.getLastCellNum();
				returnArray[r] = new String[colCnt];

				for (c = row.getFirstCellNum(); c <= colCnt; c++) {
					
					cell = row.getCell(c);
					if (cell != null) {
						
						data = null;
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_BOOLEAN:
							data = String.valueOf(cell.getBooleanCellValue());
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								data = dateFormat.format(cell.getDateCellValue());
							} else {
								data = String.valueOf(cell.getNumericCellValue());
							}
							break;
						case XSSFCell.CELL_TYPE_STRING:
							data = cell.toString();
							break;
						case XSSFCell.CELL_TYPE_BLANK:
						case XSSFCell.CELL_TYPE_ERROR:
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							if (!(cell.toString() == "")) {
								cellType = evaluator.evaluateFormulaCell(cell);
								if (cellType == 0) {
									data = String.valueOf(cell.getNumericCellValue());
								} else if (cellType == 1) {
									data = cell.getStringCellValue();
								} else if (cellType == 4) {
									data = String.valueOf(cell.getBooleanCellValue());
								}
								break;
							}
						default:
							continue;
						}
						returnArray[r][c] = data;
					}
				}
			}
		}
		return returnArray;
	}
	/** XLS 파일 읽기 - 엑셀 97 ~ 2003 */
	private static String[][] readSheetXLS(HSSFSheet sheet, FormulaEvaluator evaluator){

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		int c, colCnt, r, rowCnt = sheet.getPhysicalNumberOfRows(), cellType; // sheet의 전체 행 수를 알아낸다.
		String[][] returnArray = new String[rowCnt][];
		
		HSSFRow row;
		HSSFCell cell;
		String data;
		for (r = 0; r < rowCnt; r++) {

			row = sheet.getRow(r); // sheet에서 행을 가져오기
			if (row != null) { // 로우가 비어있지않다면
				
				colCnt = row.getLastCellNum();
				returnArray[r] = new String[colCnt];

				for (c = row.getFirstCellNum(); c <= colCnt; c++) { // 각 행의 마지막 cell까지
					// 행에대한 셀을 하나씩 추출하여 셀 타입에 따라 처리
					cell = row.getCell(c); // cell의 index

					if (cell != null) {
						data = null; // 여기서는 모든 cell의 값을 string으로 변환
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_BOOLEAN:
							boolean bdata = cell.getBooleanCellValue();
							data = String.valueOf(bdata);
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							// cell의 값이 numeric일 경우 날짜와 숫자 두가지일 경우이다.
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								data = dateFormat.format(cell.getDateCellValue());
							} else {
								data = String.valueOf(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING:
							data = cell.toString();
							break;
						case HSSFCell.CELL_TYPE_BLANK:
						case HSSFCell.CELL_TYPE_ERROR:
							// 수식일 경우 기존의 바로 처리하는 방식이 아니라 수식을 다시 계산한 후
							// 해당 값이 어떤 type인지 확인해서 처리한다.
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							if (!(cell.toString() == "")) {
								cellType = evaluator.evaluateFormulaCell(cell);
								if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
									data = String.valueOf(cell.getNumericCellValue());
								} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
									data = cell.getStringCellValue();
								} else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
									data = String.valueOf(cell.getBooleanCellValue());
								}
								break;
							}
						default:
							continue;
						}
						returnArray[r][c] = data;
					}
				}
			}
		}
		
		return returnArray;
	}
}
