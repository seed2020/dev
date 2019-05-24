package com.innobiz.orange.web.cm.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/** 관리자 페이지를 외부에서 접근 할 때 발생 Exception */
public class InternetAccessException extends CmException {

	/** serialVersionUID */
	private static final long serialVersionUID = 8298271767374480444L;

	/** IP */
	private String ip;
	
	/** 생성자 */
	public InternetAccessException() {
		super();
	}

	/** 생성자 */
	public InternetAccessException(String messageId, HttpServletRequest request) {
		super(messageId, request);
	}

	/** 생성자 */
	public InternetAccessException(String messageId, Locale locale) {
		super(messageId, locale);
	}

	/** 생성자 */
	public InternetAccessException(String messageId, String[] args, HttpServletRequest request) {
		super(messageId, args, request);
	}

	/** 생성자 */
	public InternetAccessException(String messageId, String[] args, Locale locale) {
		super(messageId, args, locale);
	}

	/** 생성자 */
	public InternetAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 생성자 */
	public InternetAccessException(String message) {
		super(message);
	}

	/** 생성자 */
	public InternetAccessException(Throwable cause) {
		super(cause);
	}
	
	/** IP 세팅 */
	public InternetAccessException setIp(String ip){
		this.ip = ip;
		return this;
	}
	/** IP 리턴 */
	public String getIp(){
		return ip;
	}
}
