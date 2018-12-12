package com.common.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class FileCreator {

	private static FileCreator voFileCreat = new FileCreator();

	private FileCreator() {
	}

	public static FileCreator getInstance() {
		return voFileCreat;
	}
	
	private void exportFile(String fileDir, String fileString, String fileNm) {
		
		BufferedWriter fw = null;
		try {
			
			// 파일 저장 경로
			String savePath=fileDir+File.separator+fileNm;
			savePath = savePath.replace('\\', '/');

			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savePath), "UTF-8"));
			// 파일안에 문자열 쓰기
			fw.write(fileString);
			fw.flush();
			
			// 객체 닫기
			fw.close(); 
			
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) try { fw.close(); } catch (Exception e) {}
		}
		
		
	}
	
	/** 파일 생성*/
	public void createFile(String tableScript, List<Map<String, String>> voList, List<Map<String,String>> mybatisList, String fileDir, StringBuilder log) {
		
		// 폴더 생성
		File dir = new File(fileDir);
		if(!dir.isDirectory()) dir.mkdirs();
		
		log.append("※ File Path : ").append(fileDir).append("\n\n");
		
		String fileNm;
		if(tableScript!=null) {
			fileNm="TABLE_SQL.txt"; // 파일명
			log.append("@ DB Script File Create\n"); // 로그
			log.append("File Name : ").append(fileNm).append("\n\n");
			exportFile(fileDir, tableScript, fileNm); // 파일 생성
		}
		
		if(voList!=null) {
			log.append("@ Vo Class File Create\n");// 로그
			log.append("File Name : ");
			String fileString;
			boolean first=true;
			for(Map<String, String> voMap : voList) {
				fileNm=voMap.get("voNm")+".java"; // 파일명
				fileString=voMap.get("voString");
				if(!first) log.append(", ");
				if(first) first=false;
				log.append(fileNm);
				exportFile(fileDir, fileString, fileNm); // 파일 생성
			}
			log.append("\n\n");
			
		}
		
		if(mybatisList!=null) {
			log.append("@ MyBatis XML File Create\n");// 로그
			log.append("File Name : ");
			String fileString;
			boolean first=true;
			for(Map<String, String> voMap : mybatisList) {
				fileNm="mapper-"+voMap.get("xmlNm")+".xml"; // 파일명
				fileString=voMap.get("xmlString");
				if(!first) log.append(", ");
				if(first) first=false;
				log.append(fileNm);
				exportFile(fileDir, fileString, fileNm); // 파일 생성
			}
		}
		
	}
	
	/** 로그 파일 생성*/
	public void createLogFile(String logString, String fileDir) {
		
		// 폴더 생성
		File dir = new File(fileDir);
		if(!dir.isDirectory()) dir.mkdirs();
		
		String fileNm="voGenLog.txt"; // 파일명
		exportFile(fileDir, logString, fileNm); // 파일 생성
		
	}

}
