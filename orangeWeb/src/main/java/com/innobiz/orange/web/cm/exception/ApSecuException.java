package com.innobiz.orange.web.cm.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/** 결재 보안 Exception */
public class ApSecuException extends CmException {

	/** serialVersionUID */
	private static final long serialVersionUID = 7379121946153058308L;

	/** 생성자 */
	public ApSecuException() {
		super();
	}

	/** 생성자 */
	public ApSecuException(String messageId, HttpServletRequest request) {
		super(messageId, request);
	}

	/** 생성자 */
	public ApSecuException(String messageId, Locale locale) {
		super(messageId, locale);
	}

	/** 생성자 */
	public ApSecuException(String messageId, String[] args, HttpServletRequest request) {
		super(messageId, args, request);
	}

	/** 생성자 */
	public ApSecuException(String messageId, String[] args, Locale locale) {
		super(messageId, args, locale);
	}

	/** 생성자 */
	public ApSecuException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 생성자 */
	public ApSecuException(String message) {
		super(message);
	}

	/** 생성자 */
	public ApSecuException(Throwable cause) {
		super(cause);
	}
}
