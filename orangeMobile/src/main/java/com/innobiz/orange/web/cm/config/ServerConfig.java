package com.innobiz.orange.web.cm.config;

import java.io.File;

/** 서버 환경 */
public class ServerConfig {
	
	/** 개발 PC 여부 */
	public final static Boolean IS_LOC = "LOC".equals(System.getProperty("run_mode"));
	
	/** 개발 서버 여부 */
	public final static Boolean IS_DEV = "DEV".equals(System.getProperty("run_mode"));
	
	/** 운연 서버 여부 */
	public final static Boolean IS_RUN = !IS_LOC && !IS_DEV;
	
	/** 모바일 여부 */
	public final static Boolean IS_MOBILE = true;
	
	/** 리눅스 여부 */
	public final static Boolean IS_LINUX = "/".equals(File.separator);
	
	/** 프라임 설정 - 디폴트 */
	public final static Boolean IS_PRIME = Boolean.FALSE;
	
}
