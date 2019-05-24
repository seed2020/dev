package com.innobiz.orange.web.wh.utils;

public class WhConstant {
	
	/** 시스템 설정의 카테고리ID - 포털 기능 이용 저장 (PT_SYS_SETUP_D) */
	public static final String SYS_CONFIG = "wh.envConfig";
	
	/** 사용자 설정의 카테고리ID - 공통 기능 이용 저장 (EM_USER_SETUP_D) */
	public static final String USER_CONFIG = "wh.userConfig";
	
	/** 모듈정보 */
	public final static String MD = "MD";
	
	/** 문서번호 연도 기본 */
	public final static String DOC_NO_YEAR = "0000";
	
	/** zip 으로 묶어서 다운로드 하는 파일 명 */
	public static final String DOWN_ZIP_FILE_NAME = "attachFiles.zip";
	
	/** 월 */
	public static final int[] MONTHS = {1,2,3,4,5,6,7,8,9,10,11,12};
	
	/** 일 */
	public static final int[] DAYS = {
		1,2,3,4,5,6,7,8,9,10,
		11,12,13,14,15,16,17,18,19,20,
		21,22,23,24,25,26,27,28,29,30,
		31};
}
