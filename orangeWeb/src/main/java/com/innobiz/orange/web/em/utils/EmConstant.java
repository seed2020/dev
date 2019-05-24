package com.innobiz.orange.web.em.utils;

public class EmConstant {
	
	/** 문서뷰어 WEB 경로명 */
	public static final String VIEWER = "viewer";
	
	/** 문서뷰어 미리보기 경로명 */
	public static final String PREVIEW = "preview";
	
	/** 문서뷰어 원본파일 복호화 경로명 */
	public static final String DECRYPT_PATH = "decrypt";
	
	/** 문서뷰어 확장자 */
	public static final String[] EXTENSIONS = {
		"hwp","doc","docx","ppt","pptx","xls","xlsx","pdf","txt","png","jpg","jpeg","gif","tif","tiff"
	};
	
	/** 수신자 이메일 최대 길이(Byte) */
	public static final Integer MAX_RECIPIENTS = 2000;
	
	/** SNS 목록 */
	public static final String[] SNS_LIST = new String[]{"facebook", "twitter","naverband","kakaostory","instargram"};
	
	/** SNS 설정 - 포털 기능 이용 저장 (PT_SYS_SETUP_D) */
	public static final String EM_SYS_CONFIG = "em.envConfig";
	
	/** SNS HTML 경로 */
	public static final String SNS_URL = "/sns";
	
	/** SNS HTML 확장자 */
	public static final String SNS_EXT = ".html";
	
	/** 사용자 설정 */
	public final static String USER_SETUP = "USER_SETUP";
	
}
