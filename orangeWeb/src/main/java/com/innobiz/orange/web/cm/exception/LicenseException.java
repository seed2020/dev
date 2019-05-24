package com.innobiz.orange.web.cm.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/** 라이센스 Exception */
public class LicenseException extends CmException {

	/** serialVersionUID */
	private static final long serialVersionUID = 6897912845718205708L;

	/** 생성자 */
	public LicenseException() {
		super();
	}

	/** 생성자 */
	public LicenseException(String messageId, HttpServletRequest request) {
		super(messageId, request);
	}

	/** 생성자 */
	public LicenseException(String messageId, Locale locale) {
		super(messageId, locale);
	}

	/** 생성자 */
	public LicenseException(String messageId, String[] args, HttpServletRequest request) {
		super(messageId, args, request);
	}

	/** 생성자 */
	public LicenseException(String messageId, String[] args, Locale locale) {
		super(messageId, args, locale);
	}

	/** 생성자 */
	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 생성자 */
	public LicenseException(String message) {
		super(message);
	}

	/** 생성자 */
	public LicenseException(Throwable cause) {
		super(cause);
	}
}
