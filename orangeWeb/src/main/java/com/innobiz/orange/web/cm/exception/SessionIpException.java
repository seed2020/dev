package com.innobiz.orange.web.cm.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/** 세션 IP Exception */
public class SessionIpException extends CmException {

	/** serialVersionUID */
	private static final long serialVersionUID = 7432742017957250594L;

	/** IP */
	private String ip;
	
	/** 생성자 */
	public SessionIpException() {
		super();
	}

	/** 생성자 */
	public SessionIpException(String messageId, HttpServletRequest request) {
		super(messageId, request);
	}

	/** 생성자 */
	public SessionIpException(String messageId, Locale locale) {
		super(messageId, locale);
	}

	/** 생성자 */
	public SessionIpException(String messageId, String[] args, HttpServletRequest request) {
		super(messageId, args, request);
	}

	/** 생성자 */
	public SessionIpException(String messageId, String[] args, Locale locale) {
		super(messageId, args, locale);
	}

	/** 생성자 */
	public SessionIpException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 생성자 */
	public SessionIpException(String message) {
		super(message);
	}

	/** 생성자 */
	public SessionIpException(Throwable cause) {
		super(cause);
	}
	
	/** IP 세팅 */
	public SessionIpException setIp(String ip){
		this.ip = ip;
		return this;
	}
	/** IP 리턴 */
	public String getIp(){
		return ip;
	}
}
