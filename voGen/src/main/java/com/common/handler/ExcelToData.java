package com.common.handler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
public class ExcelToData {
	
	// 엑셀 ==> 테이블VO 목록
	public Map<String, Object> getExcelToTableMap(Properties prop, List<String> errorList) throws IOException{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		URL classUrl=this.getClass().getClassLoader().getResource(".");
		
		// 파일 경로
		String fileDir = prop.getProperty("excel.dir");
		
		// 데이터를 가지고 올 엑셀 시트명[콤마로 구분]
		String sheetNm = prop.getProperty("excel.sheet.name");
		
		// 시트명 목록
		String[] sheetNmList = sheetNm.split(",");
		
		String path=classUrl.toString().replaceAll("file:/", "")+fileDir;
		//System.out.println("path : "+path);
		File dir = new File(path);
		
		ExcelFileFilter excelFileFilter = new ExcelFileFilter();
		File[] fileList = dir.listFiles(excelFileFilter);
		
		// 파일이 없을 경우 리턴
		if(fileList==null || fileList.length==0) {
			errorList.add("로드할 파일이 없음 - ["+path+"]");
			return null;
		}
		
		if(fileList!=null && fileList.length>0) {
			
			File excelFile = fileList[0];
			
			String[] tabNames = sheetNmList;
			Map<String, String[][]> map = ExcelReader.readToMap(excelFile, tabNames);
			
			String[] mandatorys = sheetNmList;
			
			String[][] sheet;
			// 탭 체크
			for(int i=0;i<mandatorys.length;i++){
				sheet = map.get(mandatorys[i]);
				if(sheet==null){
					errorList.add("["+tabNames[i]+"] 텝 없음");
				}
			}
			if(!errorList.isEmpty()) return null;
			
			// 테이블 목록
			List<String> tableList = new ArrayList<String>();

			// 컬럼 목록맵
			Map<String, List<Map<String,String>>> colmListMap = new HashMap<String, List<Map<String,String>>>();

			// pk 목록맵
			Map<String, List<String>> pkMapList=new HashMap<String, List<String>>();
			
			// 엑셀 작업
			for(int i=0;i<tabNames.length;i++){
				sheet = map.get(tabNames[i]);
				if(sheet==null) continue;
				processData(sheet, tableList, colmListMap, pkMapList, errorList);
				if(!errorList.isEmpty()) break;
			}
			
			returnMap.put("tableList", tableList);
			returnMap.put("colmListMap", colmListMap);
			returnMap.put("pkMapList", pkMapList);
		}
		
		return returnMap;
	}
	
	/** 요청 엑셀 데이터 처리 */
	private void processData(String[][] sheet, List<String> tableList, Map<String, List<Map<String,String>>> colmListMap, Map<String, List<String>> pkMapList, List<String> errorList){
		// 타이틀 항목 전환 맵
		Map<String, String> attrIdMap = new HashMap<String, String>();
		attrIdMap.put("테이블명", "tableNm");
		attrIdMap.put("테이블ID", "tableId");
		attrIdMap.put("속성명", "colmNm");
		attrIdMap.put("속성ID", "colmId");
		attrIdMap.put("타입", "dataTyp");
		attrIdMap.put("길이", "dataLen");
		attrIdMap.put("PK", "pkYn");
		attrIdMap.put("NULL", "nullYn");
		attrIdMap.put("FK", "fkYn");
		attrIdMap.put("검색", "srchTyp");
		
		// 필수 항목
		String[] mandatorys =  new String[]{"tableId", "colmId", "dataTyp"};
		
		// 엑셀 리스트
		List<Map<String, String>> list = processSheet(sheet, attrIdMap, null, null, mandatorys);
		if(list==null || list.isEmpty()) return;
		
		// 컬럼 목록
		List<Map<String,String>> colmList;
		
		// pk 목록
		List<String> pkList;
		
		Map<String, String> colmMap;
		String tableId, pkYn;
		boolean first;
		String tableString;
		for(Map<String, String> map : list){
			tableId = map.get("tableId");
			first=!colmListMap.containsKey(tableId);
			if(first){
				tableString=tableId;
				if(map.get("tableNm")!=null)tableString+=":"+map.get("tableNm");
				tableList.add(tableString);
				colmList=new ArrayList<Map<String,String>>();
				colmListMap.put(tableId, colmList);
			}
			
			pkYn=map.get("pkYn");
			if(pkYn!=null && "Y".endsWith(pkYn)) {
				if(first) pkMapList.put(tableId, new ArrayList<String>());
				pkList=pkMapList.get(tableId);
				pkList.add(map.get("colmId"));
			}
			colmList=colmListMap.get(tableId);
			colmMap=new HashMap<String, String>();
			colmMap.putAll(map);
			colmList.add(colmMap);
		}
		
		/*returnMap.put("tableList", tableList);
		returnMap.put("colmListMap", colmListMap);
		returnMap.put("pkMapList", pkMapList);*/
		
		// System.out.println("pkMapList.size() : "+pkMapList.size());
		//System.out.println("colmListMap.size() : "+colmListMap.size());
				
	}
	
	/** 엑셀 쉬트에서 데이터를 가져옴 */
	public List<Map<String, String>> processSheet(String[][] sheet,
			Map<String, String> attrIdMap,
			String treeAttrId, String treeAttrNm,
			String[] mandatorys){
		if(sheet==null || sheet.length<1) return null;
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		
		int r, c, i, rowCnt = sheet.length, colCnt = sheet[0].length, colSize;
		
		// 데이터 모을 attrId로 전환
		String[] attrIds = new String[colCnt];
		String[] treeNos = null;
		int treeAttrNmLen = 0;
		if(treeAttrNm!=null){
			treeAttrNmLen = treeAttrNm.length();
			treeNos = new String[colCnt];
		}
		for(i=0;i<colCnt;i++){
			if(treeAttrNmLen>0 && sheet[0][i].startsWith(treeAttrNm) && sheet[0][i].indexOf('[')<0){
				attrIds[i] = treeAttrId;
				treeNos[i] = sheet[0][i].substring(treeAttrNmLen);
			} else {
				attrIds[i] = attrIdMap.get(sheet[0][i]);
			}
		}
		
		boolean valid;
		String[] row;
		String attrId, value;
		Map<String, String> map;
		for(r = 1; r<rowCnt; r++){
			map = new HashMap<String, String>();
			row = sheet[r];
			if(row==null){
				continue;
			}
			colSize = row.length;
			
			// map 에 엑셀 데이터 담기
			for(c=0;c<colCnt;c++){
				attrId = attrIds[c];
				if(c<row.length){
					value = row[c];
					if(c<colSize && attrId != null && value != null && !value.isEmpty()) {
						map.put(attrId, value);
						if(treeNos != null && treeNos[c] != null){
							map.put("treeLevel", treeNos[c]);
						}
					}
				}
			}
			
			// 필수 항목 있는지 검사
			valid = true;
			for(i=0; i<mandatorys.length; i++){
				value = map.get(mandatorys[i]);
				if(value==null || value.isEmpty()){
					valid = false;
					break;
				}
			}
			
			if(valid){
				returnList.add(map);
			}
		}
		
		return returnList;
	}
	
	/** 엑셀파일 필터 */
	public class ExcelFileFilter implements FilenameFilter{
	    @Override
	    public boolean accept(File dir, String name){
	        boolean isAccept = false;
	        if (name.toLowerCase().endsWith(".xls")==true || name.toLowerCase().endsWith(".xlsx")==true){
	            isAccept = true; 
	        }else{
	            isAccept = false;
	        }  

	        return isAccept; 

	    } 

	}
	
}
