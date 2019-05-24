package com.innobiz.orange.web.cm.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public class LicenseLimitException extends LicenseException {

	/** serialVersionUID */
	private static final long serialVersionUID = 7955139401227577408L;

	/** 생성자 */
	public LicenseLimitException() {
		super();
	}

	/** 생성자 */
	public LicenseLimitException(String messageId, HttpServletRequest request) {
		super(messageId, request);
	}

	/** 생성자 */
	public LicenseLimitException(String messageId, Locale locale) {
		super(messageId, locale);
	}

	/** 생성자 */
	public LicenseLimitException(String messageId, String[] args, HttpServletRequest request) {
		super(messageId, args, request);
	}

	/** 생성자 */
	public LicenseLimitException(String messageId, String[] args, Locale locale) {
		super(messageId, args, locale);
	}

	/** 생성자 */
	public LicenseLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 생성자 */
	public LicenseLimitException(String message) {
		super(message);
	}

	/** 생성자 */
	public LicenseLimitException(Throwable cause) {
		super(cause);
	}	
}
