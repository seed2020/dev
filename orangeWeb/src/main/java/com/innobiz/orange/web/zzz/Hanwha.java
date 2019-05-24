package com.innobiz.orange.web.zzz;

import java.io.File;
import java.io.IOException;

public class Hanwha {
	
	/** JAVA 및 XML 주석 제거  */
	public static void main(String[] arg) throws IOException{
		
		// 웹 java 주석 제거
		JavaCmtRemover.removeCmtByFile(new File(
				"D:\\dev\\Workspace\\orangeWeb_HANWHA\\src\\main\\java"));
		
		// 모바일 java 주석 제거
		JavaCmtRemover.removeCmtByFile(new File(
				"D:\\dev\\Workspace\\orangeMobile_HANWHA\\src\\main\\java"));
		
		// xml 주석 제거
		XmlCmtRemover.removeCmtByFile(new File(
				"D:\\dev\\Workspace\\orangeWeb_HANWHA\\src\\main\\resources\\sql\\oracle"));
		
		// .project 파일 편집
		PrjConverter.convert(
				"D:\\dev\\Workspace\\orangeMobile_HANWHA\\.project",
				"<link>",
				new String[]{"context.properties","gwOrange.license","settings"}
				);
		
	}
}

/*
-- 한화개발
서버 : 192.168.10.22
계정 : root / 12345678
경로 : /app/src

 */