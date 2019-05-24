package com.innobiz.orange.web.cm.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDownLoadView extends AbstractPOIExcelView {
	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		List<String> colNames = null;
		Map<String,Object> colValues = null;
		List<Integer> widthList;
		
		CellStyle cs = workbook.createCellStyle();
		cs.setBorderTop(CellStyle.BORDER_THIN);
		//cs.setWrapText(true);
		
		CellStyle rowStyle = workbook.createCellStyle();
		rowStyle.setWrapText(true);
		rowStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		
		//시트를 여러개 생성할지...
		if(model.containsKey("sheetList") && model.get("sheetList") instanceof List ){
			List<Map<String,Object>> sheetList = (List<Map<String,Object>>)model.get("sheetList");
			Map<String,Object> sheetMap = null;
			if(sheetList.size() == 0 ) throw new IOException("sheetList is Null");
			for(int i=0;i<sheetList.size();i++){
				sheetMap = sheetList.get(i);
				
				colNames = (List<String>)sheetMap.get("colNames");
				colValues = (Map<String,Object>)sheetMap.get("colValues");
				widthList = (List<Integer>)sheetMap.get("widthList");
				
				if(colValues==null) throw new IOException("colValues is Null");
				createSheet(workbook, (String)sheetMap.get("sheetName"), colNames, colValues, cs, rowStyle, widthList, i);
			}
		}else{
			colNames = (List<String>) model.get("colNames");
			colValues = (Map<String,Object>) model.get("colValues");
			widthList = (List<Integer>)model.get("widthList");
			
			if(colValues==null) throw new IOException("colValues is Null");
			createSheet(workbook, (String) model.get("sheetName"), colNames, colValues, cs, rowStyle, widthList, null);
		}
		
		//파일명
		String realFileName = getFileNmConvert(request , (String) model.get("fileName"));
		String ext = (String)model.get("ext");
		if(ext == null || "".equals(ext)) ext = "xls";
		realFileName+="."+ext;
		response.setHeader("Content-Disposition", "attachment; filename=\""	+ realFileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
	}
	
	//시트 생성
	@SuppressWarnings("unchecked")
	private void createSheet(Workbook workbook, String sheetName, List<String> colNames, Map<String,Object> colValues,
			CellStyle cs, CellStyle rowStyle, List<Integer> widthList, Integer sheetOrder){
		Sheet sheet = workbook.createSheet();
//		CellStyle cs = workbook.createCellStyle();
//		cs.setBorderTop(CellStyle.BORDER_THIN);
		if(sheetName!=null)
			workbook.setSheetName(sheetOrder == null ? 0 : sheetOrder.intValue(), sheetName);
		if(colNames != null ) creatColum(sheet, colNames, cs);
		
		for ( int i = 0; i < colValues.size(); i++ ) createRow(sheet, rowStyle, (List<Object>)colValues.get("col"+i), i+1);
		if(widthList==null){
			if(colNames != null ) {
				for (int i = 0; i < colNames.size(); i++) sheet.autoSizeColumn(i);
			}
		} else {
			for(int i = 0; i < widthList.size(); i++){
				sheet.setColumnWidth(i, widthList.get(i).intValue());
			}
		}
		
	}
	
	// 각 컬럼에 대한 값 입력
	private void creatColum(Sheet sheet, List<String> coln, CellStyle cs) {
		Row header = sheet.createRow(0);
		Cell cell = null;
		for (int i = 0; i < coln.size(); i++) {
			cell = header.createCell(i);
			cell.setCellValue(coln.get(i));
			cell.setCellStyle(cs);
		}
	}

	// 각 로우에 대한 값 입력
	private void createRow(Sheet sheet, CellStyle cs, List<Object> colv, int rowNum) {
		Row row = sheet.createRow(rowNum);
		Cell cell = null;
		for ( int i = 0 ; i < colv.size(); i++ ) {
			cell = row.createCell(i);
			if(cs!=null)
				cell.setCellStyle(cs);
			if(colv.get(i) instanceof Integer){
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Integer)colv.get(i));
			}else{
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue((String)colv.get(i));
			}
			
		}
	}

	//Workbook 객체 생성
	@Override
	protected Workbook createWorkbook(String ext) {
		if(ext != null && "xlsx".equals(ext)) return new XSSFWorkbook();
		return new HSSFWorkbook();
	}
	
	private String getFileNmConvert(HttpServletRequest request , String fileName) throws UnsupportedEncodingException{
		if(fileName == null || "".equals(fileName)) fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
		String userAgent = request.getHeader("User-Agent");
		if(userAgent==null || userAgent.isEmpty()) userAgent = "";
		// IE, Chrome 의 경우 CharSet 을 맞춰야 파일 다운로드명이 깨지지 않으며
		// 디폴트는 EUC-KR 이며
		// 한글을 사용하지 않는 경우를 대비하여 프로퍼티에서 설정 하도록 함
		
		if (userAgent.contains("Chrome")) {
			StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            fileName = sb.toString();	
		} else if (userAgent.contains("Safari")) {
			fileName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
			//if(ServerConfig.IS_MOBILE) contDisp = "inline";
		} else if (userAgent.contains("Firefox")) {
			fileName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
		} else{
			fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		}
		
		/*String encCharSet = MessageProperties.getContextProperties().getProperty("upload.filename.charset", "EUC-KR");
		
		if (userAgent.contains("MSIE")) { 
			fileName = new String( fileName.getBytes(encCharSet), "ISO-8859-1").replaceAll(" ","%20");
		} else if (userAgent.contains("Opera") || userAgent.contains("Safari") || userAgent.contains("Firefox")) {
			fileName = new String( fileName.getBytes("UTF-8"), "ISO-8859-1");
		} else if (userAgent.contains("Chrome")) {
			fileName = new String( fileName.getBytes(encCharSet), "ISO-8859-1");
		} else{
			fileName = new String( fileName.getBytes(encCharSet), "ISO-8859-1");
		}*/
		return fileName;
	}
}
