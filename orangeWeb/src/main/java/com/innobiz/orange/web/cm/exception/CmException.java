package com.innobiz.orange.web.cm.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.innobiz.orange.web.cm.utils.MessageProperties;

/** 공통 Exception - 메세지 처리를 위한 공통 Exception */
public class CmException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = -4911551750864884028L;

	/** 생성자 */
	public CmException() {
	}

	/** 생성자 */
	public CmException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 생성자 */
	public CmException(String message) {
		super(message);
	}

	/** 생성자 */
	public CmException(Throwable cause) {
		super(cause);
	}

	/** 생성자 */
	public CmException(String messageId, HttpServletRequest request){
		super(MessageProperties.getInstance().getMessage(messageId, null, request));
	}

	/** 생성자 */
	public CmException(String messageId, String[] args, HttpServletRequest request){
		super(MessageProperties.getInstance().getMessage(messageId, args, request));
	}

	/** 생성자 */
	public CmException(String messageId, Locale locale){
		super(MessageProperties.getInstance().getMessage(messageId, null, locale));
	}

	/** 생성자 */
	public CmException(String messageId, String[] args, Locale locale){
		super(MessageProperties.getInstance().getMessage(messageId, args, locale));
	}
}
