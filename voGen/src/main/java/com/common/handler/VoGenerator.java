package com.common.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class VoGenerator {
	
	private static VoGenerator voGen = new VoGenerator();
	
	private static final String PROP_NAME="config/gen.properties";
	
	private Properties prop;
	
	private VoGenerator(){
		prop = new Properties();
		
		// 프로퍼티 로드
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROP_NAME);
		 
		try{
			prop.load(in);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException, IOException, Exception {
		
		 // 시작
        long start = System.currentTimeMillis();
 		
        StringBuilder log = new StringBuilder();
        
        log.append("==============================VoGenerator Start==============================\n");
				
		// 오류메세지 목록
		List<String> errorList = new ArrayList<String>();
		
		ExcelToData excelToData = new ExcelToData();
		Map<String, Object> tableMap = excelToData.getExcelToTableMap(voGen.prop, errorList);
		
		// 내보내기할 파일 기본 경로
		String fileDir = voGen.prop.getProperty("file.dir");
		if(fileDir==null || fileDir.isEmpty()) fileDir="d:/temp";
					
		if(errorList.size()>0) {
			log.append(errorList.toString());
		}else {
			CodeGenerator code = new CodeGenerator();
			
			try {
				// 테이블스크립트 생성여부
				String tableYn = voGen.prop.getProperty("create.table");
				boolean isTable = tableYn!=null && "Y".equals(tableYn);
				// 테이블 스크립트 추가
				String tableScript = isTable ? code.createTableScript(voGen.prop, tableMap) : null; 
				
				// VO 클래스 생성여부
				String voYn = voGen.prop.getProperty("create.vo");
				boolean isVoClass = voYn!=null && "Y".equals(voYn);
				// VO 목록맵
				List<Map<String,String>> voList = isVoClass ? code.createVoClass(voGen.prop, tableMap) : null; 
				
				// mybatis xml 생성여부
				String mybatisYn = voGen.prop.getProperty("create.mybatis");
				boolean isMybatis = mybatisYn!=null && "Y".equals(mybatisYn);
				// mybatis xml 목록맵
				List<Map<String,String>> mybatisList = isMybatis ? code.createMyBatisXML(voGen.prop, tableMap) : null; 
				
				// 파일 생성
				FileCreator.getInstance().createFile(tableScript, voList, mybatisList, fileDir, log);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// 종료
        long end = System.currentTimeMillis();
        
        log.append("\n\ntime : ").append((end-start)/1000.0f).append("\n");
		log.append("==============================VoGenerator End==============================\n");
		
		// 콘솔로그 여부
		String consoleLogYn = voGen.prop.getProperty("log.console");
		
		if(consoleLogYn!=null && "Y".equals(consoleLogYn)) {
			System.out.println(log.toString());
		}
		
		// 파일로그 여부
		String fileLogYn = voGen.prop.getProperty("log.file");
		if(fileLogYn!=null && "Y".equals(fileLogYn)) {
			FileCreator.getInstance().createLogFile(log.toString(), fileDir);
		}		
		
	}
	
}
