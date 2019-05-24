package com.innobiz.orange.web.cm.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/** 라이센스에서 사용하는 숨김용 프로퍼티 */
public class Properties extends java.util.Properties {

	/** serialVersionUID */
	private static final long serialVersionUID = 4621192017263309656L;
	
	/** 숨김용 프로퍼티 */
	private java.util.Properties hidden = new java.util.Properties();
	
	/** 생성자 */
	public Properties(){}
	
	/** 생성자 */
	public Properties(String text) throws IOException{
		this.load(new ByteArrayInputStream(text.getBytes("UTF-8")));
	}
	/** 로드 */
	public void load(InputStream inStream) throws IOException {
		hidden.load(inStream);
	}
	/** 프로퍼티 리턴 */
	public String getProperty(String key) {
		String value = hidden.getProperty(key);
		return value != null ? value : super.getProperty(key);
	}
	/** 프로퍼티 리턴 */
	public String getProperty(String key, String defaultValue) {
		String value = hidden.getProperty(key);
		return value != null ? value : super.getProperty(key, defaultValue);
	}
}
